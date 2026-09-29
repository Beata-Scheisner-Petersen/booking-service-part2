package service.booking.reservation.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import service.booking.customerapi.client.CustomerClient;
import service.booking.customerapi.client.CustomerClient;
import service.booking.exceptionhandler.customexeptions.ForbiddenException;
import service.booking.exceptionhandler.customexeptions.NotFoundException;
import service.booking.reservation.model.CreateReservationRequest;
import service.booking.reservation.model.Reservation;
import service.booking.reservation.model.ReservationStatus;
import service.booking.reservation.model.dto.GetAllCustomerReservationsDto;
import service.booking.reservation.repository.ReservationRepository;
import service.booking.roomapi.entity.Room;
import service.booking.roomapi.repository.RoomRepository;
import service.booking.roomapi.service.RoomService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

import static service.booking.reservation.utils.Validations.validateDateRange;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final RoomService roomService;
    private final CustomerClient customerClient;

    public ReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository, RoomService roomService, CustomerClient customerClient) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.roomService = roomService;
        this.customerClient = customerClient;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }


    public List<Reservation> getActiveReservationByCustomerId(Long customerId) {

        return reservationRepository.findByCustomerIdAndStatus(
                customerId,
                ReservationStatus.ACTIVE
        );
    }

    public List<GetAllCustomerReservationsDto> getAllReservationByCustomerId(Long customerId) {

        return reservationRepository.findAllByCustomerId(customerId)
                .stream()
                .sorted(Comparator.comparing(
                        reservation -> reservation.getStatus() != ReservationStatus.ACTIVE)
                ).map(
                        reservation -> new GetAllCustomerReservationsDto(
                                reservation.getId(),
                                reservation.getCustomerId(),
                                reservation.getCheckIn(),
                                reservation.getCheckOut(),
                                reservation.getRoom().getRoomNumber(),
                                reservation.getTotalCost(),
                                reservation.getStatus()
                        )
                ).toList();
    }


    @Transactional
    public Reservation createReservation(CreateReservationRequest request, String jwt) {

        if (!customerClient.customerExists(jwt)){
            throw new NotFoundException("Customer not found");
        }

        Room room = roomRepository
                .findById(request.getRoomId())
                .orElseThrow(
                        () -> new NotFoundException("Rummet finns inte")
                );

        validateDateRange(
                request.getCheckIn(),
                request.getCheckOut()
        );

        validationRoomIsAvailable(
                request.getRoomId(),
                request.getCheckIn(),
                request.getCheckOut(),
                null
        );

        validateRoomCapacity(
                room,
                request.getGuests()
        );

        Reservation reservation = new Reservation(
                request.getCustomerId(),
                room,
                request.getCheckIn(),
                request.getCheckOut(),
                countTotalPrice(
                        room,
                        request.getCheckIn(),
                        request.getCheckOut(),
                        request.getGuests()
                ),
                ReservationStatus.ACTIVE,
                request.getGuests()
        );

        return reservationRepository.save(reservation);
    }


    public void validationRoomIsAvailable(Long roomId,
                                          LocalDate checkIn,
                                          LocalDate checkOut,
                                          Long bookingToIgnore
    ) {
        List<Reservation> bookings = reservationRepository.findByRoom_IdAndStatusAndCheckInBeforeAndCheckOutAfter(
                roomId,
                ReservationStatus.ACTIVE,
                checkOut,
                checkIn
        );

        if (bookingToIgnore != null) {
            bookings = bookings.stream()
                    .filter(
                            b -> !b.getId().equals(bookingToIgnore)
                    ).toList()
            ;
        }
        if (!bookings.isEmpty()) {
            throw new IllegalArgumentException(
                    "Room is already booked for selected dates"
            );
        }
    }

    private void validateRoomCapacity(Room room, int requestedGuests) {
        int maxCapacity = room.getMaxGuests();

        if (room.isExtraBedAvailable()) {
            maxCapacity += 1;
        }

        if (requestedGuests > maxCapacity) {
            throw new IllegalArgumentException(
                    "This room can accommodate a maximum of " + maxCapacity + " guests."
            );
        }
    }

    public BigDecimal countTotalPrice(Room room,
                                      LocalDate checkIn,
                                      LocalDate checkOut,
                                      int guests) {
        long days = ChronoUnit.DAYS.between(
                checkIn,
                checkOut
        );

        BigDecimal extraBedPricePerDay = BigDecimal.ZERO;

        if (guests > room.getMaxGuests()) {
            extraBedPricePerDay = BigDecimal.valueOf(500);
        }

        BigDecimal roomPricePerDay = room.getRoomPrice();

        //Price for summer -  add 30%
        BigDecimal extraPriceForHighSeason = BigDecimal.ONE;

        if (checkIn.getMonthValue() >= 6 && checkIn.getMonthValue() <= 8) {

            extraPriceForHighSeason = BigDecimal.valueOf(1.3);
        }
        return (roomPricePerDay
                .add(extraBedPricePerDay)
                .multiply(extraPriceForHighSeason)
                .multiply(BigDecimal.valueOf(days))
        );
    }

    public Reservation cancelReservation(Long reservationId, Long customerId) {

        Reservation reservation = getReservationById(reservationId);

        if (!reservation.getCustomerId().equals(customerId)) {
            throw new ForbiddenException("You cannot cancel another customer's reservation");
        }

        reservation.setStatus(
                ReservationStatus.CANCELED
        );

        return reservationRepository.save(reservation);
    }

    public Reservation getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(
                        () -> new NotFoundException("Reservation finns inte")
                );
    }

    public Reservation updateReservation(Long reservationId,Long customerId, LocalDate checkIn, LocalDate checkOut) {

        Reservation reservation = getReservationById(reservationId);

        if (!reservation.getCustomerId().equals(customerId)){
            throw new ForbiddenException("You cannot update another customer's reservation");
        }

        validationRoomIsAvailable(
                reservation.getRoom().getId(),
                checkIn,
                checkOut,
                reservationId
        );

        reservation.setCheckIn(checkIn);
        reservation.setCheckOut(checkOut);

        reservation.setTotalCost(
                countTotalPrice(
                        reservation.getRoom(),
                        checkIn, checkOut,
                        reservation.getGuests()
                )
        );

        return reservationRepository.save(reservation);
    }


    public List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut, int guests) {
        validateDateRange(checkIn, checkOut);

        return roomService.getAllRooms()
                .stream()
                .filter(
                        room -> {
                            int maxCapacity = room.getMaxGuests();

                            // add extra place only if room supports extra bed
                            if (room.isExtraBedAvailable()) {
                                maxCapacity += 1;
                            }

                            return maxCapacity >= guests;
                        }
                ).filter(
                        room -> {
                            try {
                                validationRoomIsAvailable(
                                        room.getId(),
                                        checkIn,
                                        checkOut,
                                        null
                                );
                                return true;
                            } catch (RuntimeException e) {
                                return false;
                            }
                        }
                ).toList();
    }

    public boolean hasActiveReservation (Long customerId){
        return reservationRepository.existsByCustomerIdAndStatus (customerId, ReservationStatus.ACTIVE);
    }
}


