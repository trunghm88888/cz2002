package hrps.boundary;

import hrps.control.GuestController;
import hrps.entity.Guest;
import hrps.entity.ID;
import hrps.entity.enums.IDType;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * This class is used for displaying UI when doing operations relating guests, including registering new guests,
 * update guests' details, find a particular guest by contact number.
 *
 * @author An Ruyi
 */
public class GuestUI {
    /**
     * Static GuestUI instance to be used by other classes to call GuestUI's methods.
     */
    private static GuestUI instance = null;
    /**
     * Get the GuestController to access and update guests data.
     */
    private final GuestController guestController = GuestController.getInstance();
    /**
     * The only scanner used for this UI.
     */
    private static final Scanner sc = new Scanner(System.in);

    /**
     * The constructor is set to private to prevent multiple instantiations.
     */
    private GuestUI() {
    }

    /**
     * Gets the UI instance, or create new one if no UI was instantiated.
     * This helps prevent multiple instantiations of UI which are unnecessary.
     *
     * @return The singleton GuestUI instance ready to be used to call methods of GuestUI class.
     */
    public static GuestUI getInstance() {
        if (Objects.isNull(instance)) {
            instance = new GuestUI();
        }
        return instance;
    }

    /**
     * Starts the UI for Guest.
     */
    void run() {
        try {
            displayMainGuestUIOptions();
            int choice = Parser.getChoice();
            while (choice != 0) {
                switch (choice) {
                    case 1:
                    case 2:
                    case 3: {
                        System.out.print("Please enter Guest Contact: ");
                        String guestContact = getValidContactNumber();
                        List<Guest> guestList = guestController.searchGuestByContact(guestContact);
                        if (guestList.isEmpty()) { // if there is no guest with the given contact number
                            if (choice != 1) {
                                System.out.println("No guest found with this contact number.");
                            } else {
                                Guest newGuest = createNewGuestUI(guestContact);
                                System.out.println("New guest has been added to the system: ");
                                System.out.println(newGuest);
                            }
                        } else { // if there is a guest with the given contact number
                            if (choice == 2) { // if the user wants to update the guest details
                                System.out.println("Similar records are present in the system: ");
                                for (Guest guest : guestList) {
                                    System.out.println(guest);
                                    System.out.println("Is this the guest whom you are looking for?");
                                    System.out.print("1. Yes\n2. No\nYour choice: ");
                                    int choiceForConfirmingSimilarResult = Parser.getChoice();
                                    if (choiceForConfirmingSimilarResult == 1) {
                                        updateGuestDetailsUI(guest);
                                        break;
                                    }
                                }
                                System.out.println("No similar records found. Exiting\n");
                            } else { // if the user wants to find a guest with specified contact number
                                System.out.printf("Similar records of contact %s are present in " +
                                        "the system: %n", guestContact);
                                int i = 1;
                                for (Guest guest : guestList) {
                                    System.out.printf("%d. \n%s\n", i++, guest);
                                }
                            }
                        }
                    }
                    break;
                    case 4: {
                        String keyword = "";
                        while (Objects.isNull(keyword) || keyword.equals("")) {
                            System.out.print("Please enter a keyword to search for guest's name: ");
                            keyword = sc.nextLine();
                            if (Objects.isNull(keyword) || keyword.equals(""))
                                System.out.println("The keyword cannot be empty!");
                        }
                        List<Guest> resultGuests = guestController.searchGuestByRelativeName(keyword);
                        if (resultGuests.size() == 0)
                            System.out.println("No guest with name containing the given keyword!\n");
                        else {
                            System.out.printf("Guest with naming containing %s:%n", keyword);
                            int i = 0;
                            for (Guest guest : resultGuests) {
                                System.out.printf("%d. \n%s\n", ++i, guest);
                            }
                        }
                    }
                    break;
                    default:
                        System.out.println("Invalid choice. Exiting.");
                        break;

                }
                displayMainGuestUIOptions();
                choice = Parser.getChoice();
            }
        } catch (Exception e) {
            System.out.println("An error occured. Exiting.");
        }
    }

