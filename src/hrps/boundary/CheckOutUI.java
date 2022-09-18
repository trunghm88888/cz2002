package hrps.boundary;

import hrps.control.*;
import hrps.entity.*;
import hrps.exception.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * This class is used for displaying the UI when checking out and making payments.
 *
 * @author An Ruyi
 */
public class CheckOutUI {
    /**
     * The only one instance, this is to avoid multiple instantiations.
     */
    private static CheckOutUI instance = null;
    /**
     * The scanner used for all methods.
     */
    private final Scanner sc = new Scanner(System.in);
    /**
     * To update guests' data.
     */
    private final GuestController guestController = GuestController.getInstance();
    /**
     * To update rooms' data.
     */
    private final RoomController roomController = RoomController.getInstance();
    /**
     * To generate payments and bills.
     */
    private final CheckOutController checkOutController = CheckOutController.getInstance();
    /**
     * To update reservations' data.
     */
    private final ReservationController reservationController = ReservationController.getInstance();

    /**
     * Constructor to initialize a scanner and call all the required controllers.
     */
    private CheckOutUI() {
    }

    /**
     * Gets the UI instance, or create new one if no UI was instantiated.
     * This helps prevent multiple instantiations of UI which is unnecessary.
     *
     * @return The singleton CheckOutUI instance ready to be used to call methods of CheckOutUI
     * class.
     */
    public static CheckOutUI getInstance() {
        if (Objects.isNull(instance)) {
            instance = new CheckOutUI();
        }
        return instance;
    }

    /**
     * Starts the displaying and interfacing procedure.
     */
    void run() {
        try {
            displayCheckOutUIMainMenu();
            int choice = Parser.getChoice();
            while (choice != 0) {
                switch (choice) {
                    case 0:
                        break;
                    case 1:
                        checkOutAReservationUI(1);
                        break;
                    case 2:
                        checkOutAReservationUI(2);
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        break;
                }
                displayCheckOutUIMainMenu();
                choice = Parser.getChoice();
            }
        } catch (InputMismatchException e) {
            System.out.println("Warning: Invalid input!");
        } catch (HRPSException e) {
            System.out.println("Warning: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error has occurred. Please try again.");
        }
    }

