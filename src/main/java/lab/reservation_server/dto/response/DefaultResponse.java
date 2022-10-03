package lab.reservation_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class DefaultResponse {

    private HttpStatus code;
    private String message;

    public static DefaultResponse of(HttpStatus status, String message) {
        return new DefaultResponse(status, message);
    }

}
