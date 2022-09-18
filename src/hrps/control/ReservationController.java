package hrps.control;

import hrps.entity.*;
import hrps.exception.*;
import tool.SerializeDB;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * This class represents a controller for reservations, includes create, update, search and remove reservations from
 * the system.
 *
 * @author Peng Wenxuan
 */
public class ReservationController {
    /**
     * Confirmed/Checked-in/Checked-out/Expired reservations data file's path.
     */
    private static final String RESERVATION_DATA_DIR = "src/data/reservations.dat";
    /**
     * Waitlisted reservations data file's path.
     */
    private static final String WAITING_RESERVATION_DATA_DIR = "src/data/waitListReservations.dat";
    /**
     * ArrayList of Confirmed/Checked-in/Checked-out/Expired reservations.
     */
    private final ArrayList<Reservation> reservationList;
    /**
     * ArrayList of Waitlisted reservations.
     */
    private final ArrayList<WaitListReservation> waitListReservations;
    /**
     * The static controller instance to avoid multiple instantiations.
     */
    private static ReservationController reservationController = null;
    /**
     * Reservation Controller works closely with Room Controller.
     */
    private static final RoomController roomController = RoomController.getInstance();

    /**
     * Gets the controller instance. This is to avoid multiple instantiation of controller.
     *
     * @return The singleton reservation controller instance.
     */
    public static ReservationController getInstance() {
        if (Objects.isNull(reservationController)) {
            reservationController = new ReservationController();
        }
        return reservationController;
    }

    /**
     * Initialize ReservationController by reading from reservations data files.
     */
    private ReservationController() {
        reservationList = getReservationList(RESERVATION_DATA_DIR);
        waitListReservations = getWaitListReservations(WAITING_RESERVATION_DATA_DIR);
    }

    /**
     * Load data from the confirmed reservations data file, if the file does not exist, create and write on-memory
     * reservations data to it.
     *
     * @param dir The directory path.
     * @return An ArrayList of Reservation.
     */
    private ArrayList<Reservation> getReservationList(String dir) {
        ArrayList<Reservation> reservationList;
        File file = new File(dir);
        if (file.exists()) {
            reservationList = (ArrayList<Reservation>) SerializeDB.readSerializedObject(dir);    //
            // ArrayList
            // implements serializable by default
        } else {
            file.getParentFile().mkdir();
            reservationList = new ArrayList<>();
            SerializeDB.writeSerializedObject(dir, reservationList);
        }
        return reservationList;
    }

    /**
     * Load data from the waitlisted reservations data file, if the file does not exist, create and write
     * on-memory waitlisted reservations data to it.
     *
     * @param dir The directory path.
     * @return An ArrayList of WaitListReservation.
     */
    private ArrayList<WaitListReservation> getWaitListReservations(String dir) {
        ArrayList<WaitListReservation> waitListReservations;
        File file = new File(dir);
        if (file.exists()) {
            waitListReservations = (ArrayList<WaitListReservation>) SerializeDB.readSerializedObject(dir);    //
            // ArrayList
            // implements serializable by default
        } else {
            file.getParentFile().mkdir();
            waitListReservations = new ArrayList<>();
            SerializeDB.writeSerializedObject(dir, waitListReservations);
        }
        return waitListReservations;
    }

    /**
     * Create a pending reservation and add it to the reservations' container and update the database.
     *
     * @param guest                 Guest who made the reservation.
     * @param confirmedCheckInTime  Confirmed check-in time.
     * @param confirmedCheckOutTime Confirmed check-out time.
     * @param numOfAdult            Number of adults.
     * @param numOfChild            Number of children.
     * @param roomNum               Room no wished to be booked.
     * @return A Reservation that is in pending state and needed to be confirmed.
     */
    public ConfirmedReservation createConfirmedReservation(Guest guest,
                                                           LocalDateTime confirmedCheckInTime,
                                                           LocalDateTime confirmedCheckOutTime, int numOfAdult, int numOfChild, String roomNum) {
        ConfirmedReservation reservation = new ConfirmedReservation(numOfAdult, numOfChild, guest, confirmedCheckInTime, confirmedCheckOutTime, roomNum);   // random UUID already created in reservation superclass constructor
        this.reservationList.add(reservation);
        SerializeDB.writeSerializedObject(RESERVATION_DATA_DIR, reservationList);
        return reservation;
    }

