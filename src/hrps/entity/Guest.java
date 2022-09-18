package hrps.entity;

import hrps.entity.enums.IDType;

import java.io.Serializable;

/**
 * This class represents guests with information including name, gender, address, country, nationality, contact info,
 * credit card info, id number and type.
 *
 * @author An Ruyi
 */
public class Guest implements Serializable {
    /**
     * This guest's full name.
     */
    private String guestName;
    /**
     * This guest's address.
     */
    private String address;
    /**
     * This guest's residential country.
     */
    private String country;
    /**
     * This guest's gender.
     */
    private String gender;
    /**
     * This guest's nationality.
     */
    private String nationality;
    /**
     * This guest's contact phone number.
     */
    private String contact;
    /**
     * This guest's credit card.
     */
    private final CreditCard creditCard;
    /**
     * This guest's identification document.
     */
    private ID id;

    /**
     * Create a guest.
     *
     * @param guestName            Name of guest
     * @param creditCardNo         Credit card number in type of string, should only contain digits from 0 to 9
     * @param creditBillingAddress Credit card billing address, different parts should be separated by commas
     * @param address              Guest's address, different parts should be separated by commas
     * @param country              Guest's living country
     * @param gender               Guest's gender
     * @param idNumber             Guest's id number
     * @param idType               Guest's id type, an enum constant of IDType (see enum IDType for more information)
     * @param nationality          Guest's nationality
     * @param contact              Guest's contact information
     */
    public Guest(String guestName, String creditCardNo, String creditBillingAddress, String address,
                 String country, String gender, String idNumber, IDType idType,
                 String nationality, String contact) {
        creditCard = new CreditCard(creditCardNo, creditBillingAddress);
        this.guestName = guestName;
        this.address = address;
        this.country = country;
        setGender(gender);
        this.nationality = nationality;
        this.contact = contact;
        id = new ID(idType, idNumber);
    }

    /**
     * Get guest's name.
     *
     * @return A string representing guest's name
     */
    public String getGuestName() {
        return guestName;
    }

    /**
     * Get guest's address.
     *
     * @return A string representing guest's address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Get guest's country.
     *
     * @return A string representing guest's country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Get guest's gender.
     *
     * @return A string representing guest's gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Get guest's nationality.
     *
     * @return A string representing guest's nationality
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * Get guest's contact information.
     *
     * @return A string representing guest's contact information
     */
    public String getContact() {
        return contact;
    }

    /**
     * Get guest's credit card.
     *
     * @return Credit card object of the guest
     */
    public CreditCard getCreditCard() {
        return creditCard;
    }

    /**
     * Get guest's id number.
     *
     * @return A string representing guest's id number
     */
    public String getIdNumber() {
        return id.getIdNumber();
    }

    /**
     * Get guest's id type.
     *
     * @return An enum constant from IDType enum representing the guest's id type
     */
    public IDType getIdType() {
        return id.getIdType();
    }

    /**
     * Change guest's name.
     *
     * @param guestName New name
     */
    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    /**
     * Change guest's address.
     *
     * @param address New address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Change guest's country.
     *
     * @param country New country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Change guest's gender.
     *
     * @param gender New gender
     */
    public void setGender(String gender) {
        if (gender.equalsIgnoreCase("m")) {
            gender = "Male";
        } else if (gender.equalsIgnoreCase("f")) {
            gender = "Female";
        } else {
            gender = "Others";
        }
        this.gender = gender;
    }

    /**
     * Change guest's nationality.
     *
     * @param nationality New nationality
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    /**
     * Change guest's contact information.
     *
     * @param contact New contact information
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * Change guest's credit card's information.
     *
     * @param creditCardNo         New credit card number
     * @param creditBillingAddress New credit card billing address
     */
    public void setCreditCardDetails(String creditCardNo, String creditBillingAddress) {
        this.creditCard.setCreditCardNumber(creditCardNo);
        this.creditCard.setBillingAddress(creditBillingAddress);
    }

    /**
     * Change guest's ID
     *
     * @param newId The new ID to be assigned to this guest.
     */
    public void setId(ID newId) {
        this.id = newId;
    }

    /**
     * Return a string representing all information of the guest including name, credit card number, billing address,
     * address, country, gender, id type and number, nationality, contact information.
     *
     * @return A string contains all information of the guest.
     */
    @Override
    public String toString() {
        String[][] table = new String[9][];
        table[0] = new String[]{"Guest name", guestName};
        table[1] = new String[]{"Credit card No", creditCard.getCreditCardNumber()};
        table[2] = new String[]{"Billing Address", creditCard.getBillingAddress()};
        table[3] = new String[]{"Address", address};
        table[4] = new String[]{"Country", country};
        table[5] = new String[]{"Gender", gender};
        table[6] = new String[]{id.getIdTypeName(), id.getIdNumber()};
        table[7] = new String[]{"Nationality", nationality};
        table[8] = new String[]{"Contact", contact};
        String separator = "----------------------+-----------------------\n";
        StringBuilder display = new StringBuilder(separator);
        for (String[] row : table) {
            display.append(String.format("| %-20s|  %-20s|%n", row));
            display.append(separator);
        }
        return display.toString();
    }
}
