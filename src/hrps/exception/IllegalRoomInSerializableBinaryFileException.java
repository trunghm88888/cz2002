package hrps.exception;

/**
 * When trying to remove a non-existent CheckedInReservation.
 */
public class IllegalRoomInSerializableBinaryFileException extends HRPSException {
    public static final String ILLEGAL_ROOM_EXCEPTION = "Room you have entered is not under any " +
            "reservation. Please restore the binary file of reservation and room and try again.";

    public IllegalRoomInSerializableBinaryFileException() {
        super(ILLEGAL_ROOM_EXCEPTION);
    }
}
