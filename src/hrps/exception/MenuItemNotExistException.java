package hrps.exception;

/**
 * When trying to enter a non-existent menu item's name.
 */
public class MenuItemNotExistException extends HRPSException {
    private static final String MENU_ITEM_NOT_EXIST_EXCEPTION = "Menu item not exist";

    public MenuItemNotExistException() {
        super(MENU_ITEM_NOT_EXIST_EXCEPTION);
    }
}
