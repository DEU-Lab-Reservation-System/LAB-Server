package lab.reservation_server.exception;

import javax.validation.ConstraintViolationException;
import lab.reservation_server.dto.response.DefaultMessageResponse;
import lab.reservation_server.dto.response.reservation.CurrentReservation;
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

        DefaultMessageResponse response = DefaultMessageResponse.of("????????? ???????????????. ?????? ?????? ????????? ????????? ??????????????????");

        return ResponseEntity.badRequest().body(response);
    }


    /**
     * request?????? List????????? dto??? ?????? valid?????? ?????? ??????
     * ????????? ??????.
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
    public ResponseEntity<CurrentReservation> handle(LecturePresentException ex) {

        //DefaultResponse response = DefaultResponse.of(HttpStatus.NO_CONTENT, ex.getMessage());
        CurrentReservation currentReservation = new CurrentReservation(true);

        return ResponseEntity.ok().body(currentReservation);
    }

    /**
     * AlreadyBookedException ?????? ????????? ????????? ??????
     */
    @ExceptionHandler(AlreadyBookedException.class)
    public ResponseEntity<DefaultMessageResponse> handle(AlreadyBookedException ex) {

        DefaultMessageResponse response = DefaultMessageResponse.of(ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * FullOfCapacityException ????????? ??? ??? ??????
     */
    @ExceptionHandler(FullOfCapacityException.class)
    public ResponseEntity<DefaultMessageResponse> handle(FullOfCapacityException ex) {

        DefaultMessageResponse response = DefaultMessageResponse.of(ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}