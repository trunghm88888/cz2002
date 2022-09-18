package hrps.exception;

/**
 * When trying to check out a non-existing guest.
 */
public class CheckOutGuestContactNotFoundException extends HRPSException {
    private static final String CHECK_OUT_GUEST_CONTACT_NOT_FOUND_EXCEPTION = "Check out guest contact not found exception";

    public CheckOutGuestContactNotFoundException() {
        super(CHECK_OUT_GUEST_CONTACT_NOT_FOUND_EXCEPTION);
    }
}
