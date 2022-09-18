package hrps.control;

import hrps.entity.*;
import hrps.entity.enums.RoomBedType;
import hrps.entity.enums.RoomFacing;
import hrps.entity.enums.RoomStatus;
import hrps.entity.enums.RoomType;
import hrps.exception.HRPSException;
import hrps.exception.InvalidDatePairException;
import hrps.exception.InvalidRoomNumberFormatException;
import hrps.exception.RoomNumberNotExistException;
import tool.SerializeDB;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

/**
 * This class represents controller for rooms, includes creating new rooms or initializing rooms from database,
 * searching, updating and checking-in guests to rooms. Each room is assign a unique room number.
 *
 * @author Peng Wenxuan, An Ruyi
 */
public class RoomController {
    /**
     * Total number of rooms.
     */
    private static final int TOTAL_ROOMS = 48;
    /**
     * List of all room objects of the system.
     */
    private final List<Room> roomList;
    /**
     * Rooms status data file's path.
     */
    private static final String ROOM_STATUS_DATA_DIR = "src/data/room_status.dat";
    /**
     * Rooms data file's path.
     */
    private static final String ROOM_LIST_DATA_DIR = "src/data/room_list.dat";
    /**
     * The static controller instance to avoid multiple instantiations.
     */
    private static RoomController RoomController = null;
    /**
     * The text file that contains hotel's rooms' structure.
     */
    private static final String ROOM_DATA_PATH = "src/hrps/entity/RoomData.txt";
    /**
     * ArrayList contains statuses of all rooms, with same indexes as indexes of rooms in roomList.
     */
    private final ArrayList<RoomStatus> roomStatusList;   // a dat file only to save all room status
    /**
     * Room controller works closely with Reservation controller.
     */
    private static final ReservationController reservationController = ReservationController.getInstance();

    /**
     * Initialize RoomController by reading from rooms status data file, if the data file does not exist at the
     * path, create it and write the on-memory rooms' status data to the newly created file.
     */
    @SuppressWarnings("unchecked")
    private RoomController() {
        File file = new File(ROOM_STATUS_DATA_DIR);
        if (file.exists()) {
            roomStatusList = (ArrayList<RoomStatus>) SerializeDB.readSerializedObject(ROOM_STATUS_DATA_DIR);    //
            // ArrayList
            // implements serializable by default
        } else {
            file.getParentFile().mkdir();
            roomStatusList = new ArrayList<>(TOTAL_ROOMS);
            for (int i = 0; i < TOTAL_ROOMS; i++) {
                roomStatusList.add(RoomStatus.VACANT);      // initialize all rooms to be vacant when first creating
                // all rooms
            }
            SerializeDB.writeSerializedObject(ROOM_STATUS_DATA_DIR, roomStatusList);
        }

        file = new File(ROOM_LIST_DATA_DIR);
        if (file.exists()) {
            roomList = (ArrayList<Room>) SerializeDB.readSerializedObject(ROOM_LIST_DATA_DIR);
        } else {
            this.roomList = new ArrayList<>(TOTAL_ROOMS);
            file.getParentFile().mkdir();
            SerializeDB.writeSerializedObject(ROOM_LIST_DATA_DIR, roomList);
        }
    }

    /**
     * Get the RoomController instance. This is to avoid multiple instantiation of controller.
     *
     * @return The singleton RoomController instance.
     */
    public static RoomController getInstance() {
        if (Objects.isNull(RoomController)) {
            RoomController = new RoomController();
        }
        return RoomController;
    }

    /**
     * Create a new room based on the given parameters.
     *
     * @param roomNumber    Room's number.
     * @param roomType      Room's type.
     * @param roomBedType   Room's bed type.
     * @param roomFacing    Room's facing direction.
     * @param roomStatus    Room's status
     * @param hasWifi       Whether room has Wi-fi or not.
     * @param isSmokingFree Whether room is smoking free or not.
     * @param rate          Room's rate.
     * @return The just-created room.
     */
    public Room newRoom(String roomNumber, String roomType, String roomBedType, String roomFacing, RoomStatus roomStatus,
                        String hasWifi, String isSmokingFree, String rate) {
        RoomType type = RoomType.valueOf(roomType.toUpperCase());
        RoomBedType bedType = RoomBedType.valueOf(roomBedType.toUpperCase());
        RoomFacing roomFace = RoomFacing.valueOf(roomFacing.toUpperCase());
        boolean targetRoomHasWifi = false;
        boolean targetRoomIsSmokingFree = false;
        double rateValue = Double.parseDouble(rate);
        if (hasWifi.equals("1") || hasWifi.toLowerCase().equals("true") || hasWifi.toLowerCase().equals("yes")) {
            targetRoomHasWifi = true;
        }
        if (isSmokingFree.equals("1") || isSmokingFree.toLowerCase().equals("true") || isSmokingFree.toLowerCase().equals("yes")) {
            targetRoomIsSmokingFree = true;
        }
        Room room = new Room(roomNumber, type, bedType, roomFace, roomStatus, targetRoomHasWifi, targetRoomIsSmokingFree, rateValue);
        return room;
    }

