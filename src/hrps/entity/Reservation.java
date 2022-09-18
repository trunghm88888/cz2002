package hrps.entity;

import hrps.exception.IllegalChangeOfDateException;
import hrps.exception.InvalidStatusChangeException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * This class represents an abstraction of a reservation, including check-in/checkout time, numbers of
 * adults/children, the guest who made the reservation and the room number wished to be booked.
 *
 * @author An Ruyi
 */
public abstract class Reservation implements Serializable {
    // private static final long serialVersionUID = 1L;
    /**
     * This reservation's code, unique for each reservation.
     */
    private final UUID reservationCode;
    /**
     * Number of adults registered for this reservation.
     */
    private int numAdult;
    /**
     * Number of children registered for this reservation.
     */
    private int numChild;
    /**
     * The guest made this reservation.
     */
    private Guest guest;
    /**
     * Room number wished to be booked for this reservation.
     */
    private String roomNum;

    /**
     * This is only called when we would like to instantiate a new reservation's subclass,
     * only when we create a waiting reservation upon customers' demands.
     *
     * @param numAdult - number of adults in this reservation
     * @param numChild - number of children in this reservation
     * @param guest - guest who made this reservation
     * @param roomNum - room number wished to be booked for this reservation
     */
    Reservation(int numAdult, int numChild, Guest guest, String roomNum) {
        this.numAdult = numAdult;
        this.numChild = numChild;
        this.reservationCode = UUID.randomUUID();
        this.guest = guest;
        this.roomNum = roomNum;
    }

    /**
     * This is only called when we would like to instantiate a new reservation's subclass
     * without changing its unique reservation code.
     * For instance, when we confirm a reservation from a WaitListReservation,
     * a ConfirmedReservation is created with the same reservation code as the WaitListReservation.
     *
     * @param numAdult - number of adults in this reservation
     * @param numChild - number of children in this reservation
     * @param guest - guest who made this reservation
     * @param roomNum - room number wished to be booked for this reservation
     * @param reservationCode - unique reservation code that is used to identify this reservation
     */
    Reservation(int numAdult, int numChild, Guest guest, UUID reservationCode, String roomNum) {
        this.numAdult = numAdult;
        this.numChild = numChild;
        this.reservationCode = reservationCode;
        this.guest = guest;
        this.roomNum = roomNum;
    }

    /**
     * Get the status of this reservation.
     *
     * @return A String represents this reservation's status.
     */
    public abstract String getStatus();

    /**
     * Get this reservation's code.
     *
     * @return A UUID represents this reservation's code.
     */
    public UUID getReservationCode() {
        return reservationCode;
    }

    /**
     * Get number of adults reserved.
     *
     * @return An integer represents number of adults.
     */
    public int getNumAdult() {
        return numAdult;
    }

    /**
     * Get number of children reserved.
     *
     * @return An integer represents number of children.
     */
    public int getNumChild() {
        return numChild;
    }

    /**
     * Set the number of adults reserved.
     *
     * @param numAdult New number of adults.
     */
    public void setNumAdult(int numAdult) {
        this.numAdult = numAdult;
    }

    /**
     * Set the number of children reserved.
     *
     * @param numChild New number of children.
     */
    public void setNumChild(int numChild) {
        this.numChild = numChild;
    }

    /**
     * Change the guest of this reservation.
     *
     * @param guest New guest.
     */
    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    /**
     * Get the guest who made this reservation.
     *
     * @return A Guest object represents the guest who made this reservation.
     */
    public Guest getGuest() {
        return guest;
    }

    /**
     * Change the room number reserved for this reservation.
     *
     * @param roomNum New room number.
     */
    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    /**
     * Get the room number reserved for this reservation.
     *
     * @return A String represents the room number.
     */
    public String getRoomNum() {
        return roomNum;
    }

    /**
     * Get check-in time, return null if guest has not provided the information.
     *
     * @return A LocalDateTime represents check-in time.
     */
    public abstract LocalDateTime getCheckInTime();


