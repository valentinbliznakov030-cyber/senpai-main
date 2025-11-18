package bg.senpai_main.exceptionHandlers;

import bg.senpai_main.exceptions.EntityAlreadyExistException;
import bg.senpai_main.exceptions.EntityNotFoundException;
import feign.Feign;
import feign.FeignException;
import feign.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, Object>> handleFeignException(FeignException e) {
        HttpStatus status = HttpStatus.resolve(e.status());
        if (status == null) status = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity.status(status).body(Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", status.value(),
                "error", "Microservice error",
                "message", e.contentUTF8() != null ? e.contentUTF8() : e.getMessage()
        ));
    }

    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<?> handleFeignNotFoundException(FeignException.NotFound ex){
        return ResponseEntity.status(404).body(Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status",404,
                "message", ex.contentUTF8() != null ? ex.contentUTF8() : ex.getMessage()
        ));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(EntityNotFoundException ex){
        return ResponseEntity.status(404).body(
                Map.of(
                        "success", false,
                        "statusCode", 404,
                        "message", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(EntityAlreadyExistException.class)
    public ResponseEntity<Map<String, Object>> handleEntityAlreadyExistException(EntityAlreadyExistException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of("success", false,
                        "statusCode", 409,
                        "message", ex.getMessage()
                )
        );
    }
}
