package service.booking.exceptionhandler.customexeptions;

public class HaveReservationException extends RuntimeException {
    public HaveReservationException(String message){
        super(message);
    }
}
