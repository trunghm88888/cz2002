package hrps.entity;

import hrps.entity.enums.OrderStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * This class represents room service's order which contains one or many orders of menu items. Each room service
 * order is assigned a unique ID, the room which ordered the service, the order's date time, guest's remark and
 * order's status.
 *
 * @author Peng Wenxuan
 */
public class OrderedRoomService implements Serializable {
    /**
     * Unique ID of this room services order.
     */
    private final UUID orderID;
    /**
     * The room to which this order is delivered.
     */
    private final Room room;
    /**
     * Time when order was placed.
     */
    private final LocalDateTime orderTime;
    /**
     * Guest's remarks on this order.
     */
    private final String remark;
    /**
     * This order's status.
     */
    private OrderStatus orderStatus;
    /**
     * List of ordered menu items in this order.
     */
    private final List<OrderedMenuItem> orderItems;

    /**
     * Get total price of this order, which is sum of all the price of orders of each item.
     *
     * @return A double representing total price.
     */
    public double getTotalPrice() {
        double totalPrice = 0;
        for (OrderedMenuItem item : orderItems) {
            totalPrice += item.getPrice();
        }
        return totalPrice;
    }

    /**
     * Create a room service order with no item order.
     *
     * @param room        The room to which the order is served.
     * @param orderTime   Time of the order.
     * @param remark      Remark of the guest.
     * @param orderStatus Status of the order.
     * @param orderItems  List of items ordered.
     */
    public OrderedRoomService(Room room, LocalDateTime orderTime, String remark,
                              OrderStatus orderStatus, List<OrderedMenuItem> orderItems) {
        this.room = room;
        this.orderTime = orderTime;
        this.remark = remark;
        this.orderStatus = orderStatus;
        this.orderItems = orderItems;
        this.orderID = UUID.randomUUID();
    }

    /**
     * Get this order's ID.
     *
     * @return A UUID value.
     */
    public UUID getOrderID() {
        return orderID;
    }

    /**
     * Get the room to which this order is served.
     *
     * @return The to-be-delivered room.
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Get the time when this order was made.
     *
     * @return LocalDateTime object representing this order's ordered time.
     */
    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    /**
     * Get the guest's mark.
     *
     * @return String representing guest's remark.
     */
    public String getRemark() {
        return remark;
    }

    /**
     * Get the status of the order
     *
     * @return An Enum constant representing this order's status.
     */
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    /**
     * Get the list of ordered items.
     *
     * @return A Collection of all the ordered items.
     */
    public List<OrderedMenuItem> getOrderItems() {
        return orderItems;
    }

    /**
     * Change this order's status.
     *
     * @param orderStatus New status.
     */
    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * Get a String representing all information about this order and its item's orders.
     *
     * @return A String representing this order.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\nMenu:\nName -> Description -> Quantity -> Price\n");
        int counter = 1;
        for (OrderedMenuItem item : orderItems) {
            sb.append(counter++).append(". \n").append(item.toString());
            if (counter < orderItems.size() + 1) {
                sb.append("\n");
            }
        }
        return String.format("Order{\nRoom number:%s\nOrder time: %s\nRemark: %s\nOrder status: %s\nOrder items: %s\n}",
                room.getRoomNumber(), orderTime, remark, orderStatus, sb.toString());
    }
}
