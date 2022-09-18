package hrps.entity;

import hrps.exception.InvalidStatusChangeException;

import java.time.LocalDateTime;

/**
 * This subclass represents confirm reservations which are created when a pending reservation is confirmed with
 * check-in/check-out time and room number.
 *
 * @author An Ruyi, Peng Wenxuan
 */
public class ConfirmedReservation extends Reservation {
    /**
     * Guest's confirmed check-in time.
     */
    private LocalDateTime confirmedCheckInTime;
    /**
     * Guest's confirmed check-out time.
     */
    private LocalDateTime confirmedCheckOutTime;

    /**
     * Create a confirmed reservation.
     *
     * @param numAdult              Number of adults.
     * @param numChild              Number of children.
     * @param guest                 The guest who made this reservation.
     * @param confirmedCheckInTime  Time confirmed to check in.
     * @param confirmedCheckOutTime Time confirmed to check out.
     * @param roomNum               Room number confirmed to reserve.
     */
    public ConfirmedReservation(int numAdult, int numChild, Guest guest,
                                LocalDateTime confirmedCheckInTime, LocalDateTime confirmedCheckOutTime, String roomNum) {
        super(numAdult, numChild, guest, roomNum);
        this.confirmedCheckInTime = confirmedCheckInTime;
        this.confirmedCheckOutTime = confirmedCheckOutTime;
    }

    @Override
    public LocalDateTime getCheckInTime() {
        return confirmedCheckInTime;
    }

    @Override
    public LocalDateTime getCheckOutTime() {
        return confirmedCheckOutTime;
    }

    @Override
    public ExpiredReservation cancel() {
        return new ExpiredReservation(getNumAdult(), getNumChild(), getGuest(), getRoomNum(), confirmedCheckInTime);
    }

    @Override
    public CheckedInReservation checkIn(LocalDateTime actualCheckInTime) {
        return new CheckedInReservation(getNumAdult(), getNumChild(), getGuest(),
                getReservationCode(), actualCheckInTime, getCheckOutTime(), getRoomNum());
    }

    @Override
    public ConfirmedReservation confirm(String roomNum) throws InvalidStatusChangeException {
        throw new InvalidStatusChangeException();
    }

    @Override
    public CheckedOutReservation checkOut(LocalDateTime checkOutTime) throws InvalidStatusChangeException {
        throw new InvalidStatusChangeException();
    }

    @Override
    public String getStatus() {
        return "Confirmed";
    }

    @Override
    public void setCheckInTime(LocalDateTime checkInTime) {
        confirmedCheckInTime = checkInTime;
    }

    @Override
    public void setCheckOutTime(LocalDateTime checkOutTime) {
        confirmedCheckInTime = checkOutTime;
    }
}
