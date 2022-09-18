package hrps.exception;

/**
 * The super class of all the exceptions defined by HRPS program.
 * An error-specific message will be in each subclass of HRPSException, and would be used during
 * instantiation
 * and printing out the error message.
 *
 * @author An Ruyi
 */
public abstract class HRPSException extends Exception {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public HRPSException(String message) {
        super(message);
    }
}
