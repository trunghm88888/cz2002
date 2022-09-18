package hrps.exception;

/**
 * When trying to create a menu item with empty name.
 */
public class EmptyMenuNameException extends HRPSException {
    private static final String EMPTY_MENU_NAME_MESSAGE = "Name cannot be blank";

    public EmptyMenuNameException() {
        super(EMPTY_MENU_NAME_MESSAGE);
    }
}