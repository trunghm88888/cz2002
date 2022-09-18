package hrps.entity;

import java.io.Serializable;

/**
 * This class represents a credit card entity.
 *
 * @author An Ruyi
 */
public class CreditCard implements Serializable {
    /**
     * This credit card's number.
     */
    private String creditCardNumber;
    /**
     * This credit card's billing address.
     */
    private String billingAddress;

    /**
     * Create a credit card.
     *
     * @param creditCardNumber Credit card's number.
     * @param billingAddress   Credit card's billing address.
     */
    public CreditCard(String creditCardNumber, String billingAddress) {
        this.creditCardNumber = creditCardNumber;
        this.billingAddress = billingAddress;
    }

    /**
     * Get the number of this credit card.
     *
     * @return A String represents this credit card's number.
     */
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    /**
     * Get the billing address of this credit card.
     *
     * @return A String represent this credit card's billing address.
     */
    public String getBillingAddress() {
        return billingAddress;
    }

    /**
     * Change the number of this credit card.
     *
     * @param creditCardNumber New number.
     */
    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    /**
     * Change the billing address of this credit card.
     *
     * @param billingAddress New billing address.
     */
    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    /**
     * Display this credit card as a String.
     *
     * @return A String contains information about credit card's number and billing address.
     */
    @Override
    public String toString() {
        return String.format("{\ncreditCardNumber=%s \nbillingAddress=%s ",
                creditCardNumber, billingAddress);
    }
}
