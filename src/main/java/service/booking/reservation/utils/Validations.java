package service.booking.reservation.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.booking.exceptionhandler.customexeptions.BadRequestException;

import java.time.LocalDate;

public class Validations {
    static final Logger logger = LoggerFactory.getLogger(Validations.class);

    public static void validateDateRange(LocalDate checkIn, LocalDate checkOut){
        if (checkIn.isAfter(checkOut)|| checkIn==checkOut) {
            logger.error("invalid date selection.");
            throw new BadRequestException("CheckIn date should be before check-out date.");
        }

    }

}
