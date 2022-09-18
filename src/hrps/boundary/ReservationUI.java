package hrps.boundary;

import hrps.control.GuestController;
import hrps.control.ReservationController;
import hrps.control.RoomController;
import hrps.entity.*;
import hrps.exception.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * This class is used for displaying the UI when making reservation, including creating (includes walk-in), updating,
 * removing and printing reservations.
 *
 * @author Peng Wenxuan
 */
public class ReservationUI {
    /**
     * Static ReservationUI instance to be used by other classes to call ReservationUI's methods.
     */
    private static ReservationUI instance = null;
    /**
     * Use ReservationController to access and update reservations' data.
     */
    private final ReservationController reservationController = ReservationController.getInstance();
    /**
     * The only scanner used for this UI.
     */
    private static final Scanner in = new Scanner(System.in);
    /**
     * Define BOOK reserving type.
     */
    private static final boolean isBookOnline = true;
    /**
     * Define WALK IN reserving type.
     */
    private static final boolean isWalkIn = false;

    /**
     * Private constructor to prevent instantiation from other classes.
     */
    private ReservationUI() {
    }

    /**
     * Get the UI instance, or create new one if no UI was instantiated.
     * This helps prevent multiple instantiations of UI which is unnecessary.
     *
     * @return The singleton ReservationUI instance ready to be used to call methods of
     * ReservationUI class.
     */
    public static ReservationUI getInstance() {
        if (Objects.isNull(instance)) {
            instance = new ReservationUI();
        }
        return instance;
    }

    /**
     * Display the options for reservations-related methods.
     */
    private void displayOptions() {
        System.out.println("-".repeat(30));
        System.out.println("0. Go back to MainUI");
        System.out.println("1. Create a new reservation");
        System.out.println("2. Update reservation details");
        System.out.println("3. Cancel reservation");    // for confirmed reservation that does not checkIn in time (or customer changes decision), staff can cancel it
        System.out.println("4. Check in reservation");
        System.out.println("5. Drop reservations on wait list");
        // for staff to check and remove those reservations in waitList that are expired
        System.out.println("6. Walk-in reservation");
        System.out.println("7. Print all reservations");
        System.out.println("Your choice: ");
    }

