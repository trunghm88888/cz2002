package hrps.entity;

import hrps.exception.IllegalChangeOfDateException;
import hrps.exception.InvalidStatusChangeException;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * This subclass represents checked-out reservations which are created when guest of a checked-in reservation checked
 * out.
 *
 * @author An Ruyi, Peng wenxuan
 */
public class CheckedOutReservation extends Reservation {
    /**
     * Guest's checked-in time.
     */
    private final LocalDateTime checkInTime;
    /**
     * Guest's checked-out time.
     */
    private final LocalDateTime checkOutTime;

    /**
     * Create a checked-out reservation.
     *
     * @param numAdult        Number of adults.
     * @param numChild        Number of children.
     * @param guest           The guest who made this reservation.
     * @param reservationCode Reservation code.
     * @param checkInTime     Checked-in time.
     * @param checkOutTime    Checked-out time.
     * @param roomNum         Room number which was used.
     */
    public CheckedOutReservation(int numAdult, int numChild, Guest guest,
                                 UUID reservationCode, LocalDateTime checkInTime,
                                 LocalDateTime checkOutTime, String roomNum) {
        super(numAdult, numChild, guest, reservationCode, roomNum);
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
    }

    /**
     * Get total number of days stayed (both weekdays and weekends).
     *
     * @return An integer represents total number of days stayed (both weekdays and weekends).
     */
    public int getNumberOfDayStayed() {
        return (int) checkInTime.until(checkOutTime, java.time.temporal.ChronoUnit.DAYS);
    }

    /**
     * Get total number of weekdays stayed.
     *
     * @return An integer represents total number of weekdays stayed.
     */
    public int getNumberOfWeekdayStayed() {
        int weekdayStayed = 0;
        for (int i = 0; i < getNumberOfDayStayed(); i++) {
            if (checkInTime.plusDays(i).getDayOfWeek().getValue() < 6) {
                weekdayStayed++;
            }
        }
        return weekdayStayed;
    }

    /**
     * Get total number of weekends stayed.
     *
     * @return An integer represents total number of weekends stayed.
     */
    public int getNumberOfWeekendStayed() {
        int weekendStayed = 0;
        for (int i = 0; i < getNumberOfDayStayed(); i++) {
            if (checkInTime.plusDays(i).getDayOfWeek().getValue() > 5) {
                weekendStayed++;
            }
        }
        return weekendStayed;
    }

    @Override
    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    @Override
    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }

    @Override
    public String getStatus() {
        return "Checked out";
    }

    @Override
    public void setCheckInTime(LocalDateTime checkInTime) throws IllegalChangeOfDateException {
        throw new IllegalChangeOfDateException();
    }

    @Override
    public void setCheckOutTime(LocalDateTime checkOutTime) throws IllegalChangeOfDateException {
        throw new IllegalChangeOfDateException();
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

}
