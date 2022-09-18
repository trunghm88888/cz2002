package hrps.entity;

import java.io.Serializable;

/**
 * This subclass represents an order of menu item which consists of the menu item type and the quantity.
 *
 * @author Peng Wenxuan
 */
public class OrderedMenuItem extends MenuItem {
    /**
     * The ordered quantity of this item.
     */
    private final int quantity;

    /**
     * Create an order for a menu item with name, description, price (for each menu item) and quantity of the
     * specified menu item.
     *
     * @param name        Name of the menu item.
     * @param description Description about the menu item.
     * @param price       Price for each of this menu item.
     * @param quantity    Number of this menu item being ordered.
     */
    public OrderedMenuItem(String name, String description, double price, int quantity) {
        super(name, description, price);
        this.quantity = quantity;
    }

    /**
     * Get the ordered quantity of this menu item.
     *
     * @return An int representing the ordered quantity of this menu item.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Get the total price of this order.
     *
     * @return A double representing total price.
     */
    @Override
    public double getPrice() {
        return super.getPrice() * quantity;
    }

    /**
     * Get a String consists of the menu item and its quantity.
     *
     * @return A String representing this order.
     */
    @Override
    public String toString() {
        return super.toString() + String.format("quantity: x%d", quantity);
    }
}
