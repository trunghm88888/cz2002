package hrps.exception;

/**
 * When trying to enter a room number not in format xx-xx, where x is a decimal digit.
 */
public class InvalidRoomNumberFormatException extends HRPSException {
    private static final String INVALID_ROOM_NUMBER_FORMAT_MESSAGE = "Invalid room number format, please enter xx-xx, eg. 02-01";

    public InvalidRoomNumberFormatException() {
        super(INVALID_ROOM_NUMBER_FORMAT_MESSAGE);
    }
}
