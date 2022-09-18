package hrps.boundary;

import hrps.control.RoomController;
import hrps.entity.Room;
import hrps.entity.enums.RoomStatus;
import hrps.exception.HRPSException;
import hrps.exception.InvalidRoomTypeException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * This class is used for displaying UI for creating and finding preferred rooms (based on room
 * type, bed type, room facing direction, smoking/non-smoking), getting and updating room details.
 * There are also functions to generate the report for the rooms such as room occupancy report by
 * room type, and display all rooms under each status.
 *
 * @author An Ruyi
 */
public class RoomUI {
    /**
     * The only instance of this class to prevent multiple instantiations.
     */
    private static RoomUI instance;
    /**
     * The scanner used for all methods of this class.
     */
    private final Scanner sc = new Scanner(System.in);
    /**
     * To update rooms' data.
     */
    private final RoomController roomController = RoomController.getInstance();
    /**
     * Initialized false, set to true if all rooms have been initialized.
     */
    private boolean hasBeenInitialized = false;

    /**
     * Constructor to initialize a scanner and get roomController instance.
     */
    private RoomUI() {
    }

    /**
     * Get the UI instance, or create new one if no UI was instantiated.
     * This helps prevent multiple instantiations of UI which is unnecessary.
     *
     * @return The singleton RoomUI instance ready to be used to call methods of RoomUI class.
     */
    public static RoomUI getInstance() {
        if (Objects.isNull(instance)) {
            instance = new RoomUI();
        }
        return instance;
    }

    /**
     * Starts the UI for room-related operations.
     */
    void run() {
        try {
            displayMainRoomUIOptions();
            int choice = Parser.getChoice();
            while (choice != 0) {
                switch (choice) {
                    case 0:
                        break;
                    case 1:
                        createRooms();
                        break;
                    case 2:
                        updateRoomByRoomNumber();
                        break;
                    case 3:
                        checkRoomStatus();
                        break;
                    case 4:
                        checkForRoomDetail();
                        break;
                    case 5:
                        findPreferredRoomUI();
                        break;
                    case 6:
                        maintainRooms();
                        break;
                    case 7:
                        printRoomOccupancyReport();
                        break;
                    case 8:
                        printRoomStatusReport();
                        break;
                    default:
                        System.out.println("Invalid choice. Exiting.");
                        break;

                }
                displayMainRoomUIOptions();
                choice = Parser.getChoice();
            }
        } catch (Exception e) {
            System.out.println("Error occurred. Exiting.");
        }
    }

    /**
     * Prints the rooms under different current status.
     */
    private void printRoomStatusReport() {
        System.out.println("Here is the room status report: ");
        System.out.println(roomController.getRoomStatusRateReport());
    }

    /**
     * Prints the vacancy report for all types of rooms.
     */
    private void printRoomOccupancyReport() {
        System.out.println("Here is the room occupancy rate report: ");
        System.out.println(roomController.getRoomOccupancyReport());
    }

    /**
     * Deals with interaction with user on maintaining rooms and finishing maintenance of rooms.
     */
    private void maintainRooms() {
        System.out.println("0. Back");
        System.out.println("1. Maintain room");
        System.out.println("2. Finish maintaining room");
        int maintenanceChoice = getMaintenanceOption();
        if (maintenanceChoice == 0) {
            return;
        }
        if (maintenanceChoice == 1) {
            System.out.println("Please enter the room number you want to maintain");
            String roomNumber = sc.nextLine();
            Room targetRoom = roomController.maintain(roomNumber);
            if (targetRoom.getRoomStatus().equals(RoomStatus.MAINTENANCE)) {
                System.out.println("Room " + roomNumber + " has been maintained.");
            } else {
                System.out.printf("Room %s is currently %s and cannot be maintained.\n",
                        roomNumber, targetRoom.getRoomStatus());
            }
            return;
        } else if (maintenanceChoice == 2) {
            System.out.println("Please enter the room number you want to finish maintaining");
            String roomNumber = sc.nextLine();
            if (!roomController.isMaintaining(roomNumber)) {
                System.out.println("Room " + roomNumber + " is not being maintained.");
            } else {
                roomController.finishMaintain(roomNumber);
                System.out.println("Room " + roomNumber + " has been finished maintaining.");
            }
            return;
        }
    }

    /**
     * Iteratively asks for a choice until a legal choice for maintenance option is keyed in.
     *
     * @return A legal choice of the user for room maintenance.
     */
    private int getMaintenanceOption() {
        System.out.println("Please enter your choice: ");
        int choice = Parser.getChoice();
        while (choice != 0 && choice != 1 && choice != 2) {
            System.out.println("Invalid choice. Please re-enter your choice: ");
            choice = Parser.getChoice();
        }
        return choice;
    }

