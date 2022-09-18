package hrps.exception;

/**
 * When trying to enter a negative menu item price.
 */
public class InvalidPriceException extends HRPSException {
    private static final String NEGATIVE_PRICE_MESSAGE = "Price cannot be negative";

    public InvalidPriceException() {
        super(NEGATIVE_PRICE_MESSAGE);
    }
}