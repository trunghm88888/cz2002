package hrps.boundary;

import hrps.control.RoomController;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * This class is the main UI used for interacting with hotel managers through text command line. It contains
 * operations: Guest related operations, Reservation related operations, Room related operations,
 * Service related operations, Checkout and Payments operations.
 *
 * @author An Ruyi
 */
public class UI {
    /**
     * The only instance of this class to prevent multiple instantiations.
     */
    private static UI instance = null;

    /**
     * The relative path to rooms' data file.
     */
    private static final String DIR_OF_ROOM_LIST = "src/data/room_list.dat";

    /**
     * Dummy constructor.
     */
    private UI() {}

    /**
     * Get the UI instance, or create new one if no UI was instantiated.
     * This helps prevent multiple instantiations of UI which is unnecessary.
     *
     * @return The singleton UI instance ready to be used to call methods of UI class.
     */
    public static UI getInstance() {
        if (Objects.isNull(instance)) {
            instance = new UI();
        }
        return instance;
    }

    /**
     * Start the displaying and interfacing procedure.
     */
    public void run() {
        intializeRoom();
        // System.out.print("All rooms successfully created.\n");
        greet();
        displayOptions();
        int choice = Parser.getChoice();
        mainLoop:
        while (true) {
            try {
                switch (choice) {
                    case 0:
                        System.out.println("Exiting HRPS...Bye and hope to see you again!");
                        break mainLoop;
                    case 1:
                        GuestUI guestUI = GuestUI.getInstance();
                        guestUI.run();
                        break;
                    case 2:
                        ReservationUI reservationUI = ReservationUI.getInstance();
                        reservationUI.run();
                        break;
                    case 3:
                        RoomUI roomUI = RoomUI.getInstance();
                        roomUI.run();
                        break;
                    case 4:
                        ServiceUI serviceUI = ServiceUI.getInstance();
                        serviceUI.run();
                        break;
                    case 5:
                        CheckOutUI checkOutUI = CheckOutUI.getInstance();
                        checkOutUI.run();
                        break;
                    default:
                        System.out.println("Invalid choice!");
                        break;
                }
            } catch (Exception e) {
                System.out.printf("An error occurred. Exiting to main menu...%nPlease try again" +
                        ".%n");
                throw e;
            }
            displayOptions();
            choice = Parser.getChoice();
        }
    }

    /**
     * Initialize the room list. If the Serializable file is not found in the data folder, we will
     * read from the txt file that contains initial room list. Otherwise, read from the Serializable binary file.
     */
    private void intializeRoom() {
        File tempRoomList = new File(DIR_OF_ROOM_LIST);
        if (!tempRoomList.exists()) {
            try {
                RoomController roomController = RoomController.getInstance();
                roomController.initializeRooms();
            } catch (IOException e) {
                System.out.println("Initialzation of room fails. Please check if you have " +
                        "RoomData.txt under src/entity folder.");
            }
        }
    }

    /**
     * Display all options handled by this UI.
     */
    private void displayOptions() {
        System.out.println("-".repeat(30));
        System.out.println("0. Exit");
        System.out.println("1. Guest Page");
        System.out.println("2. Reservation Page");
        System.out.println("3. Room Page");
        System.out.println("4. Service Page");
        System.out.println("5. Check out Page");
        System.out.println("Your choice: ");
    }

    /**
     * Launches the greeting and welcoming message. It should only be called once per session.
     */
    private void greet() {
        System.out.print("  ___ ___   __________  __________    _________\n" +
                " /   |   \\  \\______   \\ \\______   \\  /   _____/\n" +
                "/    ~    \\  |       _/  |     ___/  \\_____  \\ \n" +
                "\\    Y    /  |    |   \\  |    |      /        \\\n" +
                " \\___|_  /   |____|_  /  |____|     /_______  /\n" +
                "       \\/           \\/                      \\/\n");
        System.out.println("Welcome to HRPS!");
    }
}
