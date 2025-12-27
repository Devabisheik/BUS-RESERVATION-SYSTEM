package BUS_RESERVATION_SYSTEM;

public class PassengerDetailsValidation extends RuntimeException {
    PassengerDetailsValidation(String message) {
        super(message);
    }
}