    /**
     * Create a waitlisted reservation.
     *
     * @param guest               The guest who made the reservation.
     * @param guestContact        Guest's contact.
     * @param desiredCheckInTime  Desired check-in time.
     * @param desiredCheckOutTime Desired check-out time.
     * @param numOfAdult          Number of adults.
     * @param numOfChild          Number of children.
     * @param roomNum             Room number wished to be booked.
     * @return the reservation created in the waitlist
     */
    public WaitListReservation createWaitListReservation(Guest guest, String guestContact,
                                                         LocalDateTime desiredCheckInTime,
                                                         LocalDateTime desiredCheckOutTime, int numOfAdult, int numOfChild, String roomNum) {
        WaitListReservation reservation = new WaitListReservation(numOfAdult, numOfChild, guest, desiredCheckInTime, desiredCheckOutTime, roomNum);
        this.waitListReservations.add(reservation);
        SerializeDB.writeSerializedObject(WAITING_RESERVATION_DATA_DIR, waitListReservations);
        return reservation;
    }

    /**
     * Create a walk-in reservation which is checked-in, add it to the reservations' container and update the database.
     *
     * @param guest                Guest who made the reservation.
     * @param guestContact         Guest's contact.
     * @param checkInTime          Checked-in time.
     * @param expectedCheckOutTime Expected check-out time.
     * @param numOfAdult           Number of adults.
     * @param numOfChild           Number of children.
     * @param roomNum              Booked room no.
     * @return the checked in reservation from the walk-in
     */
    public CheckedInReservation createWalkInReservation(Guest guest, String guestContact,
                                                        LocalDateTime checkInTime, LocalDateTime expectedCheckOutTime,
                                                        int numOfAdult, int numOfChild, String roomNum) {
        UUID reservationCode = UUID.randomUUID();
        CheckedInReservation reservation = new CheckedInReservation(numOfAdult, numOfChild, guest,
                reservationCode, checkInTime, expectedCheckOutTime, roomNum);
        this.reservationList.add(reservation);
        SerializeDB.writeSerializedObject(RESERVATION_DATA_DIR, reservationList);
        return reservation;
    }

    /**
     * Update contact of the specified reservation.
     *
     * @param reservation    The specified reservation.
     * @param updatedContact New contact.
     * @return The reservation after updated.
     */
    public Reservation updateContact(Reservation reservation, String updatedContact) {
        GuestController gc = GuestController.getInstance();
        gc.updateContact(reservation.getGuest(), updatedContact);
        if(reservation instanceof WaitListReservation)
            SerializeDB.writeSerializedObject(WAITING_RESERVATION_DATA_DIR, waitListReservations);
        else
            SerializeDB.writeSerializedObject(RESERVATION_DATA_DIR, reservationList);
        return reservation;
    }

    /**
     * Update number of guests of the specified reservation.
     *
     * @param reservation  The specified reservation.
     * @param updatedAdult New number of adults.
     * @param updatedChild New number of children.
     * @return The reservation after updated.
     */
    public Reservation updateNumberOfGuests(Reservation reservation, int updatedAdult, int updatedChild) {
        reservation.setNumAdult(updatedAdult);
        reservation.setNumChild(updatedChild);
        if(reservation instanceof WaitListReservation)
            SerializeDB.writeSerializedObject(WAITING_RESERVATION_DATA_DIR, waitListReservations);
        else
            SerializeDB.writeSerializedObject(RESERVATION_DATA_DIR, reservationList);
        return reservation;
    }

    /**
     * Update desired check-in time of the specified pending/confirmed reservation if there are valid rooms for the
     * new check-in time, might have to change room number of this reservation if current room does not satisfied new
     * check-in time.
     *
     * @param reservation        The pending/confirm reservation.
     * @param updatedCheckInTime Newly desired check-in time.
     * @return The reservation after updated.
     * @throws IllegalChangeOfDateException When the specified reservation is not a pending or confirmed reservation.
     * @throws InvalidDatePairException when the keyed in check in time is later than the expected check out time.
     */
    public Reservation updateCheckInTime(Reservation reservation, LocalDateTime updatedCheckInTime) throws IllegalChangeOfDateException, InvalidDatePairException {
        Room currentRoom = roomController.findRoomByRoomNumber(reservation.getRoomNum());
        List<Room> similarRooms =
                roomController.findRoomByFacing(roomController.findRoomByBedType(roomController.findRoomByType(currentRoom.getRoomType().toString()),
                        currentRoom.getRoomBedType().toString()), currentRoom.getRoomFacing().toString());
        List<Room> validSimilarRooms = roomController.checkAvailableRooms(similarRooms, updatedCheckInTime,
                reservation.getCheckOutTime());
        if (validSimilarRooms.size() == 0) {
            return reservation;
        } else if (validSimilarRooms.contains(currentRoom)) {
            reservation.setCheckInTime(updatedCheckInTime);
            return reservation;
        } else {
            Room newRoom = validSimilarRooms.get(0); // assign first valid similar rooms due to change of check-in
            // time.

            reservation.setCheckInTime(updatedCheckInTime);
            reservation.setRoomNum(newRoom.getRoomNumber());
            roomController.reserve(newRoom.getRoomNumber());

            return reservation;
        }
    }

