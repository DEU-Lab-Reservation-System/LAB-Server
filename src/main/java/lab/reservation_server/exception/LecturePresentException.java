package lab.reservation_server.exception;

public class LecturePresentException extends RuntimeException{

    public LecturePresentException() {
    }

    public LecturePresentException(String message) {
      super(message);
    }

    public LecturePresentException(String message, Throwable cause) {
      super(message, cause);
    }

    public LecturePresentException(Throwable cause) {
      super(cause);
    }

}
