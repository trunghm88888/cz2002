package hrps.control;

import hrps.entity.Menu;
import hrps.entity.MenuItem;
import hrps.exception.DuplicateMenuItemException;
import hrps.exception.HRPSException;
import hrps.exception.MenuItemNotExistException;
import tool.SerializeDB;

import java.io.*;
import java.util.Objects;

/**
 * This class represent controller that contains all methods related to menu, includes creating new menu items,
 * searching, updating and removing existed menu items, and also update the menu and the menu items on the database.
 *
 * @author Peng Wenxuan
 */
public class MenuController {
    /**
     * Menu data file's path.
     */
    private static final String MENU_DATA_DIR = "src/data/menu.dat";
    /**
     * The menu that is used by MenuController instance.
     */
    private Menu menu;
    /**
     * The static controller instance to avoid multiple instantiations.
     */
    private static MenuController MenuController = null;

    /**
     * Get the controller instance. This is to avoid multiple instantiation of controller.
     *
     * @return The singleton MenuController instance.
     */
    public static MenuController getInstance() {
        if (MenuController == null) {
            MenuController = new MenuController();
        }
        return MenuController;
    }


    /**
     * Initialize MenuController by reading from menu data file, if the data file does not exist at the given path,
     * create it and write the on-memory menu data to the newly created file.
     */
    private MenuController() {
        File file = new File(MENU_DATA_DIR);
        if (file.exists()) {
            menu = (Menu) SerializeDB.readSerializedObject(MENU_DATA_DIR);
        } else {
            file.getParentFile().mkdir();
            menu = new Menu();
            SerializeDB.writeSerializedObject(MENU_DATA_DIR, menu);
        }
    }

    /**
     * Get a clone of the menu item with this unique name.
     *
     * @param name The name to be searched.
     * @return A new MenuItem object with the specified name, null if no menu item with this name.
     */
    public MenuItem getMenuItem(String name) {
        return menu.getItem(name, true);
    }

    /**
     * Get the menu item with this unique name.
     *
     * @param name The name to be searched.
     * @return A MenuItem object from the mnu with the specified name, null if no menu item with this name.
     */
    public MenuItem searchMenuItem(String name) {
        return menu.getItem(name, false);
    }

    /**
     * Check if there exists a menu item with this name in the menu.
     *
     * @param name The name to be checked.
     * @return true if a menu item with this name exists, false otherwise.
     */
    public boolean existMenuItem(String name) {
        return searchMenuItem(name) != null;
    }

    /**
     * Create a new menu item with name, description, price and add it to the menu and update the database.
     *
     * @param name        Name of new menu item.
     * @param description Description of new menu item.
     * @param price       Price of new menu item.
     * @throws DuplicateMenuItemException When there exists a menu item with exactly the same name in the menu.
     */
    public void createMenuItem(String name, String description, double price) throws HRPSException {
        if (Objects.isNull(searchMenuItem(name))) {
            MenuItem item = new MenuItem(name, description, price);
            menu.addItem(item);
            SerializeDB.writeSerializedObject(MENU_DATA_DIR, menu);
        } else {
            throw new DuplicateMenuItemException();
        }
    }

    /**
     * Remove the menu item with the specified name from the menu and update the database.
     *
     * @param name Name of the menu item to be removed.
     * @throws HRPSException When there exists no menu item with the specified name in the menu.
     */
    public void removeMenuItem(String name) throws HRPSException {
        MenuItem target;
        if (!Objects.isNull((target = searchMenuItem(name)))) {
            if (menu.removeItem(target)) {
                SerializeDB.writeSerializedObject(MENU_DATA_DIR, menu);
            }
        } else {
            throw new MenuItemNotExistException();
        }
    }

    /**
     * Update a menu item's information and update the database.
     *
     * @param oldName        Name of the menu item to be updated.
     * @param newName        New name.
     * @param newDescription New description.
     * @param newPrice       New price.
     * @throws HRPSException When a menu item with the new name is already existed in the menu.
     */
    public void updateMenuItem(String oldName, String newName, String newDescription,
                               double newPrice) throws HRPSException {
        MenuItem target;
        if (!Objects.isNull((target = searchMenuItem(oldName)))) {
            target.setName(newName);
            target.setDescription(newDescription);
            target.setPrice(newPrice);
            SerializeDB.writeSerializedObject(MENU_DATA_DIR, menu);
        }
    }

    /**
     * Check if the menu used by this MenuController is empty or not.
     * @return true if the menu is empty, false otherwise.
     */
    public boolean hasEmptyMenu() {
        return menu.isEmpty();
    }

    /**
     * Get the menu used by this MenuController.
     *
     * @return The menu.
     */
    public Menu getMenu() {
        return menu;
    }
}
