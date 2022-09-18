package hrps.control;

import hrps.entity.Guest;
import hrps.entity.ID;
import hrps.entity.enums.IDType;
import tool.SerializeDB;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class contains a container for all guests and all methods for guest-related operations, including creating,
 * searching, and updating guests' information.
 *
 * @author Peng Wenxuan, An Ruyi
 */
public class GuestController {
    /**
     * Guests data file's path.
     */
    private static final String GUEST_DATA_DIR = "src/data/guests.dat";
    /**
     * List of registered guests.
     */
    private final List<Guest> guestList;
    /**
     * The static controller instance to avoid multiple instantiations.
     */
    private static GuestController guestController = null;

    /**
     * Initialize GuestController by reading from guests data file, if the data file does not exist at the given path,
     * create it and write the on-memory guests data to the newly created file.
     */
    @SuppressWarnings("unchecked")
    private GuestController() {
        File file = new File(GUEST_DATA_DIR);
        if (file.exists()) {
            guestList = (ArrayList<Guest>) SerializeDB.readSerializedObject(GUEST_DATA_DIR);    // ArrayList
            // implements serializable by default
        } else {
            file.getParentFile().mkdir();
            guestList = new ArrayList<>();
            SerializeDB.writeSerializedObject(GUEST_DATA_DIR, guestList);
        }
    }

    /**
     * Get the GuestController instance that can be used to call non-static GuestController methods.
     *
     * @return A singleton GuestController instance.
     */
    public static GuestController getInstance() {
        if (Objects.isNull(guestController)) {
            guestController = new GuestController();
        }
        return guestController;
    }

    /**
     * Searching for guests with name exactly same as the input string, case ignored.
     *
     * @param guestName Name string to be searched
     * @return A list of guests with the specified name.
     */
    public List<Guest> searchGuestByName(String guestName) {
        return guestList.stream().filter(o -> o.getGuestName()
                .equalsIgnoreCase(guestName)).collect(Collectors.toList());
    }

    /**
     * Searching for guests whose name's words array contains the specified string, case ignored.
     * eg: guestnames = ["John A", "John B"]
     * keyword = "John" -> John A, John B are found
     * keyword = "A" -> John A is found
     * keyword = "john" -> John A, John B are found
     * keyword = "jo" -> no one is found
     *
     * @param keyword The specified String.
     * @return A list of guests whose name contains the specified string.
     */
    public List<Guest> searchGuestByRelativeName(String keyword) {
        return guestList.stream().filter(o -> Arrays.asList(o.getGuestName().toLowerCase().split(" "))
                .contains(keyword.toLowerCase())).collect(Collectors.toList());
    }

    /**
     * Searching for guests with a specific contact information, case ignored.
     *
     * @param guestContact Contact information string to be searched
     * @return A list of guest with the specified contact information.
     */
    public List<Guest> searchGuestByContact(String guestContact) {
        return guestList.stream().filter(o -> o.getContact()
                .equalsIgnoreCase(guestContact)).collect(Collectors.toList());
    }

    /**
     * Create a new guest and add that guest to the guest container and update the data file.
     *
     * @param guestName            Name of guest
     * @param creditCardNo         Credit card number in type of string, should only contain digits from 0 to 9
     * @param creditBillingAddress Credit card billing address, different parts should be separated by commas
     * @param address              Guest's address, different parts should be separated by commas
     * @param country              Guest's country
     * @param gender               Guest's gender
     * @param IDNumber             Guest's id number
     * @param IDType               Guest's id type, an enum constant of IDType (see enum IDType for more information)
     * @param nationality          Guest's nationality
     * @param contact              Guest's contact information
     * @return The guest just created
     */
    public Guest createGuest(String guestName, String creditCardNo, String creditBillingAddress, String address,
                             String country, String gender, String IDNumber, IDType IDType, String nationality, String contact) {
        Guest guest = new Guest(guestName, creditCardNo, creditBillingAddress, address, country, gender, IDNumber, IDType, nationality, contact);
        this.guestList.add(guest);
        SerializeDB.writeSerializedObject(GUEST_DATA_DIR, guestList);
        return guest;
    }

    /**
     * Update a guest's name.
     *
     * @param guest     Guest to be updated
     * @param guestName New name
     */
    public void updateGuestName(Guest guest, String guestName) {
        guest.setGuestName(guestName);
        SerializeDB.writeSerializedObject(GUEST_DATA_DIR, guestList);
    }

    /**
     * Update a guest's credit card information.
     *
     * @param guest                 Guest to be updated
     * @param updatedCreditCardNum  New credit card number
     * @param updatedBillingAddress New billing address
     */
    public void updateCreditCard(Guest guest, String updatedCreditCardNum, String updatedBillingAddress) {
        guest.setCreditCardDetails(updatedCreditCardNum, updatedBillingAddress);
        SerializeDB.writeSerializedObject(GUEST_DATA_DIR, guestList);
    }

    /**
     * Update a guest's address.
     *
     * @param guest          Guest to be updated
     * @param updatedAddress New address
     */
    public void updateAddress(Guest guest, String updatedAddress) {
        guest.setAddress(updatedAddress);
        SerializeDB.writeSerializedObject(GUEST_DATA_DIR, guestList);
    }

    /**
     * Update a guest's gender.
     *
     * @param guest         Guest to be updated
     * @param updatedGender New gender
     */
    public void updateGender(Guest guest, String updatedGender) {
        guest.setGender(updatedGender);
        SerializeDB.writeSerializedObject(GUEST_DATA_DIR, guestList);
    }

    /**
     * Update a guest's country.
     *
     * @param guest          Guest to be updated
     * @param updatedCountry New country
     */
    public void updateCountry(Guest guest, String updatedCountry) {
        guest.setCountry(updatedCountry);
        SerializeDB.writeSerializedObject(GUEST_DATA_DIR, guestList);
    }

    /**
     * Set the specified ID for the specified guest.
     *
     * @param guest The specified guest.
     * @param newId The specified ID.
     */
    public void updateId(Guest guest, ID newId) {
        guest.setId(newId);
        SerializeDB.writeSerializedObject(GUEST_DATA_DIR, guestList);
    }

    /**
     * Update a guest's nationality.
     *
     * @param guest              Guest to be updated
     * @param updatedNationality New nationality
     */
    public void updateNationality(Guest guest, String updatedNationality) {
        guest.setNationality(updatedNationality);
        SerializeDB.writeSerializedObject(GUEST_DATA_DIR, guestList);
    }

    /**
     * Update a guest's contact information.
     *
     * @param guest          Guest to be updated
     * @param updatedContact New contact information
     */
    public void updateContact(Guest guest, String updatedContact) {
        guest.setContact(updatedContact);
        SerializeDB.writeSerializedObject(GUEST_DATA_DIR, guestList);
    }

    /**
     * Check if a credit card number is in valid standard.
     * Here, we assume that any credit card number with 16 digits are valid credit card.
     *
     * @param creditCardNo The credit card number to be checked
     * @return true if credit card number is in valid standard, false otherwise
     */
    public boolean checkCreditCardInput(String creditCardNo) {
        return creditCardNo.length() == 16;
    }

    /**
     * Check if a String is a valid contact number, which is defined as a numeral string with up to 15 digits.
     *
     * @param contact The String to be checked.
     * @return true if the specified String is a valid contact number, false if it is not.
     */
    public static boolean checkContactInput(String contact) {
        if (contact.length() <= 15) {
            try {
                Long b = Long.parseLong(contact);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
    }
}
