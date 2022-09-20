package lab.reservation_server.exception;

import lab.reservation_server.dto.response.DefaultDataResponse;
import lab.reservation_server.dto.response.DefaultResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    /**
     * IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<DefaultResponse> handle(IllegalArgumentException ex, WebRequest request) {

        DefaultResponse response = DefaultResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * BadRequestException
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<DefaultResponse> handle(BadRequestException ex) {

        DefaultResponse response = DefaultResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * DuplicateException
     */
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<DefaultResponse> handle(DuplicateException ex) {

        DefaultResponse response = DefaultResponse.of(HttpStatus.CONFLICT, ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * HttpMessageNotReadableException
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<DefaultResponse> handle(HttpMessageNotReadableException ex) {

        DefaultResponse response = DefaultResponse.of(HttpStatus.BAD_REQUEST,
            "잘못된 요청입니다. 문자 혹은 숫자의 형식을 확인해주세요");

        return ResponseEntity.badRequest().body(response);
    }


}