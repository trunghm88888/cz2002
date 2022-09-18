package hrps.exception;

/**
 * When trying to check out a guest whose name does not exist in system.
 */
public class CheckOutGuestNameNotFoundException extends HRPSException {
    private static final String CHECK_OUT_GUEST_NAME_NOT_FOUND_EXCEPTION_MESSAGE = "Check out " +
            "guest name you keyed in not found";

    public CheckOutGuestNameNotFoundException() {
        super(CHECK_OUT_GUEST_NAME_NOT_FOUND_EXCEPTION_MESSAGE);
    }
}