    /**
     * Initialize rooms from the information read from the text file, RoomData.txt, and add them to
     * the rooms list.
     *
     * @return A List of rooms.
     * @throws IOException If encounters error while reading from database.
     */
    public List<Room> initializeRooms() throws IOException {
        //read from data file
        Path path = Paths.get(ROOM_DATA_PATH);
        //read all lines
        List<String> lines = Files.readAllLines(path);
        int index = 0;
        for (String line : lines) {
            String[] description = new String[6];
            description = line.split(",");
            RoomStatus status = roomStatusList.get(index);
            index++;
            Room room = newRoom(description[0], description[1], description[2], description[3],
                    status, description[4], description[5], description[6]);
            roomList.add(room);
        }
        SerializeDB.writeSerializedObject(ROOM_LIST_DATA_DIR, roomList);
        return roomList;
    }

    /**
     * Find the room with the specified room number.
     *
     * @param roomNumber The specified room number.
     * @return Return the room with the specified room number if it exists in the rooms' container,
     * null otherwise.
     */
    public Room findRoomByRoomNumber(String roomNumber) {
        for (Room room : roomList) {
            if (room.getRoomNumber().equals(roomNumber)) {
                return room;
            }
        }
        return null;
    }

    /**
     * Find the rooms with the specified guest's contact.
     *
     * @param contact The specified guest's contact.
     * @return A List of rooms with the specified guest's contact.
     */
    public List<Room> findRoomsByGuest(String contact) {
        List<Room> targetRooms = new ArrayList<>();
        for (Room room : roomList) {
            if (!Objects.isNull(room.getCurrentGuest()))
                if (room.getCurrentGuest().getContact().equals(contact)) {
                    targetRooms.add(room);
                }
        }
        return targetRooms;
    }

    /**
     * Reserve a room by finding it with the specified room number and setting its status to RESERVED and update the
     * data file.
     *
     * @param roomNumber The specified room number.
     */
    public void reserve(String roomNumber) {
        Room room = findRoomByRoomNumber(roomNumber);
        room.setRoomStatus(RoomStatus.RESERVED);
        int roomIndex = roomList.indexOf(room);
        roomStatusList.set(roomIndex, RoomStatus.RESERVED);
        SerializeDB.writeSerializedObject(ROOM_STATUS_DATA_DIR, roomStatusList);
    }

    /**
     * Find a room's index in the system's rooms list.
     *
     * @param room The room to be found.
     * @return A int representing the index of the specified room in the system's rooms list.
     */
    public int findRoomIndexInRoomList(Room room) {
        return roomList.indexOf(room);
    }

    /**
     * Find the occupied rooms with the specified guest's contact.
     *
     * @param guest The specified guest's contact.
     * @return A List of occupied rooms with the specified guest's contact.
     */
    public List<Room> findOccupiedRoomsByGuest(Guest guest) {
        List<Room> targetRooms = new ArrayList<>();
        for (Room room : roomList) {
            if (room.getRoomStatus() == RoomStatus.OCCUPIED && room.getCurrentGuest().getContact().equals(guest.getContact())) {
                targetRooms.add(room);
            }
        }
        return targetRooms;
    }

    /**
     * Check if the room with the specified room number can be ordered room service.
     *
     * @param roomNumber The specified room number.
     * @return true if this room can be ordered room service, false if it cannot be.
     */
    public boolean canBeOrdered(String roomNumber) {    // check if the roomNumber is valid for
        // ordering (room number exist and room status occupied)
        if (findRoomByRoomNumber(roomNumber) == null) {   // room number not exist
            return false;
        }
        if (!findRoomByRoomNumber(roomNumber).getRoomStatus().toString().equals("OCCUPIED")) {   // room not in occupied status
            return false;
        }
        return true;
    }