    /**
     * Displays the UI for checking out a reservation and making payment.
     *
     * @param choice The choice of how user wants to find a reservation. 1 stands for by guest
     *               name, 2 stands for by guest contact number. If no valid choice is given, the
     *               method will exit.
     * @throws InvalidDateTimeFormatException If the date and time format is invalid.
     */
    private void checkOutAReservationUI(int choice) throws InvalidDateTimeFormatException {
        OrderController orderController = OrderController.getInstance();
        Guest targetGuest;
        if (choice == 1) {
            System.out.println("Enter guest name for check-out service: ");
            String guestName = sc.nextLine().trim();
            List<Guest> targetGuests = guestController.searchGuestByName(guestName);
            if (targetGuests.isEmpty()) {
                targetGuests = getCorrectGuestName(guestName);
                try {
                    if (Objects.isNull(targetGuests)) { // if user enter wrong name 3 times
                        throw new CheckOutGuestNameNotFoundException();
                    }
                } catch (CheckOutGuestNameNotFoundException e) {
                    System.out.printf("%s Exiting to main menu.\n", e.getMessage());
                    return;
                }
            }
            System.out.printf("Guests with name %s are listed below: \n", guestName);
            int i = 0;
            for (Guest guest : targetGuests) {
                System.out.printf("%d. Name:%s Contact:%s%n", ++i, guest.getGuestName(),
                        guest.getContact());
            }
            int guestIndex = 1;
            if (targetGuests.size() > 1) {
                System.out.println("Multiple guests with same name found. Please select one of them");
                guestIndex = Parser.getChoice();
            }
            targetGuest = targetGuests.get(guestIndex - 1);
        } else if (choice == 2) {
            System.out.println("Enter guest contact number for check-out service: ");
            String guestContact = sc.nextLine().trim();
            List<Guest> targetGuests = guestController.searchGuestByContact(guestContact);
            if (targetGuests.isEmpty()) {
                targetGuests = getCorrectGuestContact(guestContact);
                try {
                    if (Objects.isNull(targetGuests)) { // if user enter wrong name 3 times
                        throw new CheckOutGuestContactNotFoundException();
                    }
                } catch (CheckOutGuestContactNotFoundException e) {
                    System.out.printf("%s. Exiting to main menu.\n", e.getMessage());
                    return;
                }
            }
            System.out.printf("Guests with contact number %s are listed below: \n", guestContact);
            int i = 0;
            for (Guest guest : targetGuests) {
                System.out.printf("%d. Name:%s Contact:%s%n", ++i, guest.getGuestName(),
                        guest.getContact());
            }
            int guestIndex = 1;
            if (targetGuests.size() > 1) {
                System.out.println("Multiple guests with same contact number found. Please select one of them");
                guestIndex = Parser.getChoice();
            }
            targetGuest = targetGuests.get(guestIndex - 1);
        } else {
            return;
        }
        List<Room> bookedOccupiedRoomUnderTargetGuest =
                roomController.findOccupiedRoomsByGuest(targetGuest);
        if (bookedOccupiedRoomUnderTargetGuest.isEmpty()) {
            System.out.printf("%s has no booked room. Exiting to main menu.\n", targetGuest.getGuestName());
            return;
        }
        System.out.printf("%s has booked room(s) listed below: \n", targetGuest.getGuestName());
        int i = 0;
        for (Room room : bookedOccupiedRoomUnderTargetGuest) {
            System.out.printf("%d. Room number:%s Room type:%s%n", ++i,
                    room.getRoomNumber(), room.getRoomType());
        }
        int roomIndex = 1;
        if (bookedOccupiedRoomUnderTargetGuest.size() > 1) {
            System.out.println("Multiple rooms found. Please select one of them");
            roomIndex = Parser.getChoice();

        }
        Room targetRoom = bookedOccupiedRoomUnderTargetGuest.get(roomIndex - 1);

        try {
            System.out.println("Please enter check out time for this reservation (yyyy-MM-dd " +
                    "HH:mm):");
            LocalDateTime checkOutDate;
            checkOutDate = getValidDateTime();

            boolean hasPromotion = getPromotion();
            Bill currentBill = checkOutController.checkOut(targetRoom, hasPromotion, checkOutDate);
            System.out.println("Check-out service is completed. Bill is listed below: ");
            System.out.println(currentBill);
            System.out.println(makePayment(currentBill.getTotalPrice(), targetGuest));
            reservationController.deleteReservationAfterCheckingOutByRoom(targetRoom);
            orderController.flushRoomOrderAfterCheckOut(targetRoom);  // remove room orders only when payment is finished
            if (roomController.checkOutRoom(targetRoom, checkOutDate)) {
                System.out.printf("Room %s has been set to available.%n", targetRoom.getRoomNumber());
            } else {
                System.out.printf("Room %s has been set to reserved for future reservation.%n",
                        targetRoom.getRoomNumber());
            }
            System.out.println("Successfully check out!");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date or time keyed in! Exiting to check out page.");
        } catch (InvalidCheckOutTimeException e) {
            System.out.println(e.getMessage());
            System.out.println("Checking out failed. Exiting to check out page.");
        } catch (IllegalRoomInSerializableBinaryFileException e) {
            System.out.println(e.getMessage());
            System.out.println("Checking out failed. Exiting to check out page.");
        }
    }