    /**
     * Start the displaying and interfacing procedure.
     */
    void run() {
        displayOptions();
        int option = Parser.getChoice();
        String guestContact;
        while (option != 0) {
            try {
                switch (option) {
                    case 1:
                    case 2:
                    case 3:
                    case 4: {
                        guestContact = getValidGuestContact();
                        List<Reservation> reservationsFound = reservationController.searchReservationsByContact(guestContact);
                        if (reservationsFound.isEmpty()) {
                            if (option == 1) {
                                System.out.println("This is your first time to make a reservation!");
                                newReservationUI(guestContact, isBookOnline);
                            } else {
                                System.out.println("No records found!");
                            }
                        } else {
                            if (option == 1) {
                                System.out.println("There are already reservations with the same guest contact: ");
                                for (Reservation reservation : reservationsFound) {
                                    System.out.println(reservation.toString());
                                }
                                System.out.println("Are you sure you want to make another reservation?");
                                System.out.println("1. Yes");
                                System.out.println("2. No");
                                System.out.println("Your choice: ");
                                int choiceForAnotherReservation = in.nextInt();
                                in.nextLine();
                                if (choiceForAnotherReservation == 1) {
                                    newReservationUI(guestContact, isBookOnline);
                                } else if (choiceForAnotherReservation == 2) {
                                    break;
                                } else {
                                    throw new InvalidInputChoiceException();
                                }
                            } else {
                                System.out.println("Records found!");
                                for (Reservation reservation : reservationsFound) {
                                    System.out.println(reservation.toString());
                                    System.out.println("Is this the reservation you are looking for?");
                                    System.out.println("1. Yes");
                                    System.out.println("2. No");
                                    System.out.println("Your choice: ");
                                    int choiceOnConfirmingReservation = in.nextInt();
                                    in.nextLine();
                                    if (choiceOnConfirmingReservation == 1) {
                                        if (option == 2) {
                                            updateReservationUI(reservation);
                                        } else if (option == 3) {
                                            cancelExpiredReservation(reservation);
                                        } else if (option == 4) {
                                            checkInReservation(reservation);
                                        }
                                        break;
                                    } else if (choiceOnConfirmingReservation == 2) {
                                        continue;
                                    } else {
                                        throw new InvalidInputChoiceException();
                                    }
                                }
                            }
                        }
                        break;
                    }
                    case 5:
                        // for staff to check and manually remove those reservations in waitList
                        // that are expired.
                        removeWaitingReservationUI();
                        break;
                    case 6:
                        createWalkInReservation();
                        break;
                    case 7:
                        printReservationUI();
                        break;
                    default:
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Warning: Invalid input!");
            } catch (HRPSException e) {
                System.out.println("Warning: " + e.getMessage() + "Exiting...");
            } catch (DateTimeParseException e) {
                System.out.println("Warning: Invalid date keyed in. Breaking to menu...");
            } catch (Exception e) {
                System.out.println("An error has occurred. Exiting...");
            }
            displayOptions();
            option = Parser.getChoice();
        }

    }

    /**
     * Get a valid contact number from user and start the UI for creating reservation with reserving mode WALK_IN.
     */
    private void createWalkInReservation() {
        String guestContact;
        try {
            guestContact = getValidGuestContact();
            newReservationUI(guestContact, isWalkIn);
        } catch (InputMismatchException e) {
            System.out.println("Warning: Invalid input!");
        } catch (HRPSException e) {
            System.out.println("Warning: " + e.getMessage());
        }
    }

    /**
     * Iteratively asks for user input until a valid contact number is entered.
     *
     * @return A String representing a valid contact number.
     */
    private String getValidGuestContact() {
        String guestContact;
        System.out.print("Please enter Guest Contact: ");
        guestContact = in.nextLine().trim().replace(" ", "");
        boolean isValid = GuestController.checkContactInput(guestContact);
        while (!isValid) {
            System.out.print("Invalid Contact. Please re-enter Contact: ");
            guestContact = in.nextLine().trim().replace(" ", "");
            isValid = GuestController.checkContactInput(guestContact);
        }
        return guestContact;
    }

    /**
     * Display the UI for reading user's inputs to creating a new reservation. If no room is vacant in the user's
     * checkin-checkout period, user could still put a waitlisted reservation which could be confirmed by user in the
     * future if there is vacant room.
     *
     * @param guestContact The contact of the guest making this reservation.
     * @param isBookOnline   true for book online, false for walk in.
     * @throws HRPSException If there is 1) invalid date time input
     *                      2) Invalid date time pair input (check-in date is after check-out date)
     *                      3) Negative input for number of adults and children under this
     *                      reservation.
     */

    private void newReservationUI(String guestContact, boolean isBookOnline) throws HRPSException {   //find available rooms that fit customers' requirements
        RoomUI roomUI = RoomUI.getInstance();
        RoomController roomController = RoomController.getInstance();
        Guest guest = getGuest(guestContact);
        System.out.println("Guest: " + guest.getGuestName());
        List<Room> preferredRooms = roomUI.findRoomBasedOnPreference();
        LocalDateTime desiredCheckInDate, desiredCheckOutDate;
        System.out.print("Enter expected check in time (yyyy-MM-dd HH:mm): ");
        desiredCheckInDate = getValidDateTime();
        System.out.print("Enter expected check out time (yyyy-MM-dd HH:mm): ");
        desiredCheckOutDate = getValidDateTime();
        List<Room> availableRooms =
                roomController.checkAvailableRooms(preferredRooms, desiredCheckInDate, desiredCheckOutDate);
        if (availableRooms.isEmpty()) {
            System.out.println("There is no currently available room with the specified " +
                    "requirements. ");
            System.out.println("However, you can put your reservation on a waiting list.");
            System.out.print("Do you want to put your reservation on a waiting list? (Y/N): ");
            String choice = in.nextLine().trim().toUpperCase();
            if (choice.equals("Y")) {
                System.out.println("You can choose the following rooms that you prefer to put on " +
                        "waitlist.");
                System.out.println("| Room number | Flat Rate | Wifi Available | Smoking Free |");
                for (Room room : preferredRooms) {
                    System.out.printf("|    %s    |  $%s%.2f  |       %s%s      |      %s%s     " +
                                    "|\n",
                            room.getRoomNumber(),
                            room.getRate() >= 100 ? "" : " ", room.getRate(), room.isHasWiFi() ? "" : " ",
                            room.isHasWiFi() ? "Yes" : "No", room.isSmokingFree() ? "" : " ",
                            room.isSmokingFree() ? "Yes" : "No");
                }
                String selectedRoomNumber = in.nextLine().trim();
                while (!checkRoomNumInAvailableList(preferredRooms, selectedRoomNumber)) {
                    System.out.println("Invalid room number! Please select again: ");
                    selectedRoomNumber = in.nextLine();
                }
                System.out.print("Number Of Adults: ");
                int numOfAdult = in.nextInt();
                if (numOfAdult < 0) {
                    throw new NegativeNumberException();
                }
                in.nextLine();
                System.out.print("Number Of Children: ");
                int numOfChild = in.nextInt();
                if (numOfChild < 0) {
                    throw new NegativeNumberException();
                }
                reservationController.createWaitListReservation(guest, guestContact, desiredCheckInDate, desiredCheckOutDate, numOfAdult, numOfChild, selectedRoomNumber); // create waiting reservation
                System.out.println("Your reservation has been put into wait list!");
            } else {
                return;
            }
        } else {    // there are rooms can be reserved for the time period
            System.out.println("These are the available rooms with the specified requirements: ");
            System.out.println("| Room number | Flat Rate | Wifi Available | Smoking Free |");
            for (Room room : preferredRooms) {
                System.out.printf("|    %s    |  $%s%.2f  |       %s%s      |      %s%s     |\n",
                        room.getRoomNumber(),
                        room.getRate() >= 100 ? "" : " ", room.getRate(), room.isHasWiFi() ? "" : " ",
                        room.isHasWiFi() ? "Yes" : "No", room.isSmokingFree() ? "" : " ",
                        room.isSmokingFree() ? "Yes" : "No");
            }
            //enter reservation detail
            System.out.println("Select a room: ");
            String selectedRoomNumber = in.nextLine();
            while (!checkRoomNumInAvailableList(availableRooms, selectedRoomNumber)) {
                System.out.println("Invalid roomNum! please select again: ");
                selectedRoomNumber = in.nextLine();
            }
            System.out.print("Number Of Adults: ");
            int numOfAdult = in.nextInt();
            if (numOfAdult < 0) {
                throw new NegativeNumberException();
            }
            in.nextLine();
            System.out.print("Number Of Children: ");
            int numOfChild = in.nextInt();
            if (numOfChild < 0) {
                throw new NegativeNumberException();
            }
            in.nextLine();
            Reservation reservationMade;
            if (isBookOnline) {
                reservationMade = reservationController.createConfirmedReservation(guest,
                        desiredCheckInDate, desiredCheckOutDate, numOfAdult, numOfChild, selectedRoomNumber);
                roomController.reserve(selectedRoomNumber);
                System.out.println(selectedRoomNumber + " has been reserved for " + guest.getGuestName() +
                        "!");
            } else {
                reservationMade = reservationController.createWalkInReservation(guest, guestContact, desiredCheckInDate, desiredCheckOutDate, numOfAdult, numOfChild, selectedRoomNumber);
                roomController.checkIn(selectedRoomNumber, guest);
                System.out.println(selectedRoomNumber + " has been set to occupied for walk in " +
                        "guest, " + guest.getGuestName() + "!");
            }
            System.out.println("New reservation added to the system: ");
            System.out.println(reservationMade.toString());
        }
    }

    /**
     * Get the guest with the specified guest's contact, if no guest with that contact, call the creating new guest
     * UI to create a new guest with that contact from GuestUI.
     *
     * @param guestContact The specified guest's contact.
     * @return A Guest with the above contact.
     */
    private Guest getGuest(String guestContact) {   // search guest contact, if guest already exist, no need to create a new one, else create a new one
        GuestUI guestUI = GuestUI.getInstance();
        GuestController guestController = GuestController.getInstance();
        List<Guest> guestMatch = guestController.searchGuestByContact(guestContact);   // find guest by contact
        Guest guest;
        if (guestMatch.isEmpty()) {
            guest = guestUI.createNewGuestUI(guestContact);
        } else {
            guest = guestMatch.get(0);
        }
        return guest;
    }

    /**
     * Iteratively asks for user input until a valid formatted date time is entered.
     *
     * @return A LocalDateTime representing the entered date time.
     * @throws InvalidDateTimeFormatException If an invalid date time is entered for more than 2 times.
     */
    private LocalDateTime getValidDateTime() throws InvalidDateTimeFormatException {   // check input format yyyy-MM-dd
        // HH:mm
        String inputDateTime = in.nextLine();
        if (!Parser.isValidInputDate(inputDateTime)) {
            int counter = 0;
            while (!Parser.isValidInputDate(inputDateTime)) {
                if (counter == 2) {
                    throw new InvalidDateTimeFormatException();
                }
                System.out.println("Invalid date format! Please enter again (in yyyy-MM-dd HH:mm)" +
                        ": ");
                inputDateTime = in.nextLine();
                counter++;
            }
        }
        inputDateTime = inputDateTime.trim().replace(" ", "T");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime formatDateTime = LocalDateTime.parse(inputDateTime, formatter);
        return formatDateTime;
    }

    /**
     * Check if a room number (as a String) actually represents an existing room.
     *
     * @param roomList List of rooms to check on.
     * @param roomNum  Room number to be checked.
     * @return true if a room with the above roomNum exists in roomList, false otherwise.
     */
    private boolean checkRoomNumInAvailableList(List<Room> roomList, String roomNum) {
        boolean validRoomNum = false;
        for (Room room : roomList) {
            if (room.getRoomNumber().trim().equals(roomNum)) {
                validRoomNum = true;
                break;
            }
        }
        return validRoomNum;
    }

    /**
     * Display the UI for reading user's inputs on updating details of a reservation.
     *
     * @param reservation The Reservation to be updated.
     * @throws HRPSException If there is 1) invalid date time input
     *                      2) Invalid date time pair input (check-in date is after check-out date)
     *                      3) Negative input for number of adults and children under this
     *                      reservation.
     */
    private void updateReservationUI(Reservation reservation) throws HRPSException {
        System.out.println("-".repeat(30));
        System.out.println("Please enter the information that you want to update: ");
        System.out.println("1. Guest Contact ");
        System.out.println("2. Check In time ");
        System.out.println("3. Check Out time ");
        System.out.println("4. Number Of Guests ");
        System.out.println("0. Cancel ");
        int choice = in.nextInt();
        in.nextLine();
        System.out.println("Please enter the updated information: ");
        String curRoomNum = reservation.getRoomNum();
        switch (choice) {
            case 1:
                System.out.print("Guest Contact: ");
                String updatedGuestContact = getValidGuestContact();
                Reservation updatedContact = reservationController.updateContact(reservation, updatedGuestContact);
                System.out.println("Reservation details updated: ");
                System.out.println(updatedContact.toString());
                break;
            case 2:
                System.out.print("Check In time (yyyy-MM-dd HH:mm): ");
                LocalDateTime updatedCheckInTime = getValidDateTime();
                Reservation updatedCheckIn = reservationController.updateCheckInTime(reservation, updatedCheckInTime);
                if (!updatedCheckIn.getCheckInTime().equals(updatedCheckInTime))
                    System.out.println("We cannot find a room that satisfies your new check-in time, therefore it is " +
                            "not updated");
                else {
                    System.out.println("Reservation details updated: ");
                    System.out.println(updatedCheckIn.toString());
                    if (!updatedCheckIn.getRoomNum().equals(curRoomNum))
                        System.out.println("There is an update in your room number due to the change in check-in " +
                                "time, your new room has all the conditions same as your old room!");
                }
                break;
            case 3:
                System.out.print("Check Out time (yyyy-MM-dd HH:mm): ");
                LocalDateTime updatedCheckOutTime = getValidDateTime();
                Reservation updatedCheckOut = reservationController.updateCheckOutTime(reservation, updatedCheckOutTime);
                if (!updatedCheckOut.getCheckInTime().equals(updatedCheckOutTime))
                    System.out.println("We cannot find a room that satisfies your new check-in time, therefore it is " +
                            "not updated");
                else {
                    System.out.println("Reservation details updated: ");
                    System.out.println(updatedCheckOut.toString());
                    if (!updatedCheckOut.getRoomNum().equals(curRoomNum))
                        System.out.println("There is an update in your room number due to the change in check-in " +
                                "time, your new room has all the conditions same as your old room!");
                }
                break;
            case 4:
                System.out.print("Number of guests: ");
                System.out.print("Enter number of adults and number of children (eg. 2 1)");
                int updatedNumOfAdult = in.nextInt();
                int updatedNumOfChild = in.nextInt();
                if (updatedNumOfAdult < 0 || updatedNumOfChild < 0)
                    throw new NegativeNumberException();
                in.nextLine();
                Reservation updatedNoOfGuests = reservationController.updateNumberOfGuests(reservation, updatedNumOfAdult, updatedNumOfChild);
                System.out.println("Reservation details updated: ");
                System.out.println(updatedNoOfGuests.toString());
                break;
            case 0:
                break;
        }
        return;
    }

    /**
     * Display each wait list reservation and ask user on to remove/not to remove it. If User
     * chooses to remove it,
     * then call reservation controller to remove that waitlisted reservation from the list of waitlisted reservations.
     *
     * @throws InvalidInputChoiceException if user enters invalid choice
     */
    // mark them as expired
    // then print that all expired reservations are removed from the list
    private void removeWaitingReservationUI() throws InvalidInputChoiceException {
        List<WaitListReservation> waitListReservations = reservationController.getAllWaitingReservations();
        for (WaitListReservation reservation : waitListReservations) {
            System.out.println("Do you want to remove this waiting reservation? (Y/N)");
            System.out.println(reservation);
            if (in.nextLine().charAt(0) == 'y' || in.nextLine().charAt(0) == 'Y') {
                reservationController.removeReservation(reservation);
                System.out.println("This waiting reservation has been removed!");
            } else if (in.nextLine().charAt(0) == 'n' || in.nextLine().charAt(0) == 'N') {
                // intentionally left blank to serve as an error check
            } else {
                throw new InvalidInputChoiceException();
            }
        }
    }

    /**
     * Check in reservation by guest contact, note that if actual check in date time is later than confirmed check in date time for more than 24 hours,
     * the reservation will expire.
     *
     * @param reservation the reservation needed to be checked in.
     * @throws InvalidDateTimeFormatException if the date time format is invalid
     * @throws InvalidStatusChangeException   if the status change of reservation is invalid
     */
    private void checkInReservation(Reservation reservation) throws InvalidDateTimeFormatException, InvalidStatusChangeException {
        System.out.println("Please enter actual check in date time (in yyyy-MM-dd HH:mm)");
        LocalDateTime actualCheckInDateTime = getValidDateTime();
        reservationController.validateCheckIn(reservation, actualCheckInDateTime);
    }

    /**
     * Cancel a certain reservation if a guest wants to cancel it before checking in or does not check in by his confirmed check in time,
     * the released room will be automatically assigned to a future-reserved reservation or a waitList reservation,
     * the expired reservation will be removed from the reservation list.
     *
     * @param reservation the reservation need to be canceled, which is searched by guest contact.
     */
    private void cancelExpiredReservation(Reservation reservation) throws CancelCheckInReservationException {
        if (reservation instanceof ConfirmedReservation) {
            RoomController roomController = RoomController.getInstance();
            Room releasedRoom = roomController.findRoomByRoomNumber(reservation.getRoomNum());
            if (roomController.checkOutRoom(releasedRoom, reservation.getCheckInTime())) {
                System.out.printf("Room %s has been set to available.%n", releasedRoom.getRoomNumber());
            } else {
                System.out.printf("Room %s has been set to reserved for future reservation.%n",
                        releasedRoom.getRoomNumber());
            }
            reservationController.removeReservation(reservation);
        } else {
            throw new CancelCheckInReservationException();
        }
    }

    /**
     * Print all reservations in the system record. If no reservation is found, display a message.
     */
    private void printReservationUI() {
        List<Reservation> reservations = reservationController.getAllReservations();
        List<WaitListReservation> waitListReservations = reservationController.getAllWaitingReservations();
        if (reservations.isEmpty() && waitListReservations.isEmpty()) {
            System.out.println("No records found!");
        } else {
            System.out.println("Printing all reservations in the system record...");
            System.out.println("Reservations in process");
            for (Reservation reservation : reservations) {
                System.out.println(reservation.toString());
                System.out.print('\n');
            }
            System.out.println("Reservations in waitList");
            for (WaitListReservation waitListReservation : waitListReservations) {
                System.out.println(waitListReservation.toString());
                System.out.print('\n');
            }
        }

    }
}