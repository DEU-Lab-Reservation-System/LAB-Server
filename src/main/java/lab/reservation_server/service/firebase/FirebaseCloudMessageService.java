package lab.reservation_server.service.firebase;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class FirebaseCloudMessageService {
    @Value("${firebase.url}")
    private String API_URL;
    private final ObjectMapper objectMapper;

    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
      String message = makeMessage(targetToken, title, body);

      OkHttpClient client = new OkHttpClient();

      RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));

      Request request = new Request.Builder()
          .url(API_URL)
          .post(requestBody)
          .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
          .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
          .build();

      Response response = client.newCall(request).execute();

      System.out.println(response.body().string());
    }

    private String makeMessage(String targetToken, String title, String body) throws JsonParseException, JsonProcessingException {
      FcmMessage fcmMessage = FcmMessage.builder()
          .message(FcmMessage.Message.builder()
              .token(targetToken) // 사용자 고유의 휴대폰 id
              .notification(FcmMessage.Notification.builder() // inner class builder
                  .title(title)
                  .body(body)
                  .build()
              ).build()).validateOnly(false).build();

      return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {
      String firebaseConfigPath = "firebase/firebase_service_key.json";

      GoogleCredentials googleCredentials = GoogleCredentials
          .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
          .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

      googleCredentials.refreshIfExpired();
      return googleCredentials.getAccessToken().getTokenValue();
    }
}
