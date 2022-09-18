package hrps.exception;

/**
 * When trying to create a menu item whose name already exists in the menu.
 */
public class DuplicateMenuItemException extends HRPSException {
    private static final String DUPLICATE_MENU_ITEM_MESSAGE = "Duplicate menu item names";

    public DuplicateMenuItemException() {
        super(DUPLICATE_MENU_ITEM_MESSAGE);
    }
}