    /**
     * Update desired check-out time of the specified pending/confirmed reservation if there are valid rooms for the
     * new check-out time, might have to change room number of this reservation if current room does not satisfied new
     * check-out time.
     *
     * @param reservation         The pending/confirm reservation.
     * @param updatedCheckOutTime Newly desired check-out time.
     * @return The reservation after updated.
     * @throws IllegalChangeOfDateException When the specified reservation is not a pending or confirmed reservation.
     * @throws InvalidDatePairException when the keyed in check out time is earlier than the expected check in time.
     */
    public Reservation updateCheckOutTime(Reservation reservation, LocalDateTime updatedCheckOutTime) throws IllegalChangeOfDateException, InvalidDatePairException {
        Room currentRoom = roomController.findRoomByRoomNumber(reservation.getRoomNum());
        List<Room> similarRooms =
                roomController.findRoomByFacing(roomController.findRoomByBedType(roomController.findRoomByType(currentRoom.getRoomType().toString()),
                        currentRoom.getRoomBedType().toString()), currentRoom.getRoomFacing().toString());
        List<Room> validSimilarRooms = roomController.checkAvailableRooms(similarRooms, reservation.getCheckInTime(),
                updatedCheckOutTime);
        if (validSimilarRooms.size() == 0) {
            return reservation;
        } else if (validSimilarRooms.contains(currentRoom)) {
            reservation.setCheckOutTime(updatedCheckOutTime);
            return reservation;
        } else {
            Room newRoom = validSimilarRooms.get(0); // assign first valid similar rooms due to change of check-out
            // time.
            reservation.setCheckOutTime(updatedCheckOutTime);
            reservation.setRoomNum(newRoom.getRoomNumber());
            roomController.reserve(newRoom.getRoomNumber());

            return reservation;
        }
    }

    /**
     * Search for reservations with the specified contact number.
     *
     * @param contact - guest contact to find
     * @return A list of reservations with the specified contact number.
     */
    public List<Reservation> searchReservationsByContact(String contact) {
        List<Reservation> reservations = new ArrayList<>();
        for (Reservation reservation : reservationList) {
            if (reservation.getGuest().getContact().equals(contact)) {
                reservations.add(reservation);
            }
        }
        return reservations;
    }

    /**
     * Search for reservations with the specified reservation code.
     *
     * @param reservationCode - reservation code to find
     * @return A list of reservations with the specified reservation code.
     */
    public List<Reservation> searchReservationsByCode(UUID reservationCode) {
        List<Reservation> reservations = new ArrayList<>();
        for (Reservation reservation : reservationList) {
            if (reservation.getReservationCode().equals(reservationCode)) {
                reservations.add(reservation);
            }
        }
        return reservations;
    }

    /**
     * Replace a specified reservation with another reservation in the reservations' container and update the database.
     *
     * @param oldReservation Reservation to be removed from the reservations' container.
     * @param newReservation Reservation to be added to the reservations' container.
     */
    private void updateReservation(Reservation oldReservation, Reservation newReservation) {
        reservationList.remove(oldReservation);
        reservationList.add(newReservation);
        SerializeDB.writeSerializedObject(RESERVATION_DATA_DIR, reservationList);
    }

    /**
     * Move a reservation from the wait list to the confirmed list and update the database.
     *
     * @param candidateReservation The WaitListReservation to be moved.
     * @return The ConfirmedReservation.
     */
    public ConfirmedReservation confirmWaitListReservation(WaitListReservation candidateReservation) {
        ConfirmedReservation confirmedReservation = candidateReservation.confirm(candidateReservation.getRoomNum());
        waitListReservations.remove(candidateReservation);
        reservationList.add(confirmedReservation);
        SerializeDB.writeSerializedObject(RESERVATION_DATA_DIR, reservationList);
        SerializeDB.writeSerializedObject(WAITING_RESERVATION_DATA_DIR, waitListReservations);
        return confirmedReservation;
    }

    /**
     * Search reservations in waitList corresponding to a certain room.
     *
     * @param room The specified room.
     * @return List of reservations in waitList corresponding to a certain room.
     */
    public List<WaitListReservation> searchWaitListReservationByRoom(Room room) {
        ArrayList<WaitListReservation> targetWaitList = new ArrayList<>();
        for (WaitListReservation reservation : waitListReservations) {
            if (reservation.getRoomNum().equals(room.getRoomNumber())) {
                targetWaitList.add(reservation);
            }
        }
        return targetWaitList;
    }

