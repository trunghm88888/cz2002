package hrps.exception;

/**
 * When trying to set a check-out time that is before the associated check-in time.
 */
public class InvalidCheckOutTimeException extends HRPSException {
    private static final String INVALID_CHECK_OUT_TIME_MESSAGE = "Invalid Check Out Time. The " +
            "check out time cannot be before the check in time.";

    public InvalidCheckOutTimeException() {
        super(INVALID_CHECK_OUT_TIME_MESSAGE);
    }
}