    /**
     * Makes the payment by calling the Payable interface. It allows user to choose the payment method.
     *
     * @param totalAmount The total amount of bill.
     * @param guest       The guest who is paying the bill.
     * @return the String of payment details.
     */
    private String makePayment(double totalAmount, Guest guest) {
        System.out.println("Enter payment method, 1: cash, 2: credit card");
        int choice = Parser.getChoice();
        while (choice != 1 && choice != 2) {
            System.out.println("Invalid input. Please enter again");
            System.out.println("Enter payment method, 1: cash, 2: credit card");
            choice = Parser.getChoice();
        }
        if (choice == 1) {
            return checkOutController.generatePayment(totalAmount).pay();
        } else {
            // generate a creditcard payment based on the guest's credit card
            return checkOutController.generatePayment(totalAmount, guest.getCreditCard()).pay();
        }
    }

    /**
     * Gets from the users input whether the guest has promotion or not.
     *
     * @return true if user wants to use promotion, false otherwise
     */
    private boolean getPromotion() {
        System.out.println("Do you want to apply promotion code? (1: Yes, 0: No)");
        int choice = Parser.getChoice();
        while (choice != 0 && choice != 1) {
            System.out.println("Invalid input. Please enter 1 or 0");
            choice = Parser.getChoice();
        }
        return choice == 1;
    }


    /**
     * Asks for the user to enter the guest name and returns the list of guests with matching
     * name. If the user enters a name that is not found in the record for 3 times, it returns null.
     *
     * @param guestName guest name
     * @return list of guests with matching name if found with in 3 tries, null otherwise
     */
    private List<Guest> getCorrectGuestName(String guestName) {
        int counter = 0;
        while (counter < 3) {
            System.out.println("Guest named, " + guestName + ", is not found. Please try again");
            System.out.println("Enter guest name for check-out service: ");
            guestName = sc.nextLine().trim();
            List<Guest> targetGuest = guestController.searchGuestByName(guestName);
            counter++;
            if (!targetGuest.isEmpty()) {
                return targetGuest;
            }
        }
        return null;
    }

    /**
     * Asks for the user to enter the guest name and returns the list of guests with matching
     * name. If the user enters a name that is not found in the record for 3 times, it returns null.
     *
     * @param guestContact guest name
     * @return list of guests with matching name if found with in 3 tries, null otherwise
     */
    private List<Guest> getCorrectGuestContact(String guestContact) {
        int counter = 0;
        while (counter < 3) {
            System.out.println("Guest with contact: " + guestContact + " is not found. Please " +
                    "try" +
                    " again");
            System.out.println("Enter guest contact for check-out service: ");
            guestContact = sc.nextLine().trim();
            List<Guest> targetGuest = guestController.searchGuestByContact(guestContact);
            counter++;
            if (!targetGuest.isEmpty()) {
                return targetGuest;
            }
        }
        return null;
    }

    /**
     * Checks iteratively if the user enters a valid date and time.
     *
     * @return a valid date time from the user input.
     * @throws InvalidDateTimeFormatException when the user input is not in the correct format.
     */
    private LocalDateTime getValidDateTime() throws InvalidDateTimeFormatException {   // check input format yyyy-MM-dd
        // HH:mm
        Scanner in = new Scanner(System.in);
        String inputDateTime = in.nextLine().trim();
        if (!Parser.isValidInputDate(inputDateTime)) {
            int counter = 0;
            while (!Parser.isValidInputDate(inputDateTime)) {
                if (counter == 2) {
                    throw new InvalidDateTimeFormatException();
                }
                System.out.println("Invalid date format! Please enter again (in yyyy-MM-dd HH:mm)" +
                        ": ");
                inputDateTime = in.nextLine().trim();
                counter++;
            }
        }
        inputDateTime = inputDateTime.trim().replace(" ", "T");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime formatDateTime = LocalDateTime.parse(inputDateTime, formatter);
        return formatDateTime;
    }

    /**
     * Displays the menu for check out service.
     */
    private void displayCheckOutUIMainMenu() {
        System.out.println("0. Back to main menu");
        System.out.println("1. Check out a guest by name");
        System.out.println("2. Check out a guest by contact");
    }
}


