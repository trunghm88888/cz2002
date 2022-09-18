package hrps.exception;

/**
 * When trying to create a menu item with empty description.
 */
public class EmptyMenuItemDescriptionException extends HRPSException {
    private static final String EMPTY_MENU_DESCRIPTION_MESSAGE = "Description cannot be blank";

    public EmptyMenuItemDescriptionException() {
        super(EMPTY_MENU_DESCRIPTION_MESSAGE);
    }
}
