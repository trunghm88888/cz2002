package hrps.exception;

/**
 * When trying to enter a non-existent room type.
 */
public class InvalidRoomTypeException extends HRPSException {
    private static final String INVALID_ROOM_TYPE_MESSAGE = "Invalid room type keyed in";

    public InvalidRoomTypeException() {
        super(INVALID_ROOM_TYPE_MESSAGE);
    }
}
