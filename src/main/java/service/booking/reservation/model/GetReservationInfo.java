package service.booking.reservation.model;

import service.booking.roomapi.entity.Room;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GetReservationInfo(
        LocalDate checkIn,
        LocalDate checkout,
        Room room,
        Boolean isExtraBed,
        BigDecimal totalCost,
        ReservationStatus status
) {
}