    /**
     * Check in the specified guest into the room with the specified room number.
     *
     * @param roomNumber The specified room number.
     * @param guest      The specified guest.
     * @return The just-checked-in room.
     */
    //check in/out
    public Room checkIn(String roomNumber, Guest guest) {
        Room room = findRoomByRoomNumber(roomNumber);
        room.setRoomStatus(RoomStatus.OCCUPIED);
        room.setCurrentGuest(guest);
        int roomIndex = findRoomIndexInRoomList(room);
        roomStatusList.set(roomIndex, RoomStatus.OCCUPIED);  // update serializable
        SerializeDB.writeSerializedObject(ROOM_STATUS_DATA_DIR, roomStatusList);
        SerializeDB.writeSerializedObject(ROOM_LIST_DATA_DIR, roomList);
        return room;
    }

    /**
     * Maintain a room by setting its status to MAINTENANCE, ONLY IF its current status is VACANT.
     *
     * @param roomNumber Number of the room to be accessed for maintenance.
     * @return The Room with the above room number, after being accessed for maintenance.
     */
    public Room maintain(String roomNumber) {
        Room targetRoom = findRoomByRoomNumber(roomNumber);
        if (targetRoom.getRoomStatus().equals(RoomStatus.OCCUPIED) ||
                targetRoom.getRoomStatus().equals(RoomStatus.RESERVED) ||
                targetRoom.getRoomStatus().equals(RoomStatus.MAINTENANCE)) {
            return targetRoom;
        }
        targetRoom.setRoomStatus(RoomStatus.MAINTENANCE);
        return targetRoom;
    }

    /**
     * Finish maintaining a room and set its status to VACANT.
     *
     * @param roomNumber The room number of the room to finish maintaining.
     * @return The room with the above room number, after finish maintaining.
     */
    public Room finishMaintain(String roomNumber) {
        Room targetRoom = findRoomByRoomNumber(roomNumber);
        targetRoom.setRoomStatus(RoomStatus.VACANT);
        return targetRoom;
    }

    /**
     * Check if a room is being maintained.
     *
     * @param roomNumber The room number to be checked.
     * @return true if the room status is able to accessed and the status is MAINTENANCE, false otherwise.
     */
    public boolean isMaintaining(String roomNumber) {
        Room targetRoom = findRoomByRoomNumber(roomNumber);
        if (targetRoom.getRoomStatus().equals(RoomStatus.MAINTENANCE)) {
            return true;
        }
        return false;
    }


    /**
     * Find all rooms that are VACANT from a List of rooms.
     *
     * @param rooms List of rooms to be searched.
     * @return A List of all rooms from the specified list that are VACANT.
     */
    public List<Room> checkAvailableRooms(List<Room> rooms) {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getRoomStatus() == RoomStatus.VACANT) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    /**
     * Finds all rooms that are available for a period of stay between a check-in date and a check-out date, from a
     * given List of rooms. VACANT rooms are automatically qualified as they do not have any booking, for each
     * RESERVED or OCCUPIED room, this method checks if the above staying period could fit in the VACANT period of
     * the room.
     *
     * @param rooms                List of rooms to be searched.
     * @param expectedCheckInDate  The expected check in date.
     * @param expectedCheckOutDate The expected check out date.
     * @return A List of all rooms from the specified list that are VACANT for the above period check-in - check-out
     * date.
     * @throws InvalidDatePairException If the specified check in date is after the specified check out date.
     */
    public List<Room> checkAvailableRooms(List<Room> rooms, LocalDateTime expectedCheckInDate,
                                          LocalDateTime expectedCheckOutDate) throws InvalidDatePairException {
        if (expectedCheckInDate.isAfter(expectedCheckOutDate)) {
            throw new InvalidDatePairException();
        }
        List<Room> availableRooms = new ArrayList<>();
        ReservationController reservationController = ReservationController.getInstance();
        for (Room room : rooms) {
            if (room.getRoomStatus() == RoomStatus.VACANT) {
                availableRooms.add(room);
            } else if (room.getRoomStatus() == RoomStatus.RESERVED || room.getRoomStatus() == RoomStatus.OCCUPIED) {
                List<ConfirmedReservation> reservations =
                        reservationController.findConfirmedInReservationByRoom(room);
                boolean hasNoConflictWithCurrentReservation = true;
                for (ConfirmedReservation reservation : reservations) {
                    LocalDateTime start = reservation.getCheckInTime();
                    LocalDateTime end = reservation.getCheckOutTime();
                    if (!(expectedCheckInDate.isAfter(end) || expectedCheckOutDate.isBefore(start))) {
                        // if the expected check in date is after the check out date, or the expected check out date
                        // i.e. not within the reservation period, then the room is not available.
                        hasNoConflictWithCurrentReservation = false;
                    }
                }
                // fix an error where can confirm a reservation whose check-in time in overlaps with occupied period
                // of a room
                if (room.getRoomStatus() == RoomStatus.OCCUPIED) {
                    CheckedInReservation cur = reservationController.findCheckedInReservationByRoom(room);
                    if (!(expectedCheckInDate.isAfter(cur.getCheckOutTime())))
                        hasNoConflictWithCurrentReservation = false;
                }
                if (hasNoConflictWithCurrentReservation) {
                    availableRooms.add(room);
                }
            }
        }
        return availableRooms;
    }

