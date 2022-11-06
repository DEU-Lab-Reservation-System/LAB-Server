package lab.reservation_server.exception;

public class AlreadyBookedException extends RuntimeException{

        public AlreadyBookedException() {
        }

        public AlreadyBookedException(String message) {
        super(message);
        }

        public AlreadyBookedException(String message, Throwable cause) {
        super(message, cause);
        }

        public AlreadyBookedException(Throwable cause) {
        super(cause);
        }

}
