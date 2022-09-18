package hrps.entity;

/**
 * This class represents cash payments that Guest makes, implementing the Payable interface.
 *
 * @author An Ruyi
 */
public class CashPayment implements Payable {
    /**
     * Amount of this payment.
     */
    private final double amount;

    /**
     * Create a cash payment request.
     *
     * @param amount The amount of cash in this payment.
     */
    public CashPayment(double amount) {
        this.amount = amount;
    }

    @Override
    public double getTotalAmount() {
        return amount;
    }

    /**
     * @return The message for this cash payment.
     */
    @Override
    public String pay() {
        return "Paying " + getTotalAmount() + getPaymentDescription();
    }

    @Override
    public String getPaymentDescription() {
        return "using cash";
    }

}
