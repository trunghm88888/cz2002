package hrps.entity;

import hrps.exception.InvalidStatusChangeException;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * This subclass represents pending reservations which are first created when a guest made a reservation and needed to
 * be confirmed on check-in/check-out time and room number.
 *
 * @author An Ruyi, Peng Wenxuan
 */
public class WaitListReservation extends Reservation {
    /**
     * Guest's desired check-in time.
     */
    private LocalDateTime desiredCheckInTime;
    /**
     * Guest's desired check-out time.
     */
    private LocalDateTime desiredCheckOutTime;

    /**
     * Main constructor.
     *
     * @param numAdult            Number of adults.
     * @param numChild            Number of children.
     * @param guest               The guest who made this reservation.
     * @param desiredCheckInTime  Time wished to check in.
     * @param desiredCheckOutTime Time wished to check out.
     * @param roomNum             Room number wished to reserve.
     */
    public WaitListReservation(int numAdult, int numChild, Guest guest,
                               LocalDateTime desiredCheckInTime, LocalDateTime desiredCheckOutTime, String roomNum) {
        super(numAdult, numChild, guest, roomNum);
        this.desiredCheckInTime = desiredCheckInTime;
        this.desiredCheckOutTime = desiredCheckOutTime;
    }

    @Override
    public LocalDateTime getCheckInTime() {
        return desiredCheckInTime;
    }

    @Override
    public LocalDateTime getCheckOutTime() {
        return desiredCheckOutTime;
    }

    @Override
    public ConfirmedReservation confirm(String roomNum) {
        return new ConfirmedReservation(getNumAdult(), getNumChild(), getGuest(),
                desiredCheckInTime, desiredCheckOutTime, roomNum);
    }

    @Override
    public ExpiredReservation cancel() {
        return new ExpiredReservation(getNumAdult(), getNumChild(), getGuest(), getRoomNum(), desiredCheckInTime);
    }

    @Override
    public CheckedInReservation checkIn(LocalDateTime actualCheckInTime) throws InvalidStatusChangeException {
        throw new InvalidStatusChangeException();
    }

    @Override
    public CheckedOutReservation checkOut(LocalDateTime checkOutTime) throws InvalidStatusChangeException {
        throw new InvalidStatusChangeException();
    }

    @Override
    public String getStatus() {
        return "Waiting";
    }

    @Override
    public void setCheckInTime(LocalDateTime checkInTime) {
        desiredCheckInTime = checkInTime;
    }

    @Override
    public void setCheckOutTime(LocalDateTime checkOutTime) {
        desiredCheckOutTime = checkOutTime;
    }
}
