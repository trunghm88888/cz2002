package hrps.entity;

import hrps.exception.IllegalChangeOfDateException;
import hrps.exception.InvalidStatusChangeException;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * This subclass represents expired reservations which are created from pending/confirmed reservations when the guest
 * did not check in by a specific time period after the desired/confirmed check-in time.
 *
 * @author An Ruyi, Peng Wenxuan
 */
public class ExpiredReservation extends Reservation {
    /**
     * Guest's expected check-in time (if exceeding this check in time for more than 24 hours, reservation will expire).
     */
    private LocalDateTime expectedCheckInTime;

    /**
     * Create an expired reservation.
     *
     * @param numAdult Number of adults.
     * @param numChild Number of children.
     * @param guest    The guest who made this reservation.
     * @param roomNum  Room number wished to reserve.
     * @param expectedCheckInTime The guest's expected check-in time.
     */
    public ExpiredReservation(int numAdult, int numChild, Guest guest, String roomNum, LocalDateTime expectedCheckInTime) {
        super(numAdult, numChild, guest, roomNum);
        this.expectedCheckInTime = expectedCheckInTime;
    }

    @Override
    public String getStatus() {
        return "Expired";
    }

    @Override
    public LocalDateTime getCheckInTime() {
        return null;
    }

    @Override
    public LocalDateTime getCheckOutTime() {
        return null;
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
    public CheckedOutReservation checkOut(LocalDateTime checkOutTime) throws InvalidStatusChangeException {
        throw new InvalidStatusChangeException();
    }

    @Override
    public void setCheckInTime(LocalDateTime checkInTime) throws IllegalChangeOfDateException {
        throw new IllegalChangeOfDateException();
    }

    @Override
    public void setCheckOutTime(LocalDateTime checkOutTime) throws IllegalChangeOfDateException {
        throw new IllegalChangeOfDateException();
    }

    /**
     * @return the apology message that represents this expired reservation.
     */
    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String checkInTime = dateFormat.format(expectedCheckInTime);
        return String.format("Sorry. %s's reservation with expected check-in time on %s " +
                "has expired.\n", getGuest(), checkInTime);
    }
}
