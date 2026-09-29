package service.booking.reservation.repository;


import service.booking.reservation.model.Reservation;
import service.booking.reservation.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository <Reservation, Long> {
    List<Reservation> findAllByCustomerId(Long customerId);

    List<Reservation> findByRoom_IdAndStatusAndCheckInBeforeAndCheckOutAfter (
            Long roomId,
            ReservationStatus reservationStatus,
            LocalDate checkIn,
            LocalDate checkOut
    );

    List<Reservation> findByCustomerIdAndStatus(Long customerId, ReservationStatus status);
    Boolean existsByCustomerIdAndStatus (Long customerId, ReservationStatus Status);

}

