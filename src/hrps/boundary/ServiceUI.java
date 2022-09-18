package hrps.boundary;

import hrps.control.MenuController;
import hrps.control.OrderController;
import hrps.control.RoomController;
import hrps.entity.MenuItem;
import hrps.entity.OrderedMenuItem;
import hrps.entity.OrderedRoomService;
import hrps.entity.Room;
import hrps.entity.enums.OrderStatus;
import hrps.exception.*;

import java.util.*;

/**
 * This class is used for displaying UI for displaying and updating room service menu, making
 * orders as well as checking and updating the status of the orders.
 *
 * @author An Ruyi, Peng Wenxuan
 */
public class ServiceUI {
    /**
     * The only instance of this class to prevent multiple instantiations.
     */
    private static ServiceUI instance = null;
    /**
     * To access and update menu items' data.
     */
    private final MenuController menuController = MenuController.getInstance();
    /**
     * To access and update orders' data.
     */
    private final OrderController orderController = OrderController.getInstance();
    /**
     * To check if a room exists and is valid for room service.
     */
    private final RoomController roomController = RoomController.getInstance();
    /**
     * The scanner used for all methods of this class.
     */
    private static final Scanner in = new Scanner(System.in);

    /**
     * Constructor to initialize a scanner and get all required controllers.
     */
    private ServiceUI() {}

    /**
     * Get the UI instance, or create new one if no UI was instantiated.
     * This helps prevent multiple instantiations of UI which is unnecessary.
     *
     * @return The ServiceUI instance ready to be used to call methods of ServiceUI class.
     */
    public static ServiceUI getInstance() {
        if (Objects.isNull(instance)) {
            instance = new ServiceUI();
        }
        return instance;
    }

    /**
     * Display all options handled by this UI.
     */
    private static void displayOptions() {
        System.out.println("Select your option: ");
        System.out.println("0. Go back");
        System.out.println("1. Display Menu");
        System.out.println("2. Create Menu Item");
        System.out.println("3. Update Menu Item");
        System.out.println("4. Remove Menu Item");
        System.out.println("5. Make an order");
        System.out.println("6. Check or update order status");
    }

    /**
     * Start the displaying and interfacing procedure.
     */
    void run() {
        try {
            displayOptions();
            int option;
            option = Parser.getChoice();

            while (option != 0) {
                switch (option) {
                    case 0:
                        break;
                    case 1:
                        displayMenu();
                        break;
                    case 2:
                        createMenuItem();
                        break;
                    case 3:
                        updateMenuItem();
                        break;
                    case 4:
                        removeMenuItem();
                        break;
                    case 5:
                        makeOrder();
                        break;
                    case 6:
                        checkOrder();
                        break;
                    default:
                        System.out.println("Please select a valid option!");
                        break;
                }
                displayOptions();
                option = Parser.getChoice();
            }
        } catch(Exception e) {
            System.out.println("An error has occurred. Exiting...");
        }
    }

    /**
     * Display all menu items with descriptions and prices.
     */
    private void displayMenu() {
        System.out.println(menuController.getMenu());
    }

    /**
     * Display the UI to read a room number from user, if no such room exists or such room is not valid for room
     * service, display a message and return. If such room is valid for room service, display all the orders made to
     * that room and display the UI for user to update status of any order in those orders.
     */
    private void checkOrder() {
        String roomNum;
        try {
            System.out.print("Enter room number to print order: ");
            roomNum = in.nextLine().trim();
            System.out.println("Order Details:");
            // check validity of room number
            roomController.isValidRoomNumber(roomNum);
            if (roomController.canBeOrdered(roomNum)) {
                Room room = roomController.findRoomByRoomNumber(roomNum);
                List<OrderedRoomService> orders = orderController.getRoomOrder(room);
                int counter = 0;
                for (OrderedRoomService order : orders) {
                    System.out.printf("%d, ", ++counter);
                    System.out.println(order);
                }
                System.out.println("Would you like to update any order's status? (Y/N)");
                String choice = in.nextLine().trim();
                if (choice.equalsIgnoreCase("Y")) {
                    int orderNum = getLegalOrderNumber(counter);
                    OrderedRoomService order = orders.get(orderNum - 1);

                    OrderStatus status = getLegalOrderStatus();
                    orderController.changeOrderStatus(order, status);
                    System.out.println("Order status is updated to " + status);
                    return;
                } else {
                    return;
                }
            } else if (!Objects.isNull(roomController.findRoomByRoomNumber(roomNum))) {
                System.out.println("This room has no guest and cannot order service now!");
            } else {
                System.out.println("Invalid room number!");
            }
        } catch (HRPSException e) {
            System.out.println("Warning: " + e.getMessage());
        }
    }

