package hrps.entity;

/**
 * Implementing this interface allows the class to be accepted as a valid payment by the HRPS system.
 *
 * @author An Ruyi
 */
public interface Payable {
    /**
     * Get the total amount of money of this payment.
     *
     * @return A double represents total amount of money.
     */
    public double getTotalAmount();

    /**
     * Get the payment method of this payment.
     *
     * @return A String represents the payment method.
     */
    public String getPaymentDescription();

    /**
     * Make a payment and generate a String containing all details of that payment.
     * @return A string represents the payment's details.
     */
    public String pay();
}
