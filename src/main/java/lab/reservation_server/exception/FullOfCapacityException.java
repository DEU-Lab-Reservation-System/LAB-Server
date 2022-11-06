package lab.reservation_server.exception;

public class FullOfCapacityException extends RuntimeException {

        public FullOfCapacityException() {
        }

        public FullOfCapacityException(String message) {
        super(message);
        }

        public FullOfCapacityException(String message, Throwable cause) {
        super(message, cause);
        }

        public FullOfCapacityException(Throwable cause) {
        super(cause);
        }


}