    /**
     * Iteratively ask user to enter a valid new status of order as an integer choice from a list of status.
     * @return The OrderStatus parsed from user's input.
     */
    private OrderStatus getLegalOrderStatus() {
        int status;
        System.out.println("Enter new status: (0 - confirmed, 1 - preparing, 2 - " +
                "delivered)");
        status = Parser.getChoice();
        while (status < 0 || status > 2) {
            System.out.println("Please enter a valid status!");
            status = Parser.getChoice();
            System.out.println("Enter new status: (0 - confirmed, 1 - preparing, 2 - " +
                    "delivered)");
        }
        return OrderStatus.values()[status];
    }

    /**
     * Iteratively ask user to choose a valid order number.
     * @param maxCount The total number of orders.
     * @return The order number parsed from user's input.
     */
    private int getLegalOrderNumber(int maxCount) {
        int orderNum;
        System.out.print("Select the order number from the above order list: ");
        orderNum = Parser.getChoice();
        while (orderNum < 1 || orderNum > maxCount) {
            System.out.println("Please enter a valid order number!");
            orderNum = Parser.getChoice();
        }
        return orderNum;
    }

    /**
     * Display the UI to make an order. If the menu is empty, display a message and return. Order can only be made if
     * a room with the entered room number exists and is OCCUPIED. If the room is valid for making order, display the
     * menu and display the UI for choosing menu items, each with a quantity, and finally the order's remarks. If
     * user does not choose any items, no order is created and a message is displayed. If an order is created,
     * display a successful message.
     */
    private void makeOrder() {
        if (menuController.hasEmptyMenu()) {
            System.out.println("Menu is empty! Please create menu items first before making " +
                    "orders");
            return;
        }
        String roomNum;
        try {
            System.out.print("Enter room number to make order: ");
            roomNum = in.nextLine().trim();
            roomController.isValidRoomNumber(roomNum);
            if (roomController.canBeOrdered(roomNum)) {   // check room number exist and room occupied
                displayMenu();
                String itemName, remarks;
                List<OrderedMenuItem> items = new LinkedList<>();
                System.out.println("Please select the items to order... (enter \"STOP\" to terminate)");
                System.out.print("Item: ");
                itemName = in.nextLine().trim();
                /*
                if (itemName.equalsIgnoreCase("STOP")) {
                    System.out.println("Order cancelled!");
                    return;
                }*/
                while (!itemName.equalsIgnoreCase("STOP")) {
                    if (menuController.existMenuItem(itemName)) {
                        MenuItem menuItem = menuController.getMenuItem(itemName);
                        int quantity = getValidQuantity();
                        OrderedMenuItem orderedMenuItem = new OrderedMenuItem(itemName, menuItem.getDescription(), menuItem.getPrice(), quantity);
                        items.add(orderedMenuItem);
                        System.out.println("Item added to order.");
                    } else {
                        System.out.println("Item named \"" + itemName + "\" does not exist!");
                    }
                    System.out.println("Please select the items to order... (enter \"STOP\" to " +
                            "terminate)");
                    System.out.print("Item: ");
                    itemName = in.nextLine().trim();
                }
                if (!items.isEmpty()) {
                    System.out.print("Enter the remarks for this order: ");
                    remarks = in.nextLine().trim();
                    orderController.makeOrder(roomController.findRoomByRoomNumber(roomNum), remarks, items);
                    System.out.println("Order made!\n");
                } else {
                    System.out.println("An empty order is made. As a result, this order is " +
                            "forfeited.\n");
                }
            } else if (!Objects.isNull(roomController.findRoomByRoomNumber(roomNum))) {
                System.out.println("This room has no guest and cannot order service now!");
            } else {
                System.out.println("Invalid room number!");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! You should key in a number of quantity!");
            System.out.println("Order failed.");
        } catch (HRPSException e) {
            System.out.println("Warning: " + e.getMessage());
        }
    }

    /**
     * Iteratively ask user to enter a valid quantity of a menu item, which is defined as a positive integer.
     * @return The quantity of a menu item, which is a positive integer.
     */
    private int getValidQuantity() {
        int quantity;
        System.out.print("Quantity: ");    // ask user to input quantity for a menu item
        quantity = Parser.getInt();
        while (quantity <= 0) {
            System.out.println("Invalid quantity! Please enter a valid quantity!");
            System.out.print("Quantity: ");
            quantity = Parser.getInt();
        }
        return quantity;
    }

    /**
     * Remove an item from the menu if such item with the entered name exists, otherwise display an error message.
     */
    private void removeMenuItem() {
        try {
            String name;
            System.out.print("Enter the item name to remove:");
            name = in.nextLine().trim();
            menuController.removeMenuItem(name);
            System.out.println("Item removed!");
        } catch (HRPSException e) {
            System.out.println("Warning: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Display the UI for user to enter a item name to update its details, including name, description and price. New
     * name is a String and cannot not be the same as another item's name in the menu, price must be a non-negative
     * double.
     */
    private void updateMenuItem() {
        try {
            String oldName, newName = null;
            String newDescription = null;
            double newPrice = -2;
            System.out.print("Enter the item name to update: ");
            oldName = in.nextLine().trim();
            if (menuController.existMenuItem(oldName)) {
                boolean hasUpdateDName = false;
                boolean hasUpdatedDescription = false;
                boolean hasUpdatedPrice = false;
                System.out.print("Update item name? (y/n) ");
                String choice = in.nextLine().trim();
                if (choice.charAt(0) == 'y' || choice.charAt(0) == 'Y') {
                    hasUpdateDName = true;
                    System.out.println("Enter updated name: ");
                    newName = in.nextLine().trim();
                    if (newName.trim().equals("")) {
                        throw new EmptyMenuNameException();
                    }
                } else if (choice.charAt(0) == 'n' || choice.charAt(0) == 'N') {
                    newName = oldName;
                    hasUpdateDName = false;
                } else {
                    throw new InvalidInputChoiceException();
                }
                System.out.print("Update item description? (y/n) ");
                choice = in.nextLine().trim();
                if (choice.charAt(0) == 'y' || choice.charAt(0) == 'Y') {
                    hasUpdatedDescription = true;
                    System.out.print("Enter updated description: ");
                    newDescription = in.nextLine().trim();
                    if (newDescription.trim().equals("")) {
                        throw new EmptyMenuItemDescriptionException();
                    }
                } else if (choice.charAt(0) == 'n' || choice.charAt(0) == 'N') {
                    newDescription = menuController.searchMenuItem(oldName).getDescription();
                    hasUpdatedDescription = false;
                } else {
                    throw new InvalidInputChoiceException();
                }
                System.out.print("Update item price? (y/n) ");
                choice = in.nextLine().trim();
                if (choice.charAt(0) == 'y' || choice.charAt(0) == 'Y') {
                    hasUpdatedPrice = true;
                    System.out.print("Enter updated price: ");
                    newPrice = Parser.getDouble();
                    while(newPrice < 0) {
                        System.out.println("Invalid price! Please enter a price greater than 0!");
                        System.out.print("Enter updated price: ");
                        newPrice = Parser.getDouble();
                    }
                } else if (choice.charAt(0) == 'n' || choice.charAt(0) == 'N') {
                    newPrice = menuController.searchMenuItem(oldName).getPrice();
                    hasUpdatedPrice = false;
                } else {
                    throw new InvalidInputChoiceException();
                }
                if (!(hasUpdateDName || hasUpdatedDescription || hasUpdatedPrice)) {
                    System.out.println("No update is done.Exiting");
                    return;
                }
                menuController.updateMenuItem(oldName, newName, newDescription, newPrice);
                System.out.println("Item information updated!\n");
            } else {
                System.out.println("Item not in menu!\n");
            }
        } catch (InputMismatchException e) {
            System.out.println("Warning: Invalid input! You should key in a number for price!");
        } catch (HRPSException e) {
            System.out.println("Warning: " + e.getMessage());
        }
    }

    /**
     * Display the UI for user to enter name (cannot be duplicated), description and price (cannot be negative) for a
     * new menu item and create that item.
     */
    private void createMenuItem() {
        try {
            System.out.print("Enter the item name: ");
            String name = in.nextLine().trim();
            if (!menuController.existMenuItem(name)) {
                if (name.trim().equals("")) {
                    throw new EmptyMenuNameException();
                }
                System.out.print("Enter the item description: ");
                String description = in.nextLine().trim();
                System.out.print("Enter the item price: ");
                double price = Parser.getDouble();
                while(price < 0) {
                    System.out.println("Invalid price! Please enter a price greater than 0!");
                    System.out.print("Enter the item price: ");
                    price = Parser.getDouble();
                }
                menuController.createMenuItem(name, description, price);
                System.out.println("Item created!");
            } else {
                System.out.println("Item already exists in menu!");
            }
        } catch (InputMismatchException e) {
            System.out.println("Warning: Invalid input!");
        } catch (HRPSException e) {
            System.out.println("Warning: " + e.getMessage());
        }
        System.out.println();
    }
}
