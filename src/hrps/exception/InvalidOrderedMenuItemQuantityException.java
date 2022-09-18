package hrps.exception;

/**
 * When trying to enter a non-positive menu item quantity.
 */
public class InvalidOrderedMenuItemQuantityException extends HRPSException {
    private static final String NON_POSITIVE_QUANTITY_MESSAGE = "Order menu item quantity must be positive";

    public InvalidOrderedMenuItemQuantityException() {
        super(NON_POSITIVE_QUANTITY_MESSAGE);
    }
}