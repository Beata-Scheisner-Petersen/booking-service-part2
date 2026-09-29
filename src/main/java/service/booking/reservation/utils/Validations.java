package service.booking.reservation.utils;

import service.booking.exceptionhandler.customexeptions.BadRequestException;

import java.time.LocalDate;

public class Validations {

    public static void validateDateRange(LocalDate checkIn, LocalDate checkOut){
        if (checkIn.isAfter(checkOut)|| checkIn==checkOut) {
            throw new BadRequestException("CheckIn date should be before check-out date.");
        }

    }

}
