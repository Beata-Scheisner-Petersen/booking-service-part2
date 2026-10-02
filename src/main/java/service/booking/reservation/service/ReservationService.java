package service.booking.reservation.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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
    final Logger logger = LoggerFactory.getLogger(ReservationService.class);

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

    public List<GetAllCustomerReservationsDto> getAllReservationByCustomerId(Long customerId) {

        logger.info("get all reservation that customer with id {} have", customerId);
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
            logger.error("Customer with id {} not found then creating reservation", request.getCustomerId());
            throw new NotFoundException("Customer not found");
        }

        Room room = roomRepository
                .findById(request.getRoomId())
                .orElseThrow(
                        () -> {
                            logger.error("Room with number {} does not exist", request.getRoomId());
                             return new NotFoundException("The room does not exist.");
                        }
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

        logger.info("Reservation is created");
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
            logger.warn("bookingToIgnore is not null");
            bookings = bookings.stream()
                    .filter(
                            b -> !b.getId().equals(bookingToIgnore)
                    ).toList()
            ;
        }
        if (!bookings.isEmpty()) {
            logger.error("room {} is already booked for selected dates", roomId);
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
            logger.error(
                    "customer tried to book {} guests in room {} that can accommodate a maximum of {} guests",
                    requestedGuests, room.getId(), maxCapacity
            );
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
            logger.info("add price for extra bed per day");
            extraBedPricePerDay = BigDecimal.valueOf(500);
        }

        BigDecimal roomPricePerDay = room.getRoomPrice();

        //Price for summer -  add 30%
        BigDecimal extraPriceForHighSeason = BigDecimal.ONE;

        if (checkIn.getMonthValue() >= 6 && checkIn.getMonthValue() <= 8) {

            logger.info("Add high season price");
            extraPriceForHighSeason = BigDecimal.valueOf(1.3);
        }
        logger.info("total price is calculated.");
        return (roomPricePerDay
                .add(extraBedPricePerDay)
                .multiply(extraPriceForHighSeason)
                .multiply(BigDecimal.valueOf(days))
        );
    }

    public Reservation cancelReservation(Long reservationId, Long customerId) {

        Reservation reservation = getReservationById(reservationId);

        if (!reservation.getCustomerId().equals(customerId)) {
            logger.error("Customer {} tried to cancel reservation for customer {}", customerId, reservation.getCustomerId());
            throw new ForbiddenException("You cannot cancel another customer's reservation");
        }

        reservation.setStatus(
                ReservationStatus.CANCELED
        );

        logger.info("Reservation with id {} is cancelled", reservationId);
        return reservationRepository.save(reservation);
    }

    public Reservation getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(
                        () -> {
                            logger.error("Reservation with id {} does not exist", reservationId);
                            return new NotFoundException("Reservation does not exist");
                        }
                );
    }

    public Reservation updateReservation(Long reservationId,Long customerId, LocalDate checkIn, LocalDate checkOut) {

        Reservation reservation = getReservationById(reservationId);

        if (!reservation.getCustomerId().equals(customerId)){
            logger.error("Customer {} tried to update customer {} reservation with id {}",
                    customerId, reservation.getCustomerId(), reservationId
            );
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

        logger.info("Reservation with id {} is updated", reservationId);
        return reservationRepository.save(reservation);
    }


    public List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut, int guests) {
        validateDateRange(checkIn, checkOut);

        logger.info("Getting available rooms.");
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
        logger.info("Getting if customer with id {} have active reservations", customerId);
        return reservationRepository.existsByCustomerIdAndStatus (customerId, ReservationStatus.ACTIVE);
    }
}


