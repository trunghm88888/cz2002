package hrps.control;

import hrps.entity.OrderedMenuItem;
import hrps.entity.OrderedRoomService;
import hrps.entity.Room;
import hrps.entity.enums.OrderStatus;
import tool.SerializeDB;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class represents the controller for room service orders, includes create, update, search for orders and
 * calculate prices of orders.
 *
 * @author Peng Wenxuan
 */
public class OrderController {
    /**
     * Orders data file's path.
     */
    private static final String ORDER_DATA_DIR = "src/data/order.dat";
    /**
     * Delivery time of an order.
     */
    private static final int DELIVERY_TIME = 60;
    /**
     * Preparing time of an order.
     */
    private static final int PREPARING_TIME = 30;
    /**
     * List of orders that are being handled.
     */
    private final List<OrderedRoomService> orderList;
    /**
     * The static controller instance to avoid multiple instantiations.
     */
    private static OrderController instance = null;

    /**
     * Get the controller instance. This is to avoid multiple instantiation of controller.
     *
     * @return The singleton OrderController instance.
     */
    public static OrderController getInstance() {
        if (Objects.isNull(instance)) {
            instance = new OrderController();
        }
        return instance;
    }

    /**
     * Initialize OrderController by reading from orders data file, if the data file does not exist at the given
     * path, create it and write the on-memory orders data to the newly created file.
     */
    @SuppressWarnings("unchecked")
    private OrderController() {
        File file = new File(ORDER_DATA_DIR);
        if (file.exists()) {
            orderList = (List<OrderedRoomService>) SerializeDB.readSerializedObject(ORDER_DATA_DIR);
        } else {
            file.getParentFile().mkdir();
            orderList = new ArrayList<>();
            SerializeDB.writeSerializedObject(ORDER_DATA_DIR, orderList);
        }
    }

    /**
     * Create a room service order with the specified list of item's orders.
     *
     * @param room    To-be-delivered-to room.
     * @param remarks Guest's remark.
     * @param items   The specified list of item's orders
     */
    public void makeOrder(Room room, String remarks, List<OrderedMenuItem> items) {
        OrderedRoomService order = new OrderedRoomService(room, LocalDateTime.now(), remarks, OrderStatus.CONFIRMED, items);
        orderList.add(order);
        SerializeDB.writeSerializedObject(ORDER_DATA_DIR, orderList);
    }

    /**
     * Changes the order status to the specified status given in the parameter.
     *
     * @param order  The order to be modified
     * @param status The new status of the order
     */
    public void changeOrderStatus(OrderedRoomService order, OrderStatus status) {
        order.setOrderStatus(status);
        updateOrder();
    }

    /**
     * Update order's status and update the database.
     */
    public void updateOrder() {
        //updateOrderStatus();
        SerializeDB.writeSerializedObject(ORDER_DATA_DIR, orderList);
    }

    /**
     * Get orders of a specific room.
     *
     * @param room The specific room.
     * @return A list contains all orders that were made/delivered to this room.
     */
    public List<OrderedRoomService> getRoomOrder(Room room) {
        updateOrder();
        ArrayList<OrderedRoomService> orders = new ArrayList<>();
        for (OrderedRoomService order : orderList) {
            if (order.getRoom().getRoomNumber().equals(room.getRoomNumber())) {
                orders.add(order);
            }
        }
        return orders;
    }

    /**
     * Get total price of all orders made/delivered to the specified room.
     *
     * @param room         The specified room.
     * @param checkinTime  The checked-in time of the guest in the specified room.
     * @param checkoutTime The checked-out time of the guest in the specified room.
     * @return A double representing total price of all orders of the specified room.
     */
    public double getTotalPrice(Room room, LocalDateTime checkinTime, LocalDateTime checkoutTime) {
        double totalPrice = 0;
        for (OrderedRoomService order : orderList) {
            if (order.getRoom().equals(room)) {
                if (Duration.between(checkinTime, order.getOrderTime()).toSeconds() > 0 &&
                        Duration.between(order.getOrderTime(), checkoutTime).toSeconds() > 0) {
                    totalPrice = order.getTotalPrice();
                }
            }
        }
        return totalPrice;
    }

    /**
     * Get the first order in the order list that was made/delivered to the specified room.
     *
     * @param targetRoom The specified room.
     * @return An OrderRoomService of the specified room and return null if there is no order.
     */
    public double findOrderedRoomServiceTotalPriceByRoom(Room targetRoom) {
        OrderedRoomService orders = null;
        for (OrderedRoomService orderedRoomService : orderList) {
            if (orderedRoomService.getRoom().getRoomNumber().equals(targetRoom.getRoomNumber())) {
                orders = orderedRoomService;
            }
        }
        if (orders == null) {
            return 0;
        } else {
            return orders.getTotalPrice();
        }
    }

    /**
     * Remove all room orders when this room is being checked out.
     *
     * @param roomToCheckOut the room to check out
     */
    public void flushRoomOrderAfterCheckOut(Room roomToCheckOut) {
        // fix concurrentmodification
        List<OrderedRoomService> toRemove = new ArrayList<>();
        for (OrderedRoomService roomOrder : orderList) {
            if (roomOrder.getRoom().getRoomNumber().equals(roomToCheckOut.getRoomNumber())) {
                toRemove.add(roomOrder);
            }
        }
        for (OrderedRoomService ors : toRemove) {
            orderList.remove(ors);
        }
        SerializeDB.writeSerializedObject(ORDER_DATA_DIR, orderList);
    }
}
