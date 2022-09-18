package hrps.entity;

/**
 * This class represents the bill of a payment, including number of days (weekend/non-weekend) stayed, room price,
 * service price, tax, promotion and total amount needed to be paid.
 *
 * @author An Ruyi
 */
public class Bill {
    /**
     * Multiplier of price after promotion.
     */
    private static final double PROMOTION_RATE = 0.9;
    /**
     * Multiplier of weekend room rate.
     */
    private static final double WEEKEND_RATE = 1.1;
    /**
     * Multiplier of weekday room rate.
     */
    private static final double WEEKDAY_RATE = 1.0;
    /**
     * Multiplier of tax.
     */
    private static final double TAX_RATE = 0.07;

    /**
     * The reservation that is billed.
     */
    private final CheckedOutReservation checkedOutReservation;
    /**
     * Base price of this bill's reservation's room.
     */
    private final double roomFlatPrice;
    /**
     * This bill is applied promotion or not.
     */
    private final boolean hasPromotion;
    /**
     * This bill's reservation's room service price.
     */
    private final double servicePrice;

    /**
     * Create a bill with information from a CheckedOutReservation.
     *
     * @param checkedOutReservation The checked out reservation needed to bill.
     * @param roomFlatPrice         Room basic price.
     * @param servicePrice          Room service price.
     * @param hasPromotion          Whether this bill is applied promotion or not.
     */
    public Bill(CheckedOutReservation checkedOutReservation, double roomFlatPrice,
                double servicePrice, boolean hasPromotion) {
        this.checkedOutReservation = checkedOutReservation;
        this.roomFlatPrice = roomFlatPrice;
        this.hasPromotion = hasPromotion;
        this.servicePrice = servicePrice;
    }

    /**
     * Get the total price that the guest needs to pay (tax included).
     *
     * @return total amount of money the guest needs to pay.
     */
    public double getTotalPrice() {
        double rawPrice = calculateRoomPrice() + servicePrice;
        double promotedPrice;
        if (hasPromotion) {
            promotedPrice = rawPrice * PROMOTION_RATE;
        } else {
            promotedPrice = rawPrice;
        }
        return calculatePostTaxPrice(promotedPrice);
    }

    /**
     * Gets the amount of price reduction by promotion on a raw price.
     *
     * @param rawPrice Price before promotion.
     * @return price reduction by promotion.
     */
    private double calculatePromotionDiscount(double rawPrice) {
        return rawPrice * (1 - PROMOTION_RATE);
    }

    /**
     * Get the amount of tax on a raw price.
     *
     * @param rawPrice Price before tax.
     * @return Amount of tax.
     */
    private double calculateTaxPayable(double rawPrice) {
        return rawPrice * TAX_RATE;
    }

    /**
     * Calculate the price after tax, which is rawPrice * (1 + TAX_RATE).
     * @param rawPrice The base price.
     * @return A double representing post-tax price.
     */
    private double calculatePostTaxPrice(double rawPrice) {
        return rawPrice * (1 + TAX_RATE);
    }

    /**
     * Calculates room price of the associated reservation based on number of weekday and weekend
     * stays.
     *
     * @return Room price.
     */
    private double calculateRoomPrice() {
        int numberOfWeekends = checkedOutReservation.getNumberOfWeekendStayed();
        int numberOfWeekdays = checkedOutReservation.getNumberOfWeekdayStayed();
        double weekendPrice = roomFlatPrice * numberOfWeekends * WEEKEND_RATE;
        double weekdayPrice = roomFlatPrice * numberOfWeekdays * WEEKDAY_RATE;
        return weekendPrice + weekdayPrice;
    }

    /**
     * Represents a bill as a string includes Numbers of days (weekends/weekdays) stayed, Room flat price, Service
     * price, Tax, Promotion (optional), Total price.
     *
     * @return A String representing the bill.
     */
    @Override
    public String toString() {
        int numberOfRows = hasPromotion ? 9 : 8;
        String[][] table = new String[numberOfRows][];
        table[0] = new String[]{"Number of days stayed",
                Integer.toString(checkedOutReservation.getNumberOfDayStayed())};
        table[1] = new String[]{"Number of weekdays stayed",
                Integer.toString(checkedOutReservation.getNumberOfWeekdayStayed())};
        table[2] = new String[]{"Number of weekends stayed",
                Integer.toString(checkedOutReservation.getNumberOfWeekendStayed())};
        table[3] = new String[]{"Total room price: ", String.format("$%.2f", calculateRoomPrice())};
        table[4] = new String[]{"Total service price: ", String.format("$%.2f", servicePrice)};
        if (hasPromotion) {
            double promotionPercentage = (1 - PROMOTION_RATE) * 100;
            String promotionPercentageString = String.format("(-%%%.0f)", promotionPercentage);
            table[5] = new String[]{"Promotion discount: " + promotionPercentageString, String.format("-$%.2f",
                    calculatePromotionDiscount(calculateRoomPrice() + servicePrice))};
            table[6] = new String[]{"Remaining price: ", String.format("$%.2f",
                    calculateRoomPrice() + servicePrice -
                            calculatePromotionDiscount(calculateRoomPrice() + servicePrice))};
            table[7] = new String[]{"Tax payable: ",
                    String.format("$%.2f",
                            calculateTaxPayable(calculateRoomPrice() + servicePrice
                                    - calculatePromotionDiscount(calculateRoomPrice() + servicePrice)))};
            table[8] = new String[]{"Total amount payable: ", String.format("$%.2f",
                    getTotalPrice())};
        } else {
            table[5] = new String[]{"Raw price: ", String.format("$%.2f",
                    calculateRoomPrice() + servicePrice)};
            table[6] = new String[]{"Tax payable: ", String.format("$%.2f",
                    calculateTaxPayable(calculateRoomPrice() + servicePrice))};
            table[7] = new String[]{"Total amount payable: ", String.format("$%.2f",
                    getTotalPrice())};
        }
        String invoiceTitle = "-----------------------BILL INVOICE------------------------\n";
        String separator = "-----------------------------+-----------------------------\n";
        StringBuilder display = new StringBuilder(invoiceTitle);
        for (String[] row : table) {
            display.append(String.format("| %-27s| %-27s|%n", row));
            display.append(separator);
        }
        return display.toString();
    }
}
