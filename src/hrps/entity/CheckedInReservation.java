package hrps.entity;

import hrps.exception.IllegalChangeOfDateException;
import hrps.exception.InvalidStatusChangeException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * This subclass represents checked-in reservations which are created when guest of a confirmed reservation checked-in.
 * This type of reservation has the ability to order room services.
 *
 * @author An Ruyi, Peng Wenxuan
 */
public class CheckedInReservation extends Reservation implements Serializable {
    /**
     * Time when guest of this reservation checked-in.
     */
    private final LocalDateTime checkInTime;
    /**
     * Expected check-out time of guest of this reservation.
     */
    private LocalDateTime expectedCheckOutTime;
    /**
     * List of room services which the guest of this reservation ordered.
     */
    private final List<OrderedRoomService> orderedRoomServices;

    /**
     * Create a checked-in reservation.
     *
     * @param numAdult             Number of adults.
     * @param numChild             Number of children.
     * @param guest                The guest who made this reservation.
     * @param reservationCode      Reservation code.
     * @param checkInTime          Checked-in time.
     * @param expectedCheckOutTime Expected check-out time.
     * @param roomNum              Room number to be used.
     */
    public CheckedInReservation(int numAdult, int numChild, Guest guest, UUID reservationCode,
                                LocalDateTime checkInTime, LocalDateTime expectedCheckOutTime, String roomNum) {
        super(numAdult, numChild, guest, reservationCode, roomNum);
        this.checkInTime = checkInTime;
        this.expectedCheckOutTime = expectedCheckOutTime;
        this.orderedRoomServices = new java.util.ArrayList<>();
    }

    @Override
    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    @Override
    public LocalDateTime getCheckOutTime() {
        return expectedCheckOutTime;
    }

    /**
     * Get expected check-out time of this reservation.
     *
     * @return A LocalDateTime object: expected check-out time.
     */
    public LocalDateTime getExpectedCheckOutTime() {
        return expectedCheckOutTime;
    }

    /**
     * Add an ordered room service to this reservation.
     *
     * @param orderedRoomService The OrderedRoomService object to be added.
     */
    public void addOrderedRoomService(OrderedRoomService orderedRoomService) {
        orderedRoomServices.add(orderedRoomService);
    }

    /**
     * Get list of this reservation's ordered room services.
     *
     * @return A List-implementing object of ordered room services.
     */
    List<OrderedRoomService> getOrderedRoomServices() {
        return this.orderedRoomServices;
    }

    /**
     * Get total price of this reservation's ordered room services.
     *
     * @return A Double represents total price of room services.
     */
    double getTotalServicePrice() {
        double totalPrice = 0;
        for (OrderedRoomService orderedRoomService : orderedRoomServices) {
            totalPrice += orderedRoomService.getTotalPrice();
        }
        return totalPrice;
    }

    @Override
    public CheckedOutReservation checkOut(LocalDateTime checkOutTime) {
        return new CheckedOutReservation(getNumAdult(), getNumChild(), getGuest(),
                getReservationCode(), this.checkInTime, checkOutTime, getRoomNum());
    }

    @Override
    public ExpiredReservation cancel() throws InvalidStatusChangeException {
        throw new InvalidStatusChangeException();
    }

    @Override
    public CheckedInReservation checkIn(LocalDateTime actualCheckInTime) throws InvalidStatusChangeException {
        throw new InvalidStatusChangeException();
    }

    @Override
    public ConfirmedReservation confirm(String roomNum) throws InvalidStatusChangeException {
        throw new InvalidStatusChangeException();
    }

    @Override
    public String getStatus() {
        return "Checked in";
    }

    @Override
    public void setCheckInTime(LocalDateTime checkInTime) throws IllegalChangeOfDateException {
        throw new IllegalChangeOfDateException();
    }

    @Override
    public void setCheckOutTime(LocalDateTime checkOutTime) {
        expectedCheckOutTime = checkOutTime;
    }
}
