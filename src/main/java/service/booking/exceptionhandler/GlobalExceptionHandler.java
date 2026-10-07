package service.booking.exceptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import service.booking.exceptionhandler.customexeptions.*;

import java.util.HashMap;
import java.util.Map;

/**
 * GlobalExceptionHandler = handles everything else
 * Business Error
 * Validation Error
 * Database Error
 * Controller Error
 * JWT error never makes it here.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationError(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().
                getFieldErrors()
                .forEach(
                        error -> errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );
        var response = ResponseEntity.badRequest().body(errors);
        printLoggingWarning(response, "handleValidationError");
        return response;
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<String> handleUsernameExists(AlreadyExistException exception) {
       var response = ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
        printLoggingWarning(response, "handleUsernameExists");
        return response;
    }

    @ExceptionHandler(WrongEmailOrPasswordException.class)
    public ResponseEntity<String> handleWrongEmailOrPassword(WrongEmailOrPasswordException exception) {
        var response = ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
        printLoggingWarning(response,"handleWrongEmailOrPassword");
        return response;
    }

    @ExceptionHandler(HaveReservationException.class)
    public ResponseEntity<String> haveReservation(HaveReservationException exception) {
        var response = ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
        printLoggingWarning(response, "haveReservation");
        return response;
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException exception) {
        var response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        printLoggingWarning(response, "handleIllegalArgument");
        return response;
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<String> handleForbiddenException(ForbiddenException exception) {
        var response = ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
        printLoggingWarning(response, "handleForbiddenException");
        return response;
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException exception) {
        var response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        printLoggingWarning(response, "handleNotFoundException");
        return response;
    }

    @ExceptionHandler(ExternalServiceConnectionException.class)
    public ResponseEntity<String> externalServiceConnectionException(ExternalServiceConnectionException exception) {
        var response = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(exception.getMessage());
        printLoggingWarning(response, "externalServiceConnectionException");
        return response;
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> handleHttpClientError(HttpClientErrorException exception) {
        var response = ResponseEntity.status(exception.getStatusCode()).body(exception.getResponseBodyAsString());
        printLoggingWarning(response, "handleHttpClientError");
        return response;
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<String> handleResourceAccess(ResourceAccessException exception) {
        var response = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(exception.getMessage());
        printLoggingWarning(response, "handleResourceAccess");
        return response;
    }

    private void printLoggingWarning(ResponseEntity<?> response, String className) {
        logger.warn("""
                GlobalExceptionHandler: {}
                Status: {}
                Message: {}
                """, className ,response.getStatusCode(), response.getBody()
        );
    }
}