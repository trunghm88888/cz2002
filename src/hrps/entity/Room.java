package hrps.entity;

import hrps.entity.enums.RoomBedType;
import hrps.entity.enums.RoomFacing;
import hrps.entity.enums.RoomStatus;
import hrps.entity.enums.RoomType;

import java.io.Serializable;

/**
 * This class represents rooms in the hotel, with attributes room number, room type, bed type, facing direction,
 * status of vacancy, has wi-fi? is smoking-free?, rate per day and the currently staying guest.
 *
 * @author An Ruyi
 */
public class Room implements Serializable {
    /**
     * This room's number, unique for each room.
     */
    private final String roomNumber;
    /**
     * This room's type.
     */
    private RoomType roomType;
    /**
     * This room's bed type.
     */
    private RoomBedType roomBedType;
    /**
     * This room's facing direction.
     */
    private RoomFacing roomFacing;
    /**
     * This room's status of vacancy.
     */
    private RoomStatus roomStatus;
    /**
     * true = This room has wi-fi, false = This room does not have it.
     */
    private boolean hasWiFi;
    /**
     * true = This room is smoking-free, false = This room is not.
     */
    private boolean isSmokingFree;
    /**
     * This room's base rate.
     */
    private double rate;
    /**
     * This room's currently staying guest, null if no one is staying in this room.
     */
    private Guest currentGuest;


    /**
     * Main constructor.
     *
     * @param roomNumber    Room number, used to identify each room.
     * @param roomType      Enum constant represents room type.
     * @param roomBedType   Enum constant represents room's bed type.
     * @param roomFacing    Enum constant represents room facing direction.
     * @param roomStatus    Enum constant represents room's booking status.
     * @param hasWiFi       true = this room has Wi-fi, false = this room does not have Wi-fi.
     * @param isSmokingFree true = this room is smoking-free, false = this room is not smoking-free.
     * @param rate          Room basic price per day.
     */
    public Room(String roomNumber, RoomType roomType, RoomBedType roomBedType,
                RoomFacing roomFacing, RoomStatus roomStatus,
                boolean hasWiFi, boolean isSmokingFree, double rate) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.roomBedType = roomBedType;
        this.roomFacing = roomFacing;
        this.roomStatus = roomStatus;
        this.hasWiFi = hasWiFi;
        this.isSmokingFree = isSmokingFree;
        this.rate = rate;
    }

    /**
     * Get this room's number.
     *
     * @return A String representing this room number.
     */
    public String getRoomNumber() {
        return roomNumber;
    }

    /**
     * Get this room's guest.
     *
     * @return A Guest object representing this room's current guest.
     */
    public Guest getCurrentGuest() {
        return currentGuest;
    }

    /**
     * Get this room's type.
     *
     * @return A RoomType enum constant representing this room's type.
     */
    public RoomType getRoomType() {
        return roomType;
    }

    /**
     * Get this room's bed type.
     *
     * @return A RoomBedType enum constant representing this room's bed type.
     */
    public RoomBedType getRoomBedType() {
        return roomBedType;
    }

    /**
     * Get this room's facing direction.
     *
     * @return A RoomFacing enum constant representing this room's facing direction.
     */
    public RoomFacing getRoomFacing() {
        return roomFacing;
    }

    /**
     * Get this room's booking status.
     *
     * @return A RoomStatus enum constant representing this room's booking status.
     */
    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    /**
     * Check if this room has Wi-fi.
     *
     * @return true = this room has Wi-fi, false = this room does not have Wi-fi.
     */
    public boolean isHasWiFi() {
        return hasWiFi;
    }

    /**
     * Check if this room is smoking-free.
     *
     * @return true = this room is smoking-free, false = this room is not smoking-free.
     */
    public boolean isSmokingFree() {
        return isSmokingFree;
    }

    /**
     * Get the price per day of this room.
     *
     * @return A double representing the price per day of this room.
     */
    public double getRate() {
        return rate;
    }

    /**
     * Change the current guest of this room.
     *
     * @param currentGuest New guest.
     */
    public void setCurrentGuest(Guest currentGuest) {
        this.currentGuest = currentGuest;
    }

    /**
     * Change the booking status of this room.
     *
     * @param status New status.
     */
    public void setRoomStatus(RoomStatus status) {
        roomStatus = status;
    }

    /**
     * Change the rate of this room.
     *
     * @param rate New rate.
     */
    public void setRate(double rate) {
        this.rate = rate;
    }

    /**
     * Get a table that contains all information about this room.
     *
     * @return A String contains all information about this room.
     */
    @Override
    public String toString() {
        String[][] table = new String[8][];
        table[0] = new String[]{"Room Number:", roomNumber};
        table[1] = new String[]{"Room Type", roomType.toString()};
        table[2] = new String[]{"Room Bed Type", roomBedType.toString()};
        table[3] = new String[]{"Room Facing", roomFacing.toString()};
        table[4] = new String[]{"Room Status", roomStatus.toString()};
        table[5] = new String[]{"Wifi", hasWiFi ? "Yes" : "No"};
        table[6] = new String[]{"Is Smoking Free", isSmokingFree ? "Yes" : "No"};
        table[7] = new String[]{"Rate", String.format("%.2f", rate)};
        String separator = "----------------------+-----------------------\n";
        StringBuilder display = new StringBuilder(separator);
        for (String[] row : table) {
            display.append(String.format("| %-20s|  %-20s|%n", row));
            display.append(separator);
        }
        return display.toString();
    }
}