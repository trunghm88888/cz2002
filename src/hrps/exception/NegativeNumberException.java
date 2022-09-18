package hrps.exception;

/**
 * When trying to enter a negative number.
 */
public class NegativeNumberException extends HRPSException {
    private static final String INVALID_NUMBER_MESSAGE = "Number must be a natural number";

    public NegativeNumberException() {
        super(INVALID_NUMBER_MESSAGE);
    }
}

