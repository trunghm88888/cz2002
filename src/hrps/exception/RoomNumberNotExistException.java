package hrps.exception;

/**
 * When trying to enter a non-existent room number.
 */
public class RoomNumberNotExistException extends HRPSException {
    private static final String ROOM_NUMBER_NOT_EXISTS_MESSAGE = "The room number you have keyed " +
            "in does not exists.";

    public RoomNumberNotExistException() {
        super(ROOM_NUMBER_NOT_EXISTS_MESSAGE);
    }
}
