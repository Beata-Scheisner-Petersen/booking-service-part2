package service.booking.exceptionhandler.customexeptions;

public class ExternalServiceConnectionException extends RuntimeException {
    public ExternalServiceConnectionException(String message) {
        super(message);
    }
}
