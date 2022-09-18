package hrps.exception;

/**
 * When trying to set a check-in time that is after the associated check-out time.
 */
public class InvalidDatePairException extends HRPSException {
    private static final String INVALID_DATE_PAIR_EXCEPTION = "Invalid date pair. Check-in date " +
            "must be before check-out date.";

    public InvalidDatePairException() {
        super(INVALID_DATE_PAIR_EXCEPTION);
    }
}
