package hrps.exception;

/**
 * This exception is thrown when the user enters an invalid input choice.
 */
public class InvalidInputChoiceException extends HRPSException {
    private static final String INVALID_INPUT_CHOICE = "Invalid input choice.";

    public InvalidInputChoiceException() {
        super(INVALID_INPUT_CHOICE);
    }
}
