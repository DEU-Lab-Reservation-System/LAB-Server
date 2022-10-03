package lab.reservation_server.exception;

import javax.validation.ConstraintViolationException;
import lab.reservation_server.dto.response.DefaultMessageResponse;
import lab.reservation_server.dto.response.DefaultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;


@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    /**
     * Validation Check
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DefaultMessageResponse> handle(MethodArgumentNotValidException ex) {

        DefaultMessageResponse response = DefaultMessageResponse.of(
            ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<DefaultMessageResponse> handle(IllegalArgumentException ex, WebRequest request) {

        DefaultMessageResponse response = DefaultMessageResponse.of(ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * BadRequestException
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<DefaultMessageResponse> handle(BadRequestException ex) {

        DefaultMessageResponse response = DefaultMessageResponse.of(ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * DuplicateException
     */
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<DefaultMessageResponse> handle(DuplicateException ex) {

        DefaultMessageResponse response = DefaultMessageResponse.of(ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * HttpMessageNotReadableException
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<DefaultMessageResponse> handle(HttpMessageNotReadableException ex) {

        DefaultMessageResponse response = DefaultMessageResponse.of("잘못된 요청입니다. 문자 혹은 숫자의 형식을 확인해주세요");

        return ResponseEntity.badRequest().body(response);
    }


    /**
     * request에서 List형태로 dto를 받고 valid에서 걸릴 경우
     * 여기로 온다.
     * ConstraintViolationException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<DefaultMessageResponse> handle(ConstraintViolationException ex) {

        DefaultMessageResponse response = DefaultMessageResponse.of(ex.getConstraintViolations().iterator().next().getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * LecturePresentException
     */
    @ExceptionHandler(LecturePresentException.class)
    public ResponseEntity<DefaultResponse> handle(LecturePresentException ex) {

        DefaultResponse response = DefaultResponse.of(HttpStatus.NO_CONTENT, ex.getMessage());

        return ResponseEntity.ok(response);
    }

}