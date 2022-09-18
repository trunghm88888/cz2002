package hrps.entity;

/**
 * This class represents credit card payment method implementing the Payable interface.
 *
 * @author An Ruyi
 */
public class CreditCardPayment implements Payable {
    /**
     * The credit card used to make this payment.
     */
    private final CreditCard creditCard;
    /**
     * Amount of this payment.
     */
    private final double amount;

    /**
     * Billing address of this payment.
     */
    private final String billingAddress;

    /**
     * Create a credit card payment request.
     *
     * @param creditCard The credit card used to make payment.
     * @param amount     The total amount of money need to be paid.
     */
    public CreditCardPayment(CreditCard creditCard, double amount) {
        this.creditCard = creditCard;
        this.amount = amount;
        this.billingAddress = creditCard.getBillingAddress();
    }

    /**
     * @return the message for this credit card payment.
     */
    @Override
    public String pay() {
        return "Paying " + getTotalAmount() + " " + getPaymentDescription() + "\nCredit Card Number: " +
                creditCard.getCreditCardNumber().substring(0, 4)
                + "X".repeat(creditCard.getCreditCardNumber().length() - 8)
                + creditCard.getCreditCardNumber()
                .substring(creditCard.getCreditCardNumber().length() - 4);
    }

    @Override
    public double getTotalAmount() {
        return amount;
    }

    @Override
    public String getPaymentDescription() {
        return String.format("using Credit Card. Bill sent to %s.", billingAddress);
    }
}

