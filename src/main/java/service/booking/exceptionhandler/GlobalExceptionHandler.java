package service.booking.exceptionhandler;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import service.booking.exceptionhandler.customexeptions.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<String> handleUsernameExists(AlreadyExistException e) {
        return ResponseEntity
                .status(
                        HttpStatus.CONFLICT
                ).body(
                        e.getMessage()
                );
    }

    @ExceptionHandler(WrongEmailOrPasswordException.class)
    public ResponseEntity<String> handleWrongEmailOrPassword(WrongEmailOrPasswordException e) {
        return ResponseEntity
                .status(
                        HttpStatus.CONFLICT
                ).body(
                        e.getMessage()
                );
    }

    @ExceptionHandler(HaveReservationException.class)
    public ResponseEntity<String> HaveReservation(HaveReservationException e) {
        return ResponseEntity
                .status(
                        HttpStatus.CONFLICT
                ).body(
                        e.getMessage()
                );
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(
            IllegalArgumentException e) {

        return ResponseEntity
                .status(
                        HttpStatus.BAD_REQUEST
                ).body(
                        e.getMessage()
                );
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<String> handleForbiddenException(ForbiddenException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ExternalServiceConnectionException.class)
    public ResponseEntity<String> externalServiceConnectionException(ExternalServiceConnectionException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ex.getMessage());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> handleHttpClientError(HttpClientErrorException e) {
        return ResponseEntity
                .status(e.getStatusCode())
                .body(e.getResponseBodyAsString());
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<String> handleResourceAccess(ResourceAccessException e) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(e.getMessage());
    }

    private void printLoggingWarning(ResponseEntity<?> response) {
        logger.warn("""
                GlobalExceptionHandler: handleValidationError
                Status: {}
                Message: {}
                """,response.getStatusCode(), response.getBody()
        );
    }

}