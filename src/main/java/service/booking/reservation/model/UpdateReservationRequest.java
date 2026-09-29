package service.booking.reservation.model;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class UpdateReservationRequest {

    @NotNull(message = "Check-in date is required")
    @FutureOrPresent(message = "Date has to be in the future")
    private LocalDate checkIn;

    @NotNull  (message = "Check-out date is required")
    @Future(message = "Date has to be in the future")
    private LocalDate checkOut;

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }
}
