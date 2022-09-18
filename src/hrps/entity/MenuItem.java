package hrps.entity;

import javax.sql.rowset.serial.SerialArray;
import java.io.Serializable;

/**
 * This class represents menu items that can be ordered by guest. Each menu item has a name, description and price.
 *
 * @author An Ruyi
 */
public class MenuItem implements Serializable {
    /**
     * This item's name, unique for each item.
     */
    private String name;
    /**
     * This item's description.
     */
    private String description;
    /**
     * This item's price per item.
     */
    private double price;

    /**
     * Create a menu item with a name, description and price.
     *
     * @param name        Name of the menu item.
     * @param description Description about the menu item.
     * @param price       Price for each of this menu item.
     */
    public MenuItem(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    /**
     * Get the description of this menu item.
     *
     * @return A string representing description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the price for each of this menu item.
     *
     * @return A double representing price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Get the name of this menu item.
     *
     * @return A String representing name.
     */
    public String getName() {
        return name;
    }

    /**
     * Change the name of this menu item.
     *
     * @param name New name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Change the description of this menu item.
     *
     * @param description New description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Change the price of this menu item.
     *
     * @param price New price.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets a menu item with all information (name, description, price) are exactly same as this
     * menu item. It is used to create a menu item to serve orders of guests.
     *
     * @return A new MenuItem object with exactly same information as this MenuItem.
     */
    public MenuItem clone() {
        return new MenuItem(this.name, this.description, this.price);
    }

    /**
     * Gets a String representing all information about this menu item, including item name,
     * item description, item price in a table format.
     *
     * @return A String with all information of about this menu item.
     */
    @Override
    public String toString() {
        String[][] table = new String[3][];
        table[0] = new String[]{"Item Name", name};
        String seperatedText = "";
        int index = 0;
        while (index < description.length()) {
            if (index == 0) {
                seperatedText += description.substring(index, Math.min(index + 35, description.length()));
                seperatedText += "|\n";
            } else if (index + 35 >= description.length()) {
                seperatedText += String.format("|                     | %-35s", description.substring(index, Math.min(index + 35, description.length())));
            } else {
                seperatedText += String.format("|                     | %-35s|%n", description.substring(index, Math.min(index + 35, description.length())));
            }
            index = index + 35;
        }
        if (description.length() <= 35) {
            seperatedText = description;
        }
        table[1] = new String[]{"Item Description", seperatedText};
        table[2] = new String[]{"Item Price", "$" + String.format("%.2f", price)};
        String separator = "----------------------+-------------------------------------\n";
        StringBuilder display = new StringBuilder(separator);
        for (String[] row : table) {
            display.append(String.format("| %-20s| %-35s|%n", row));
            display.append(separator);
        }
        return display.toString();
    }
}
