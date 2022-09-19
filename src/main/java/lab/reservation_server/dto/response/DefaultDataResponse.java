package lab.reservation_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@Setter
@Builder
public class DefaultDataResponse<T> {

    private HttpStatus status;
    private String message;
    private T data;

    public static<T> DefaultDataResponse<T> of(HttpStatus status, String message, T data) {
        return DefaultDataResponse.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }

}
