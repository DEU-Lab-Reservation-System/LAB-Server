package lab.reservation_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class DefaultMessageResponse {

    private String message;

    public static DefaultMessageResponse of(String message) {
      return new DefaultMessageResponse(message);
    }
}
