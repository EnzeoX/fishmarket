package technikal.task.fishmarket.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Nikolay Boyko
 */

@Slf4j
@ControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleException(Exception e) {
        String exceptionName = e.getClass().getSimpleName();
        log.error("{}, {}", exceptionName, e.getMessage());
        // i'm using it like this because i'm on old version of Intelij Idea which doesn't support pattern matching
        switch (exceptionName) {
            case "StringIndexOutOfBoundsException":
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Provided incorrect string");
            case "NullPointerException":
            case "DataFormatException":
            case "IllegalArgumentException":
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            case "HttpMessageNotReadableException":
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Required request body is missing");
            case "AuthenticationException":
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
            case "DataIntegrityViolationException":
                ConstraintViolationException throwable = (ConstraintViolationException) e.getCause();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(throwable.getCause().getMessage());
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
