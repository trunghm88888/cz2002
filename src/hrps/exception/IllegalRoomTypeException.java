package hrps.exception;

/**
 * This exception is thrown when the user input an illegal room type.
 */
public class IllegalRoomTypeException extends HRPSException {
    private static final String ILLEGEAL_ROOM_TYPE_MESSAGE = "Illegal room type";

    public IllegalRoomTypeException() {
        super(ILLEGEAL_ROOM_TYPE_MESSAGE);
    }
}
