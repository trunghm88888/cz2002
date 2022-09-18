package hrps.exception;


/**
 * This exception is thrown when the user input an illegal bed type.
 */
public class IllegalBedTypeException extends HRPSException {
    private static final String ILLEGEAL_BED_TYPE_MESSAGE = "Illegal bed type";

    public IllegalBedTypeException() {
        super(ILLEGEAL_BED_TYPE_MESSAGE);
    }
}
