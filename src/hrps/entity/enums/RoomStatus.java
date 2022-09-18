package hrps.entity.enums;

/**
 * All types of a room's status.
 */
public enum RoomStatus {
    /**
     * VACANT means that NO booking for ANY time has been made on this room.
     */
    VACANT,
    /**
     * There are guest(s) currently staying in room.
     */
    OCCUPIED,
    /**
     * Room is booked for some times.
     */
    RESERVED,
    /**
     * Room is under maintenance. We cannot book a room with this.
     */
    MAINTENANCE
}