    /**
     * Changes the status of the room. When a room is checking out, if there is future reservation
     * or waitlist reservation become available to reserve, set it to reserved. Else, vacant.
     *
     * @param room               The room to be checked out.
     * @param actualCheckOutDate the actual check out date of the room.
     * @return true if the room is set to vacant, false otherwise.
     */
    public boolean checkOutRoom(Room room, LocalDateTime actualCheckOutDate) {
        // if there is future confirmed reservation, set it to reserved
        List<ConfirmedReservation> reservationsOfTheRoom =
                reservationController.findConfirmedInReservationByRoom(room);
        boolean isVacant = true;
        boolean isReserved = false;
        ConfirmedReservation currentlyReservedReservation = null;
        for (ConfirmedReservation reservation : reservationsOfTheRoom) {
            if (reservation.getCheckInTime().isAfter(actualCheckOutDate)) {
                // out date
                room.setRoomStatus(RoomStatus.RESERVED);
                isVacant = false;
                isReserved = true;
                currentlyReservedReservation = reservation;
            }
        }

        // if there is anything reservation on the room on the waiting list
        List<WaitListReservation> waitListReservations =
                reservationController.searchWaitListReservationByRoom(room);
        for (WaitListReservation reservation : waitListReservations) {
            if (isReserved) { // see if the waitlist is at the gap of current time and the current reservation
                if (reservation.getCheckOutTime().isBefore(currentlyReservedReservation.getCheckInTime()) &&
                        reservation.getCheckInTime().isAfter(actualCheckOutDate)) {
                    room.setRoomStatus(RoomStatus.RESERVED);
                    currentlyReservedReservation = reservationController.confirmWaitListReservation(reservation);
                    isVacant = false;
                }
            } else {
                // no future reservation from confirmedReservation, but see if we can turn
                // anything waitListReservation to confirmedReservation
                if (reservation.getCheckInTime().isAfter(actualCheckOutDate)) {
                    room.setRoomStatus(RoomStatus.RESERVED);
                    currentlyReservedReservation = reservationController.confirmWaitListReservation(reservation);
                    isVacant = false;
                }
            }
        }
        if (isVacant) {
            room.setRoomStatus(RoomStatus.VACANT);
        }
        return isVacant;
    }