    /**
     * Get check-out time, return null if guest has not provided the information.
     *
     * @return A LocalDateTime represents check-out time.
     */
    public abstract LocalDateTime getCheckOutTime();

    /**
     * Confirm this reservation.
     *
     * @param roomNum The confirmed room number to be reserved.
     * @return A ConfirmedReservation object represents this confirmed reservation.
     * @throws InvalidStatusChangeException When this method is called on a Reservation object other than
     *                                      Pending subclass.
     */
    public abstract ConfirmedReservation confirm(String roomNum) throws InvalidStatusChangeException;

    /**
     * Check in this reservation.
     *
     * @param actualCheckInTime The datetime when the guest checked in.
     * @return A CheckedInReservation object represents this check-in.
     * @throws InvalidStatusChangeException When this method is called on a Reservation object other than Confirmed
     *                                      subclass.
     */
    public abstract CheckedInReservation checkIn(LocalDateTime actualCheckInTime) throws InvalidStatusChangeException;

    /**
     * Change this reservation to be expired.
     *
     * @return A ExpiredReservation.
     * @throws InvalidStatusChangeException When this method is called on a Reservation object other than Confirmed
     *                                      subclass.
     */
    public abstract ExpiredReservation cancel() throws InvalidStatusChangeException;

    /**
     * Check out this reservation.
     *
     * @param checkOutTime The time when the guest checked out.
     * @return A CheckedOutReservation object.
     * @throws InvalidStatusChangeException When this method is called on a Reservation object other than CheckedIn
     *                                      subclass.
     */
    public abstract CheckedOutReservation checkOut(LocalDateTime checkOutTime) throws InvalidStatusChangeException;

    /**
     * Change the desired/confirmed check-in time of this reservation
     *
     * @param checkInTime New check-in time.
     * @throws IllegalChangeOfDateException When this method is called on a Reservation object other than WaitList or
     *                                      Confirmed subclasses.
     */
    public abstract void setCheckInTime(LocalDateTime checkInTime) throws IllegalChangeOfDateException;

    /**
     * Change the desired/confirmed check-out time of this reservation
     *
     * @param checkOutTime New check-out time.
     * @throws IllegalChangeOfDateException When this method is called on a Reservation object other than WaitList or
     *                                      Confirmed subclasses.
     */
    public abstract void setCheckOutTime(LocalDateTime checkOutTime) throws IllegalChangeOfDateException;

    /**
     * Get a string of basic reservation information, including reserved room number, reservation status,
     * number of adults/children, guest's name, guest contact, check in date time, check out date time in a table format.
     *
     * @return A String represents this reservation.
     */
    @Override
    public String toString() {
        String checkInTimePrefix;
        if (this instanceof CheckedInReservation || this instanceof CheckedOutReservation) {
            checkInTimePrefix = "Actual ";
        } else {
            checkInTimePrefix = "Expected ";
        }
        String checkOutTimePrefix;
        if (this instanceof CheckedOutReservation) {
            checkOutTimePrefix = "Actual ";
        } else {
            checkOutTimePrefix = "Expected ";
        }

        String[][] table = new String[8][];
        table[0] = new String[]{"Room number", getRoomNum()};
        table[1] = new String[]{"Reservation status", getStatus()};
        table[2] = new String[]{"Guest Name", guest.getGuestName()};
        table[3] = new String[]{"Guest Contact", guest.getContact()};
        table[4] = new String[]{"Number of Adult", Integer.toString(numAdult)};
        table[5] = new String[]{"Number of children", Integer.toString(numChild)};
        table[6] = new String[]{checkInTimePrefix + "Check-in time", getCheckInTime().toString()};
        table[7] = new String[]{checkOutTimePrefix + "Check-out time", getCheckOutTime().toString()};
        String separator = "---------------------------+-----------------------\n";
        StringBuilder display = new StringBuilder(separator);
        for (String[] row : table) {
            display.append(String.format("| %-25s|  %-20s|%n", row));
            display.append(separator);
        }
        return display.toString();
    }
}
