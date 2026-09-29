package service.booking.reservation.model.dto;

import service.booking.reservation.model.ReservationStatus;
import service.booking.roomapi.entity.Room;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GetAllCustomerReservationsDto(
        Long id,
        Long customerId,
        LocalDate checkIn,
        LocalDate checkOut,
        int roomNumber,
        BigDecimal totalCost,
        ReservationStatus status
) {

}