    /**
     * Find rooms with the specified facing direction from a list of rooms.
     *
     * @param rooms  List of rooms to be searched.
     * @param facing The specified facing direction.
     * @return A List of all rooms from the specified list that have the specified facing direction.
     */
    public List<Room> findRoomByFacing(List<Room> rooms, String facing) {
        List<Room> targetRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getRoomFacing().toString().equals(facing.toUpperCase())) {
                targetRooms.add(room);
            }
        }
        return targetRooms;
    }

    /**
     * Find rooms with the specified status from a list of rooms.
     *
     * @param rooms  List of rooms to be searched.
     * @param status The specified status.
     * @return A List of all rooms from the specified list that have the specified status.
     */
    public List<Room> findRoomByStatus(List<Room> rooms, String status) {
        List<Room> targetRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getRoomStatus().toString().equals(status.toUpperCase())) {
                targetRooms.add(room);
            }
        }
        return targetRooms;
    }

    /**
     * Find rooms with the specified type.
     *
     * @param type The specified type.
     * @return A List of all rooms that have the specified type.
     */
    public List<Room> findRoomByType(String type) {
        List<Room> targetRooms = new ArrayList<>();
        RoomType roomType = RoomType.valueOf(type.toUpperCase());
        for (Room room : roomList) {
            if (room.getRoomType() == roomType) {
                targetRooms.add(room);
            }
        }
        return targetRooms;
    }

    /**
     * Find rooms with the specified bed type from a list of rooms.
     *
     * @param rooms   List of rooms to be searched.
     * @param bedType The specified bed type.
     * @return A List of all rooms from the specified list that have the specified bed type.
     */
    public List<Room> findRoomByBedType(List<Room> rooms, String bedType) {
        List<Room> targetRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getRoomBedType().toString().equals(bedType.toUpperCase())) {
                targetRooms.add(room);
            }
        }
        return targetRooms;
    }

    /**
     * Change the specified room's rate.
     *
     * @param room            The specified room.
     * @param updatedRoomRate New rate.
     */
    public void updateRoomRate(Room room, double updatedRoomRate) {
        room.setRate(updatedRoomRate);
    }


    /**
     * Change the specified room's status.
     *
     * @param room              The specified room.
     * @param updatedRoomStatus New status.
     */
    public void updateRoomStatus(Room room, String updatedRoomStatus) {
        RoomStatus status = RoomStatus.valueOf(updatedRoomStatus);
        room.setRoomStatus(status);
    }

    /**
     * Check if the specified String is the number of a room that actually exists in system.
     *
     * @param roomNumber The specified String.
     * @return true if the specified String actually represents an existed room.
     * @throws InvalidRoomNumberFormatException if the specified String is not a legal room number.
     * @throws RoomNumberNotExistException      if the specified String is not an existed room number.
     */
    public boolean isValidRoomNumber(String roomNumber) throws HRPSException {
        if (!roomNumber.matches("[0-9]{2}-[0-9]{2}")) {
            throw new InvalidRoomNumberFormatException();
        }
        if (Objects.isNull(findRoomByRoomNumber(roomNumber))) {
            throw new RoomNumberNotExistException();
        }
        return true;
    }

    /**
     * Get the occupancy report of each room type.
     *
     * @return A String representing the occupancy report of each room type.
     */
    public String getRoomOccupancyReport() {
        StringBuilder report = new StringBuilder();
        report.append("Room Occupancy Report By Room Types:\n");
        int total, vacant;
        List<String> vacantRoomNumbers;
        for (RoomType roomType : RoomType.values()) {
            // count vacant rooms of this room type
            total = 0;
            vacant = 0;
            vacantRoomNumbers = new ArrayList<>();

            for (Room r : roomList) {
                if (r.getRoomType() == roomType) {
                    total++;
                    if (r.getRoomStatus() == RoomStatus.VACANT) {
                        vacant++;
                        vacantRoomNumbers.add(r.getRoomNumber());
                    }
                }
            }

            report.append(roomType.toString()).append(" : ").append("Number : ").append(vacant)
                    .append(" out of ").append(total).append(" rooms are ").append("vacant.\n");
            report.append("\tRooms : ");
            StringJoiner sj = new StringJoiner(", ");
            for (String n : vacantRoomNumbers)
                sj.add(n);
            report.append(sj.toString());
            report.append(vacantRoomNumbers.size() > 0 ? "." : "").append("\n");
        }
        return report.toString();
    }

    /**
     * Get rooms numbers with each room status.
     *
     * @return A String representing rooms numbers with each room status.
     */
    public String getRoomStatusRateReport() {
        StringBuilder sb = new StringBuilder("Room Report By Current Status:\n");
        for (RoomStatus status : RoomStatus.values()) {
            sb.append(status.toString()).append(" : ");
            int counter = 0;
            List<Room> currentStatusRooms = new ArrayList<>();
            for (Room room : roomList) {
                if (room.getRoomStatus() == status) {
                    currentStatusRooms.add(room);
                }
            }
            Iterator<Room> iterator = currentStatusRooms.iterator();
            while (iterator.hasNext()) {
                sb.append(iterator.next().getRoomNumber());
                counter++;
                if (iterator.hasNext()) {
                    sb.append(", ");
                }
                if (counter % 7 == 0) {
                    sb.append("\n\t");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
