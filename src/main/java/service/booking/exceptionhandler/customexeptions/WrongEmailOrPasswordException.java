package service.booking.exceptionhandler.customexeptions;

public class WrongEmailOrPasswordException extends RuntimeException{
    public WrongEmailOrPasswordException(String message) {
        super(message);
    }
}
