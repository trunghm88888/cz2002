package hrps.entity;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents menus which can be used to add, remove and choose service items.
 *
 * @author An Ruyi
 */
public class Menu implements Serializable {
    /**
     * This menu's list of items.
     */
    private final List<MenuItem> menuItems;
    //private static final long serialVersionUID = 3L;

    /**
     * Create an empty menu.
     */
    public Menu() {
        menuItems = new java.util.ArrayList<>();
    }

    /**
     * Add a menu item to this menu.
     *
     * @param item MenuItem object to be added.
     */
    public void addItem(MenuItem item) {
        menuItems.add(item);
    }

    /**
     * Return the first menu item with name same as a specific string (not case-ignored), return a clone of the menu
     * item if isClone is set to true.
     *
     * @param name    Specific String to search for menu item.
     * @param isClone Whether to clone the found menu item.
     * @return A MenuItem object.
     */
    public MenuItem getItem(String name, boolean isClone) {
        for (MenuItem item : menuItems) {
            if (item.getName().equalsIgnoreCase(name)) {
                if (isClone) {
                    return item.clone();
                }
                else {
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * Remove a menu item from this menu.
     *
     * @param item The MenuItem object to be removed.
     * @return true if this menu contains the specified MenuItem object.
     */
    public boolean removeItem(MenuItem item) {
        return menuItems.remove(item);
    }

    /**
     * Get a string that represents all menu items of this menu, each separated from the others by a line breaker.
     *
     * @return A String that represents all menu items of this menu.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        for (MenuItem item : menuItems) {
            sb.append(item.toString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Check if this menu is empty (aka contains no item).
     * @return true if this menu is empty, false otherwise.
     */
    public boolean isEmpty() {
        return menuItems.isEmpty();
    }
}
