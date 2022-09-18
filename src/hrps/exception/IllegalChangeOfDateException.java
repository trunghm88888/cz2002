package hrps.exception;

/**
 * When trying to change a check-in/check-out date that cannot be changed (eg. change actual checked-in/checked-out date
 * of CheckedInReservation/CheckedOutReservation).
 */
public class IllegalChangeOfDateException extends HRPSException {
    private static final String INVALID_STATUS_CHANGE_MESSAGE = "Illegal change of date at " +
            "current reservation status";

    public IllegalChangeOfDateException() {
        super(INVALID_STATUS_CHANGE_MESSAGE);
    }
}
