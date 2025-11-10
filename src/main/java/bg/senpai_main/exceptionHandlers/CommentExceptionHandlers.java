package bg.senpai_main.exceptionHandlers;

import bg.senpai_main.exceptions.CommentChangeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class CommentExceptionHandlers {
    @ExceptionHandler(CommentChangeException.class)
    public ResponseEntity<Map<String, Object>> commentChangeExceptionHandler(CommentChangeException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of("success", false, "statusCode", HttpStatus.CONFLICT.value(), "message", ex.getMessage())
        );
    }
}