    /**
     * Updates the guest details following inputs from the user.
     *
     * @param guestToUpdate The guest to be updated.
     */
    private void updateGuestDetailsUI(Guest guestToUpdate) {
        System.out.println("Please enter the information that you want to update: ");
        System.out.printf("1. Name%n2. Address%n3. Contact%n4. Credit Card%n5. ID %n" +
                "6. Gender%n7. Nationality%n0. Cancel %n");
        int choice = Parser.getChoice();
        System.out.println("Please enter the information to be updated: ");
        switch (choice) {
            case 1:
                System.out.print("Guest Name: ");
                String updatedName = sc.nextLine().trim();
                while (updatedName.isEmpty()) {
                    System.out.println("Name cannot be empty. Please enter again: ");
                    System.out.println("Guest Name: ");
                    updatedName = sc.nextLine().trim();
                }
                guestController.updateGuestName(guestToUpdate, updatedName);
                break;
            case 2:
                System.out.print("Guest Address: ");
                String updatedAddress = sc.nextLine().trim();
                guestController.updateAddress(guestToUpdate, updatedAddress);
                break;
            case 3:
                System.out.print("Guest Contact: ");
                String updatedContact;
                updatedContact = getValidContactNumber();
                guestController.updateContact(guestToUpdate, updatedContact);
                break;
            case 4:
                System.out.print("Guest Credit Card: ");
                String updatedCreditCard = getValidCreditCardNumberUI();
                System.out.print("Credit card billing address: ");
                String updatedBillingAddress = sc.nextLine().trim();
                guestController.updateCreditCard(guestToUpdate, updatedCreditCard, updatedBillingAddress);
                break;
            case 5:
                ID updatedId = getIdDetailUI();
                guestController.updateId(guestToUpdate, updatedId);
                break;
            case 6:
                System.out.print("Guest Gender: (m/f/o) (o stands for Others)");
                String updatedGender = sc.nextLine().trim();
                while (!(updatedGender.equalsIgnoreCase("m") || updatedGender.equalsIgnoreCase("f") ||
                        updatedGender.equalsIgnoreCase("o"))) {
                    System.out.println("Invalid input. Please enter again: ");
                    System.out.print("Guest Gender: (m/f/o) (o stands for Others)");
                    updatedGender = sc.nextLine().trim();
                }
                guestController.updateGender(guestToUpdate, updatedGender);
                break;
            case 7:
                System.out.print("Guest nationality: ");
                String updatedNationality = sc.nextLine().trim();
                guestController.updateNationality(guestToUpdate, updatedNationality);
                break;
            case 0:
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
        if (choice <= 7 && choice >= 1) { // if the user has made a valid choice to update
            System.out.println("Guest details updated: ");
            System.out.println(guestToUpdate);
        }
    }

    /**
     * Display UI to read user's inputs and create a new guest from given contact number and the inputs.
     *
     * @param guestContact The contact number of the guest to be created.
     * @return The newly created guest.
     */
    Guest createNewGuestUI(String guestContact) {
        System.out.println("Please enter the following details for the new guest.");
        System.out.print("Name: ");
        String guestName = sc.nextLine().trim();
        while (guestName.isEmpty()) {
            System.out.println("Name cannot be empty. Please try again.");
            System.out.print("Name: ");
            guestName = sc.nextLine().trim();
        }
        System.out.print("Address: ");
        String guestAddress = sc.nextLine().trim();
        System.out.print("Living country: ");
        String guestCountry = sc.nextLine().trim();
        System.out.print("Gender: (m/f/o) (o stands for Others)");
        String guestGender = sc.nextLine().trim();
        while (!(guestGender.equalsIgnoreCase("m") || guestGender.equalsIgnoreCase("f") ||
                guestGender.equalsIgnoreCase("o"))) {
            System.out.println("Invalid input. Please try again.");
            System.out.print("Gender: (m/f/o) (o stands for Others)");
            guestGender = sc.nextLine().trim();
        }
        System.out.print("Nationality: ");
        String guestNationality = sc.nextLine().trim();

        // Key in credit card
        String creditCardNumber = getValidCreditCardNumberUI();

        System.out.print("Credit Card Billing Address: ");
        String creditBillingAddress = sc.nextLine().trim();

        // ID of guest
        ID id = getIdDetailUI();

        Guest newGuest = guestController.createGuest(guestName, creditCardNumber, creditBillingAddress,
                guestAddress, guestCountry, guestGender, id.getIdNumber(), id.getIdType(),
                guestNationality, guestContact);
        return newGuest;

    }

    /**
     * Iteratively asks for user input until a valid contact number is entered.
     *
     * @return A String representing a valid contact number.
     */
    private String getValidContactNumber() {
        String contactNumber = sc.nextLine().trim().replace(" ", "");
        boolean isValid = GuestController.checkContactInput(contactNumber);
        while (!isValid) {
            System.out.println("Invalid contact number. Please try again.");
            contactNumber = sc.nextLine().trim().replace(" ", "");
            isValid = GuestController.checkContactInput(contactNumber);
        }
        return contactNumber;
    }

    /**
     * Iteratively asks for user input until a valid credit card number is entered.
     *
     * @return A String representing a valid credit card number.
     */
    private String getValidCreditCardNumberUI() {
        System.out.print("Credit Card Number: ");
        String creditCardNumber = sc.nextLine().trim().replace(" ", "");
        boolean isValidCreditCardNumber = guestController.checkCreditCardInput(creditCardNumber);
        while (!isValidCreditCardNumber) {
            //System.out.print(creditCardNo.length());
            System.out.print("Invalid Credit Card Number. Please re-enter Credit Card Number: ");
            creditCardNumber = sc.nextLine().trim().replace(" ", "");
            isValidCreditCardNumber = guestController.checkCreditCardInput(creditCardNumber);
        }
        return creditCardNumber;
    }

    /**
     * Iteratively asks for user input until a valid entry of ID is entered.
     *
     * @return A valid ID.
     */
    private ID getIdDetailUI() {
        IDType idType;
        String idNumber;
        // Key in ID type and ID number
        // A loop is set up to make sure that either type of ID is keyed in
        // as ID is a mandatory field.
        System.out.print("ID Type: (1. Passport, 2. Driving License) ");
        int idTypeChoice = Parser.getChoice();
        while (idTypeChoice < 1 || idTypeChoice > 2) {
            System.out.println("Invalid choice. Please try again.");
            System.out.print("ID Type: (1. Passport, 2. Driving License) ");
            idTypeChoice = Parser.getChoice();
        }
        switch (idTypeChoice) {
            case 1:
                idType = IDType.PASSPORT;
                break;
            case 2:
                idType = IDType.DRIVING_LICENSE;
                break;
            default:
                idType = null;
        }
        if (idType.equals(IDType.PASSPORT)) {
            System.out.print("Passport Number: ");
            idNumber = sc.nextLine().trim();
            while (idNumber.isEmpty()) {
                System.out.println("You have not keyed in a passport number. Please try again.");
                System.out.print("Passport Number: ");
                idNumber = sc.nextLine().trim();
            }
        } else {
            System.out.print("Driving License Number: ");
            idNumber = sc.nextLine().trim();
            while (idNumber.isEmpty()) {
                System.out.println("You have not keyed in a driving license number. Please try again.");
                System.out.print("Driving License Number: ");
            }
        }
        return new ID(idType, idNumber);
    }

    /**
     * Displays the main menu of the Guest UI.
     */
    private void displayMainGuestUIOptions() {
        System.out.println("-".repeat(30));
        System.out.println("0. Go back to MainUI");
        System.out.println("1. Create a new guest");
        System.out.println("2. Update guest details");
        System.out.println("3. Find a guest by contact number");
        System.out.println("4. Find a guest by name");
        System.out.println("Your choice: ");
    }
}