    /**
     * Deals with user interaction on finding a list of rooms that are preferred by the user.
     */
    private void findPreferredRoomUI() {
        List<Room> preferredRooms = findRoomBasedOnPreference();
        if (Objects.isNull(preferredRooms)) { // user has cancelled the operation
            return;
        } else if (preferredRooms.isEmpty()) {
            System.out.println("No room found based on your preference.");
            return;
        }
        System.out.println("Rooms found based on your preference: ");
        System.out.println("| Room number | Flat Rate | Wifi Available | Smoking Free |");
        for (Room room : preferredRooms) {
            System.out.printf("|    %s    |  $%s%.2f  |       %s%s      |      %s%s     |\n",
                    room.getRoomNumber(),
                    room.getRate() >= 100 ? "" : " ", room.getRate(), room.isHasWiFi() ? "" : " ",
                    room.isHasWiFi() ? "Yes" : "No", room.isSmokingFree() ? "" : " ",
                    room.isSmokingFree() ? "Yes" : "No");
        }
    }

    /**
     * Deals with user interaction on finding the detail of a specific room.
     */
    private void checkForRoomDetail() {
        int choiceForRoomDetail;
        try {
            do {
                System.out.println("Please enter room number: ");
                String roomNumberStringForDetail = sc.nextLine();
                roomController.isValidRoomNumber(roomNumberStringForDetail);
                Room targetRoomForDetail =
                        roomController.findRoomByRoomNumber(roomNumberStringForDetail);
                if (Objects.isNull(targetRoomForDetail)) {
                    System.out.println("Room not found. Please enter a correct room " +
                            "number:");
                } else {
                    System.out.println("Detail of " + targetRoomForDetail.getRoomNumber() + ":");
                    System.out.println(targetRoomForDetail);
                }
                System.out.println("1. Get details of another room");
                System.out.println("2. Cancel operation");
                choiceForRoomDetail = Parser.getChoice();
            } while (choiceForRoomDetail == 1);
        } catch (HRPSException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Deals with user interaction on finding the status of a room via 3 different methods. 1)
     * Room number 2) Guest name 3) Room type
     */
    private void checkRoomStatus() {
        int choice;
        System.out.println("0. Go back");
        System.out.println("1. Check room status via room number: ");
        System.out.println("2. Check room status via guest contact: ");
        System.out.println("3. Check room status via room type: ");
        System.out.println("Your choice: ");
        choice = Parser.getChoice();
        try {
            switch (choice) {
                case 1:
                    checkRoomStatusByNumber();
                    break;
                case 2:
                    checkRoomStatusByGuestContact();
                    break;
                case 3:
                    checkRoomStatusByRoomType();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid choice. Exiting.");
                    break;
            }
        } catch (HRPSException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Deals with user interaction on finding the status of a room via room number.
     *
     * @throws InvalidRoomTypeException if the room type entered by the user is invalid.
     */
    private void checkRoomStatusByRoomType() throws InvalidRoomTypeException {
        System.out.println("Enter room type: (Single, Double, VIP, Suite or Deluxe)");
        String roomType = sc.nextLine().toUpperCase();
        if (!roomType.equals("SINGLE") && !roomType.equals("DOUBLE")
                && !roomType.equals("VIP") && !roomType.equals("SUITE")
                && !roomType.equals("DELUXE")) {
            throw new InvalidRoomTypeException();
        }
        List<Room> targetRoomByType = roomController.findRoomByType(roomType);
        if (targetRoomByType.isEmpty()) {
            System.out.println("No room found under room type: " + roomType);
        } else {
            System.out.println("Rooms found under room type: " + roomType);
            for (Room room : targetRoomByType) {
                System.out.printf("Room %s is currently %s.\n",
                        room.getRoomNumber(), room.getRoomStatus());
            }
        }
    }

    /**
     * Check if a room is associated to a guest with the entered guest contact.
     */
    private void checkRoomStatusByGuestContact() {
        System.out.println("Enter guest contact: ");
        String guestContact = sc.nextLine();
        List<Room> targetRoomByGuest = roomController.findRoomsByGuest(guestContact);
        if (targetRoomByGuest.isEmpty()) {
            System.out.println("No room found under guest contact: " + guestContact);
        } else {
            System.out.println("Rooms found under guest contact: " + guestContact);
            for (Room room : targetRoomByGuest) {
                System.out.printf("Room %s is currently %s.\n",
                        room.getRoomNumber(), room.getRoomStatus());
            }
        }
    }

    /**
     * Deals with user interaction on finding the status of a room via room number.
     *
     * @throws HRPSException if the room number entered by the user is invalid.
     */
    private void checkRoomStatusByNumber() throws HRPSException {
        System.out.println("Enter room number: ");
        String roomNumberString = sc.nextLine();
        roomController.isValidRoomNumber(roomNumberString);
        Room targetRoom = roomController.findRoomByRoomNumber(roomNumberString);
        if (Objects.isNull(targetRoom)) {
            System.out.println("Room not found. Please enter a correct room " +
                    "number:");
        } else {
            System.out.printf("Room %s is currently %s.\n",
                    roomNumberString, targetRoom.getRoomStatus());
        }
    }

    /**
     * Find a room by room number. If no such room exists, display a error message. If such room exists, display the
     * UI for updating details of that room.
     */
    private void updateRoomByRoomNumber() {
        try {
            while (true) {
                System.out.println("Enter room number: ");
                String roomNumberString = sc.nextLine();
                roomController.isValidRoomNumber(roomNumberString);
                Room targetRoom = roomController.findRoomByRoomNumber(roomNumberString);
                if (Objects.isNull(targetRoom)) {
                    System.out.println("Room not found. Please enter a correct room " +
                            "number:");
                } else {
                    System.out.printf("Room %s has been found.\n", roomNumberString);
                    System.out.println(targetRoom);
                    updateRoom(targetRoom);
                    break;
                }
            }
        } catch (HRPSException e) {
            System.out.println("Warning: " + e.getMessage());
        }
    }

    /**
     * Reads the room information from the text file if the room list has not been loaded.
     * It is not available for the user to manually call this method.
     */
    private void createRooms() {
        try {
            roomController.initializeRooms();
            if (hasBeenInitialized) {
                System.out.println("Rooms have already been initialized.");
            } else {
                System.out.println("All rooms have been initialized.");
                hasBeenInitialized = true;
            }
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }
    }

    /**
     * Displays the UI options on the room management menu.
     */
    private void displayMainRoomUIOptions() {
        System.out.println("-".repeat(30));
        System.out.println("0. Go back to MainUI");
        System.out.println("1. Create all rooms");
        System.out.println("2. Update room details");
        System.out.println("3. Check room status");
        System.out.println("4. Get room details");
        System.out.println("5. Find a room based on preferences");
        System.out.println("6. Room maintenance");
        System.out.println("7. Room occupancy Report");
        System.out.println("8. Room status Report");
        System.out.println("Your choice: ");
    }

    /**
     * Asks the user to iteratively input a room type until a valid room type is entered or 'CANCEL" is entered.
     *
     * @return a legal room type from the user.
     */
    private String getValidRoomTypeInput() {
        System.out.println("Please choose the preferred room type (Single, Double, Deluxe, VIP, " +
                "Suite):");
        String roomType = sc.nextLine().toUpperCase().trim();
        while (!(roomType.equals("SINGLE") || roomType.equals("DOUBLE") || roomType.equals("SUITE") ||
                roomType.equals("DELUXE") || roomType.equals("VIP") || roomType.equals("CANCEL"))) {
            System.out.println("Invalid room type. Please try again. Or type 'CANCEL' to exit.");
            System.out.println("Please choose the preferred room type (Single, Double, Deluxe, VIP, " +
                    "Suite):");
            roomType = sc.nextLine().toUpperCase();
        }
        return roomType;
    }

    /**
     * Asks the user to iteratively input a room type until a valid room bed type is entered or
     * 'CANCEL" is entered.
     *
     * @return a legal room bed type from the user.
     */
    private String getValidRoomBedTypeInput() {
        System.out.println("Please choose the preferred bed type (Single, Double, Master)");
        String roomBedType = sc.nextLine().toUpperCase().trim().replaceAll("\\s+", "");
        while (!(roomBedType.equals("SINGLE") || roomBedType.equals("DOUBLE") ||
                roomBedType.equals("MASTER") || roomBedType.equals("CANCEL"))) {
            System.out.println("Invalid bed type. Please try again. Or type 'CANCEL' to exit.");
            System.out.println("preferred bed type (Single, Double, King)");
            roomBedType = sc.nextLine().toUpperCase().trim().replaceAll("\\s+", "");
        }
        return roomBedType;
    }

    /**
     * Asks the user to iteratively input a room facing until a valid room type is entered or
     * 'CANCEL" is entered.
     *
     * @return a legal room facing from the user.
     */
    private String getValidRoomFacingInput() {
        System.out.println("Please choose the preferred facing (North, South, East, West):");
        String roomFacing = sc.nextLine().toUpperCase().trim();
        while (!(roomFacing.equals("NORTH") || roomFacing.equals("SOUTH") ||
                roomFacing.equals("EAST") || roomFacing.equals("WEST") || roomFacing.equals("CANCEL"))) {
            System.out.println("Invalid room facing. Please try again. Or type 'CANCEL' to exit.");
            System.out.println("Please choose the preferred facing (North, South, East, West):");
            roomFacing = sc.nextLine().toUpperCase();
        }
        return roomFacing;
    }

    /**
     * Find
     * @return A list of room that the users prefers
     */
    List<Room> findRoomBasedOnPreference() {
        // create a candidate list of rooms based on room type
        String roomType = getValidRoomTypeInput();
        if (roomType.equals("CANCEL")) {
            return null;
        }
        List<Room> candidateRoomsByType = roomController.findRoomByType(roomType);
        while (candidateRoomsByType.isEmpty()) {
            System.out.println("No room found under room type: " + roomType);
            System.out.println("Please try with other room type:");
            roomType = getValidRoomTypeInput();
            if (roomType.equals("CANCEL")) {
                return null;
            } else {
                candidateRoomsByType = roomController.findRoomByType(roomType);
            }
        }

        // select bedType from current candidate list
        String bedType = getValidRoomBedTypeInput();
        if (bedType.equals("CANCEL")) {
            return null;
        }
        List<Room> candidateRoomsByBedType =
                roomController.findRoomByBedType(candidateRoomsByType, bedType);
        while (candidateRoomsByBedType.isEmpty()) {
            System.out.println(bedType + " bed type is currently unavailable in " + roomType + " " +
                    "room type.");
            System.out.println("Please enter try with other room bed type:");
            bedType = getValidRoomBedTypeInput();
            if (bedType.equals("CANCEL")) {
                return null;
            }
            candidateRoomsByBedType = roomController.findRoomByBedType(candidateRoomsByType, bedType);
        }

        // select facing from the candidate list
        String roomFacing = getValidRoomFacingInput();
        List<Room> candidateRoomsByFacing = roomController.findRoomByFacing(candidateRoomsByBedType, roomFacing);
        while (candidateRoomsByFacing.isEmpty()) {
            System.out.println(roomFacing + " facing is currently unavailable in " + roomType + " " +
                    "room type with " + bedType + " bed type.");
            System.out.println("Please enter try with other room facing (CANCEL to escape):");
            roomFacing = getValidRoomFacingInput();
            if (roomFacing.equals("CANCEL")) {
                return null;
            }
            candidateRoomsByFacing = roomController.findRoomByFacing(candidateRoomsByBedType, roomFacing);
        }
        return candidateRoomsByFacing;
    }

    /**
     * Check if the specified String is a valid RoomStatus (ignore case), if it is then return it. If the String is
     * not a valid RoomStatus, iteratively ask for user to enter a valid RoomStatus string.
     * @param roomStatus The specified String.
     * @return A valid RoomStatus string
     */
    private String scanForValidRoomStatusInput(String roomStatus) {
        roomStatus = roomStatus.toUpperCase();
        while (!(roomStatus.equals("VACANT") || roomStatus.equals("OCCUPIED") ||
                roomStatus.equals("RESERVED") || roomStatus.equals("MAINTENANCE"))) {
            System.out.println("Invalid choice. Please try again.");
            roomStatus = sc.nextLine();
        }
        return roomStatus;
    }

    /**
     * Iteratively asks for user input until a valid room room rate input is entered
     *
     * @return a valid room rate
     */
    private double scanForValidRoomRateInput() {
        double roomRate = -1;
        while (roomRate < 0) {
            System.out.println("Room rate must be greater or equals to 0. Please try again.");
            roomRate = Parser.getDouble();
        }
        return roomRate;
    }


    /**
     * Display the UI to update the room details for the given room.
     *
     * @param targetRoom the room that is to be updated
     */
    private void updateRoom(Room targetRoom) {
        System.out.println("Please enter the information that you want to update: ");
        System.out.println("1. Room Rate ");
        System.out.println("0. Cancel ");
        int choice = Parser.getChoice();
        System.out.println("Please enter the updated information: ");
        switch (choice) {
            case 1:
                System.out.print("New room rate: ");
                double updatedRoomRate = scanForValidRoomRateInput();
                roomController.updateRoomRate(targetRoom, updatedRoomRate);
                System.out.println("Room rate updated: ");
                System.out.println(targetRoom);
                break;
            case 0:
                break;
            default:
                System.out.println("Invalid choice. Exiting.");
                break;
        }

    }


}
