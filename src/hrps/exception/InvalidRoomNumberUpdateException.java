package hrps.exception;

/**
 * When trying to update a room number, which by policy is non-updatable.
 */
public class InvalidRoomNumberUpdateException extends HRPSException {
    private static final String INVALID_ROOM_NUMBER_UPDATE_MESSAGE = "Sorry, you are not allowed to update room number for this room!";

    public InvalidRoomNumberUpdateException() {
        super(INVALID_ROOM_NUMBER_UPDATE_MESSAGE);
    }
}