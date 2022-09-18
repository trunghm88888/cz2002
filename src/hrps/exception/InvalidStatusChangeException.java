package hrps.exception;

/**
 * Exception thrown when a status change in reservation is invalid.
 * We only allow logical status changes, but not the other way around.
 */
public class InvalidStatusChangeException extends HRPSException {
    private static final String INVALID_STATUS_CHANGE_MESSAGE = "Invalid reservation status " +
            "change.";

    public InvalidStatusChangeException() {
        super(INVALID_STATUS_CHANGE_MESSAGE);
    }
}