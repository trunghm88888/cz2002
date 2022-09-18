package hrps.control;

import hrps.entity.*;
import hrps.exception.InvalidCheckOutTimeException;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The controller that handles the checking out process, including checking out guest(s), requesting payment and
 * generating the bill(s).
 *
 * @author An Ruyi
 */
public class CheckOutController {
    /**
     * The static controller instance to avoid multiple instantiations.
     */
    private static CheckOutController instance;
    /**
     * Instance of reservation controller.
     */
    private final ReservationController reservationController = ReservationController.getInstance();
    /**
     * Instance of order controller.
     */
    private final OrderController orderController = OrderController.getInstance();

    /**
     * Constructor to get reservation controller and order controller.
     */
    private CheckOutController() {}

    /**
     * Get the CheckOutController instance. This is to avoid multiple instantiation of controller.
     *
     * @return The singleton RoomController instance.
     */
    public static CheckOutController getInstance() {
        if (Objects.isNull(instance)) {
            instance = new CheckOutController();
        }
        return instance;
    }

    /**
     * Check out guest(s) and the associated check-in reservations from the specified room. Calculate the total price
     * of room and room services and generate the bill.
     *
     * @param targetRoom   The room to be checked out.
     * @param hasPromotion true if this bill is valid for promotion, false otherwise.
     * @param checkOutDate the actual date of check-out that is used to compute the total housing
     *                    price.
     * @return The Bill containing the details of total amount the guest need to pay.
     * @throws InvalidCheckOutTimeException if the check-out date is before the check-in date.
     */
    public Bill checkOut(Room targetRoom, boolean hasPromotion, LocalDateTime checkOutDate) throws InvalidCheckOutTimeException {
        CheckedInReservation targetCheckedInReservation =
                reservationController.findCheckedInReservationByRoom(targetRoom);
        if (checkOutDate.isBefore(targetCheckedInReservation.getCheckInTime())) {
            throw new InvalidCheckOutTimeException();
        }
        double servicePrice = orderController.findOrderedRoomServiceTotalPriceByRoom(targetRoom);
        double roomFlatPrice = targetRoom.getRate();
        CheckedOutReservation checkedOutReservation =
                targetCheckedInReservation.checkOut(checkOutDate);
        Bill currentBill = new Bill(checkedOutReservation, roomFlatPrice, servicePrice,
                hasPromotion);
        return currentBill;
    }

    /**
     * Generate a payment.
     *
     * @param totalAmount Total amount needed to be paid.
     * @return An object of a Payable-implementing class, accepted as a payment.
     */
    public Payable generatePayment(double totalAmount) {
        return new CashPayment(totalAmount);
    }

    /**
     * Generate a credit card payment.
     *
     * @param totalAmount Total amount needed to be paid.
     * @param creditCard  The credit card used to pay.
     * @return An object of a Payable-implementing class, accepted as a payment.
     */
    public Payable generatePayment(double totalAmount, CreditCard creditCard) {
        return new CreditCardPayment(creditCard, totalAmount);
    }
}