    /**
     * Get the non-waitlisted reservations' list.
     *
     * @return A List of all non-waitlisted reservations.
     */
    public List<Reservation> getAllReservations() {
        return reservationList;
    }

    /**
     * Get all waitlisted reservations.
     *
     * @return A List of all waitlisted reservations.
     */
    public List<WaitListReservation> getAllWaitingReservations() {
        return waitListReservations;
    }

    /**
     * Remove a specified reservation from the non-waitlisted reservations' container and update the database.
     *
     * @param reservation The reservation to be removed.
     */
    public void removeReservation(Reservation reservation) {
        reservationList.remove(reservation);
        SerializeDB.writeSerializedObject(RESERVATION_DATA_DIR, reservationList);
    }

    public void deleteReservationAfterCheckingOutByRoom(Room room) throws IllegalRoomInSerializableBinaryFileException {
        CheckedInReservation targetCheckedInReservation = findCheckedInReservationByRoom(room);
        if (targetCheckedInReservation != null) {
            reservationController.removeReservation(targetCheckedInReservation);
        } else {
            throw new IllegalRoomInSerializableBinaryFileException();
        }
    }

    /**
     * Find checked-in reservation of a specified room.
     *
     * @param room Room to be searched.
     * @return A CheckedInReservation if guests of this room have checked in, null if guests have not checked in or
     * no reservation was made to this room.
     */
    public CheckedInReservation findCheckedInReservationByRoom(Room room) {
        for (Reservation reservation : reservationList) {
            if (reservation instanceof CheckedInReservation && reservation.getRoomNum().equals(room.getRoomNumber())) {
                return (CheckedInReservation) reservation;
            }
        }
        return null;
    }

    /**
     * Find confirmed reservation of a specified room.
     *
     * @param room Room to be searched.
     * @return A List of ConfirmedReservation whose assigned room is the specified room.
     */
    public List<ConfirmedReservation> findConfirmedInReservationByRoom(Room room) {
        List<ConfirmedReservation> confirmedReservations = new ArrayList<>();
        for (Reservation reservation : reservationList) {
            if (reservation instanceof ConfirmedReservation && reservation.getRoomNum().equals(room.getRoomNumber())) {
                confirmedReservations.add((ConfirmedReservation) reservation);
            }
        }
        return confirmedReservations;
    }

    /**
     * Get date-time from user's inputs in a valid format.
     *
     * @return The LocalDateTime object representing the user's inputted date-time.
     * @throws InvalidDateTimeFormatException When user's input is not in format yyyy-MM-dd HH:mm.
     */
    private LocalDateTime getValidDateTime() throws InvalidDateTimeFormatException {   // check input format yyyy-MM-dd HH:mm
        Scanner in = new Scanner(System.in);
        String inputDateTime = in.nextLine();
        if (!inputDateTime.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")) {
            throw new InvalidDateTimeFormatException();
        }
        inputDateTime = inputDateTime.trim().replace(" ", "T");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime formatDateTime = LocalDateTime.parse(inputDateTime, formatter);
        return formatDateTime;
    }

    /**
     * Check if actual check in date time is 24 hours later than expected check in date time, if so, this reservation will be expired.
     * else will perform check in for this confirmed reservation.
     *
     * @param reservation           the reservation to check in or expire.
     * @param actualCheckInDateTime actual check in date time.
     * @throws InvalidStatusChangeException when the checking in this reservation is not from a
     * legal status change. (for instance, checking in a waiting reservation is not allowed)
     */
    public void validateCheckIn(Reservation reservation, LocalDateTime actualCheckInDateTime) throws InvalidStatusChangeException {
        RoomController roomController = RoomController.getInstance();
        LocalDateTime expectedCheckInDateTime = reservation.getCheckInTime();
        if (actualCheckInDateTime.isAfter(expectedCheckInDateTime.plus(Duration.ofHours(24)))) {
            ExpiredReservation expiredReservation = reservation.cancel();
            System.out.println(expiredReservation.toString());
            roomController.checkOutRoom(roomController.findRoomByRoomNumber(reservation.getRoomNum()), expectedCheckInDateTime.plus(Duration.ofHours(24)));
            // if the actual check in time is 24 hours later than the expected check in time, it
            // will be expired via the logic modelling of checkOutRoom(expectedCheckInTime + 24
            // hours))
            removeReservation(reservation);
        } else {
            CheckedInReservation checkedInReservation = reservation.checkIn(actualCheckInDateTime);
            roomController.checkIn(reservation.getRoomNum(), reservation.getGuest());
            updateReservation(reservation, checkedInReservation);
            System.out.println("Check in successfully for this reservation!");
        }
    }
}


