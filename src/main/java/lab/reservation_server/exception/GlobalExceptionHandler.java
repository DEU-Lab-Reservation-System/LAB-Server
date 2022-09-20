package lab.reservation_server.exception;

import lab.reservation_server.dto.response.DefaultDataResponse;
import lab.reservation_server.dto.response.DefaultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    /**
     * Validation Check
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DefaultResponse> handle(MethodArgumentNotValidException ex) {

        DefaultResponse response = DefaultResponse.of(HttpStatus.BAD_REQUEST,
            ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());

        return ResponseEntity.badRequest().body(response);
    }


}