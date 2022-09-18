# Design principle

### 1.Use of Object-Oriented (OO) Concepts

- **Abstraction:**

Abstraction is defined as the process of reducing the object to its essence so that only the
necessary characteristics are exposed to the users. In simple terms, abstraction “displays” only the
relevant attributes of objects and “hides” the unnecessary details.

- From the list of required real-world objects in HRPS system, such as room, guest, reservation,
  menu and so on, we abstracted key entity classes. Each class has its own set of attributes for
  storing states and data, as well as methods for manipulating that data and performing useful
  actions.

  For example, the entity `Guest` abstracts all the information needed for creating the guest
  object. The controller `GuestController` contains a list of all guests and all methods for
  guest-related operations, including creating, updating and removing guest information, as well as
  receiving commands from `GuestUI` to perform corresponding actions on target guest.

- We also make use of abstract class and interface to provide a higher level of abstraction.

  For example, we make `Reservation` entity an abstract class with all the shared attributes for
  different kinds of concrete reservation entities, which abstract all required information for
  creating a reservation; We also have a `Payable` interface, which contains all methods needed to
  implement a valid payment in HRPS application.

- **Inheritance:**

Inheritance is to create new classes that are built upon existing classes by reusing methods and
fields of the parent class as well as adding new capabilities. This ensures reusability and
increasing efficiency in the code.

- In our application, we made `PendingReservation`, `ConfirmedReservation`, `CheckedInReservation`
  , `CheckedOutReservation`, and `ExpiredReservation` to inherit from `Reservation` super class,
  since they share all common attributes in `Reservation` class and some other basic functions, but
  have their own properties and unique behaviors.

  We argue that it is necessary to build these subclasses. For example, only `CheckedInReservation`
  can order room service, and only `CheckedOutReservation` can compute its bill. In addition, room
  status transfer should have certain limits. For instance, a `PendingReservation` can only be
  converted to either `ConfirmedReservation` by confirming it or `ExpiredReservation` by canceling
  it, and `CheckedInReservation` can only be converted to `CheckedOutReservation` by checking it
  out. If invalid transformation is made, we will throw certain exceptions to forbid these kinds of
  behaviors. These constraints cannot be realized by a simple change of the *"status"* attribute.

  We feature the transformation of status via instantiating other subclasses with the same UUID. For
  instance, in `PendingReservation`, we have methods `checkIn(LocalDateTime actualCheckInTime)` to
  allow status transformation by instantiating a `ConfirmedReservation` with the UUID of the
  original `PendingReservation`, then we will add the `ConfirmedReservation` to
  our `reservationList`, as well as removing `PendingReservation`, and
  perform `SerializeDB.writeSerializedObject(dir,data)` to update our stored file.


- **Encapsulation:**

Encapsulation is to build a barrier to protect an object's private data. Access to private data can
only be done by public methods like getters and setters to keep them safe from unwanted
interference.

- In our HRPS application, encapsulation is applied to all entity classes.

  For example, all attributes in `Room`, `Guest`, `Reservation`, `MenuItem` and so on are made
  private. We also made many methods to be private so that they can only be called by methods within
  the same class, in order to help hide implementation details.

  For example, in `Guest` class we have:

```java
public class Guest implements Serializable {
    private String guestName;
    private String address;
    private String country;
    private String gender;
    private String nationality;
    private String contact;
    private CreditCard creditCard;
    private ID id;
    ...
}
```

Furthermore, we have information hiding of the logics of control classes such that the boundary
classes which make use of these logics, need not know the implementation details. Instead, the
boundary class clients can just call methods provided by the control to perform the intended tasks.

- **Polymorphism:**

Polymorphism is the ability of an object to take many forms, allowing us to perform the same action
in various ways. In other words, we can reference objects of difference as a general base type, and
the behavior of this object will depend on its actual type.

- In `ReservationController`, we maintain a `reservationList` using `ArrayList<Reservation>`, and
  declare all members to be `Reservation`, but when instantiating actual reservation objects, we
  create concrete enties using `PendingReservation`, `ConfirmedReservation`, `CheckedInReservation`
  , `CheckedOutReservation` and `ExpiredReservation`.
  `Reservation` itself as an abstract class cannot be instantiated. Then when calling methods
  declared in `Reservation` superclass, those methods will actually behave in the way that specify
  in the corresponding subclass.

  For example, for the updating reservation status operation, we will first get the current status
  of the target reservation in `ReservationUI`, if its status is 'PENDING', and we want to confirm
  this pending reservation, we can call `waitingReservationStatusTransfer(reservation, 1)`
  in `ReservationController`. When executing `reservation.confirm(reservation.getRoomNum())`, we
  will actually execute the `confirm(String roomNum)` method implemented in `PendingReservation`
  class, if we wrongly call the same method implemented in other subclasses, an exception will be
  thrown.

```java
public class ReservationController() {
    private final ArrayList<Reservation> reservationList;
  ...

    public Reservation waitingReservationStatusTransfer(Reservation reservation, int choice3) throws InvalidStatusChangeException, InvalidInputChoiceException {
        Reservation newReservation = null;
        if (choice3 == 1) {   // to confim this pending reservation
            newReservation = reservation.confirm(reservation.getRoomNum());
        } else if (choice3 == 2) {   // to cancel this pending reservation
            newReservation = reservation.cancel();
        } else {
            throw new InvalidInputChoiceException();
        }
        updateReservation(reservation, newReservation);
        return newReservation;
    }
  ...
}

public class PendingReservation() {
  ...

    @Override
    public ConfirmedReservation confirm(String roomNum) {
        return new ConfirmedReservation(getNumAdult(), getNumChild(), getGuest(),
                getReservationCode(), desiredCheckInTime, desiredCheckOutTime, roomNum);
    }
  ...
}
```

- In `CheckOutController`, we generate a `Payable` object with `generatePayment()` method with
  different signatures. A payment object, which has implemented `Payable` interface, can be
  generated to the client by specifying the method arguments based on users input.

```java
public class CheckOutController {
    public Payable generatePayment(double totalAmount) {
        return new CashPayment(totalAmount);
    }

    public Payable generatePayment(double totalAmount, CreditCard creditCard) {
        return new CreditCardPayment(creditCard, totalAmount);
    }
}
```

- **Exception Handling**  (todo: add UML diagram to dsiplay all exceptions we have)

Exception Handling is a mechanism to handle runtime errors such as invalid input, removal of item
that does not exist, addition duplicate item, input of a negative amount and so on.

- In our HRPS application, we create our own exception classes to handle different types of run time
  errors that may occur when using our system. We first have an abstract class `HRPSException`, and
  many subclasses inherited from it. Each exception subclass is used to handle one specific type of
  exception, and has a specific error message catering to that type of error.

  For example, for handling negative price input, the UI will detect the invalid input and raise the
  exception of
  `InvalidPriceException`.

```java
public class InvalidPriceException extends HRPSException {
    private static final String NEGATIVE_PRICE_MESSAGE = "Price cannot be negative";

    public InvalidPriceException() {
        super(NEGATIVE_PRICE_MESSAGE);
    }
}


public class CheckOutUI {
    void run() {
        try {
            ...
        } catch (HRPSException) {
            System.out.println("Warning a detected HRPS exception is caught" + e.getMessage());
        }
    }
}
```


- **Interface**

We created the `Payable` interface to define the behavior of objects that can be paid that all
payment methods need to implement. This is to support future upgrades should there be a novel
introduction of new payment method like PayLah. We do not have to change the existing code
in `CheckOutController` and in `CheckOutUI` that deals with the payment and hence allows for easy
extensibility.

### 2.Use of Design Patterns

- **Singleton Design Pattern**

Singleton Design Pattern is basically limiting our class so that whoever is using that class can
only create 1 instance from that class.

- In our application, all controller classes such as `OrderController`,`GuestController`
  , `RoomController` and boundary classes such as `ServiceUI`, `GuestUI`, `RoomUI`, are implemented
  using the Singleton Pattern.

  For example, in `OrderController` class:

```java
public class OrderController {
    private static OrderController instance = null;
    ...

    public static OrderController getInstance() {
        if (Objects.isNull(instance)) {
            OrderController = new OrderController();
        }
        return OrderController;
    }
}
```

And when we need to have an `OrderController` object, we can just call the static method of
`OrderController.getInstance
()` to get the instance.

```java
public class ServiceUI {
    private final OrderController orderController = OrderController.getInstance();
    ...
}
```

- **Entity-Control-Boundary Design Pattern**

The ECB pattern organises the responsibilities of classes according to their role in the use-case
realization.

- In our application, we divide all our classes to three types:
    - boundary: objects that interface with system actors, take charge of receiving user commands as
      well as interacting with controllers to perform certain operation.
    - controller: objects that mediate between boundaries and entities, also take in charge of
      reading and storing information to disk.
    - entity: objects representing system data

  For example, in `ReservationUI`, if we want to change status of a pending reservation, the order
  of call will be

    - `ReservationUI`: if the target reservation status is checked to be "PENDING", ask user choice
      to "confirm" it (choice = 1) or "cancel" it (choice = 2)

      Suppose user wants to confirm it, so
      call `reservationController.WaitingReservation(reservation, 1)`

    - `ReservationController`: execute `WaitingReservation(reservation, 1)`,
      call `reservation.confirm(roomNum)`
    - `PendingReservation`: execute `confirm(roomNum)`

### 3.Use of SOLID principles

**I. Single Responsibility Principle (SRP)**

The Single Responsibility Principle states that a class should only focus on one unique task, which
facilitates easy debugging and encourages re-usability and maintainability.

Firstly ,our implementation adopts the Entity-control-boundary (E-C-B) architectural pattern
seperating classes into three layers (in 3 packages):

1. Entity: The class that contains the domain objects.
2. Control: The class that encapsulates the business logic and the business rules.
3. Boundary: The class that handles the execution of business logics and presentation logic.

- We have split the boundary classes into UI classes deal with different functions and one UI for
  the main menu. For example,
  `GuestUI` class is used to interact with the users on guest-related issue such as creating a new
  guest, updating a guest's information and deleting a guest. In the case of changing UI related to
  guest operations, we would only need to change this class.

- We have split the control classes into controllers that deals with specific domain objects. For
  exmaple, `ReservationController` deals with operations on the `Reservation` object. In the case of
  changing logic related to operations on `Reservation`, we would only need to change this class.

- Finally, we have split every possible domain objects into separate classes.

In this way, we ensure that all classes in our implementation carry their own responsibility,
reducing the coupling between classes.

**II. Open/Closed Principle (OCP)**

We applied abstraction to our implementation to ensure OCP is abided by.

- For instance, when we need to change how the fee of a reservation is computed, we would only need
  to change the `Bill` class that is in charge of computing the total fee with logics on taxes and
  promotions. We do not need to modify the client class `CheckOutUi`. An abstract layer is used to
  separate the logic from the presentation layer.

- In addition, none of the entity classes are static classes. Hence, all of them can be used as
  parent classes for extension on more specific classes as subclasses without changing the actual
  parent class.

**III. Liskov Substitution Principle (LSP)**

LSP states that derived classes must be substitutable for their base classes.

In our project, we have implemented LSP by using inheritance.
`Reservation` is a parent class for reservation with different status such as
`PendingReservation`,`ConfirmedReservation` and `CheckedInReservation`. These subclasses are
substitutable by the general abstract `Reservation` class in
`ReservationController` where these subclasses, regardless of their status, are treated as
`Reservation` objects stored in an ArrayList<Reservation>.

```java
public class ReservationController {
    private ArrayList<Reservation> reservations;
}
```

Given that we have implemented LSP, we can use call methods declared in `Reservation` class in the
client to manipulate the ArrayList<Reservation> which stores only subclass objects of `Reservation`.

**IV. Interface Segregation Principle (ISP)**

Interface Segregation principle states that classes should not depend on interfaces that they do not
use, which means that to have many interfaces, each for a specific purpose is better than having one
general interface. That being said, the classes should not be implementing an interface with methods
they do not need.

- Although both `RoomController` and `OrderController` are under the control package to handle the
  operations on `Room` and `Order` objects, they are not related to each other. Hence, it is not
  necessary to share common behaviour between them by implementing to a common interface.

- Meanwhile, we have `Payable` interface where succinct method of `pay()`, `getTotalAmount()` and
  `getPaymentDescription()` is defined. No other details are necessary for classes to implement this
  interface as we would only need these operation to the interface.

```java
public interface Payable {
    public double getTotalAmount();

    public String getPaymentDescription();

    public void pay();
}
```

With all payment methods implementing the `Payable` interface, we allows for easy extension of
additional payment methods introduced to the system like PayLah, without the need to modify existing
code in `CheckOutController`.

**V. Dependency Inversion Principle (DIP)**

DIP states that high-level modules should not depend on low-level modules, and both should depend on
abstractions.

- Our implementation uses dependency injection to achieve DIP. The classess can be injected into
  each other. For instance, in `Reservation`, we have injected `Guest` object when we instantiate
  a `Reservation` object in the constructor as shown below. We have pass in a reference of guest
  instead of creating it using `new` keyword.

```java
public class Reservation {
    private Guest guest;
    ...

    public Reservation(Guest guest, ...) {
        this.guest = guest;
        ...
    }
}
```

- Similarly, we pass in the reference of the entity object they control and use methods provided by
  those entity objects.

```java
public class GuestController {
    public void updateCreditCard(Guest guest, String updatedCreditCardNum, String updatedBillingAddress) {
        guest.setCreditCardDetails(updatedCreditCardNum, updatedBillingAddress);
    }
}
```

- And we have created many abstract methods in `Reservation` for its subclassess to override. This
  follows DIP as the superclass need not depend on the details of its subclasses.

### 4.Assumptions made in the design

In addition to those assumption mentioned in the assignment document, we have made the following
assumptions:

1. Room conditions:

    1. The hotel consists of 48 rooms in total that locates at level 2 to 7. Each level consists of
       8 rooms.
        1. At level 2-5, there are 4 double-bed rooms and 4 single-bed room each floor.
        2. At level 6, there are 8 vip rooms.
        3. At level 7-8, there are 4 deluxe rooms and 4 suite room each floor.
    2. As a total, there are 16 single rooms, 16 double rooms, 8 vip rooms, 8 deluxe rooms and 8
       suite.
    3. 4 four facing are evenly distributed among the rooms at each floor for each type.
    4. There are 2 WiFi-disabled single rooms and 2 WiFi-disabled double rooms.
    5. There are 4 non-smoking-free single rooms and 4 non-smoking-free double rooms.
2. Guests information are stored in the system, even if they have already checked out.
3. A credit card has to be given regardless of which payment method would be used during checkout.
4. Payments made are not refundable.
5. Goods & Services Tax is 7% and is applied to the subtotal after promotion deductions.
6. Room Service Orders can’t be made unless the Reservation is checked in.
7. Room status change follows a logic chain.
    1. For normal reservation process, it follows VACANT→RESERVED→OCCUPIED→VACANT.
    2. For walk-in reservation, it is allowed to have VACANT→OCCUPIED→VACANT.
    3. For maintenance and finish maintenance, it is allowed to have VACANT→MAINTENANCE and
       MAINTENANCE→VACANT only.
8. Reservation status change follows the chains that is specified in each subclass. For
   instance, `checkOut()` method that is able to turn a `Reservation` to a
   `CheckedOutReservation` would only be available to `CheckedInReservation`.
9. Credit card with 16 digits is assumed to be valid.
10. No two guests have the same contact number.
11. Orders are not refundable. Once an order is made, it can't be cancelled and the guest has to pay
    for this order.
12. For room under maintenance, it is unavailable to any reservation as it is not known that when
    the maintenance will be finished. To make maintaining room available for reservation, the user
    have to manually finish the maintenance of that room.
13. Assume that all checked-out reservation and expired reservation will not be further use.
    However, we have propose to keep them in the system for history tracking for future use.
    (See "Our own proposed feature")
14. We assume the guest can manage the space well so we do not limit the number of adults and
    children in a reservation, even if the number of adults and children are more than the room can
    logically accommodate.
15. Items in the same batch of order has the same status as they are processed together. Hence, all
    items under the same order batch will be of the same staus (confirmed, processing or delivered).
16. If a guest wish to change the room number under that particular reservation, he has to cancel
    the reservation and make a new one with the new room number.
17. Promotion rate are set to be a 10% discount. Tax rate is a GST at 7%. GST is applied to the
    price after the promotion discount.
18. Time is static for the reservations. We would need manual check of the current time in the
    following cases
    1. When a guest is checking in and checking out -
        1. we need user to key in the actual check in time during check in. If this is a guest from
           a confirmed reservation, we will use the actual check in time to see if the confirmed
           reservation has expired (≥24 hours after expected check in time). After checking, if the
           reservation has expired, the user will not be able to check in to the reserved room under
           that reservation.
        2. we need user to key in the check out time to see if there is early check out.
    2. When drop reservation from wait list - we need user to check one by one whether the waiting
       reservation's desired check in time has been reached, and drop it from the wait list if it
       does.
19. Reservation flow is: on waitlist -> confirmed -> check-in -> check-out. Staff cannot check in a
    wait list reservation or check out a confirmed reservation.
20. Upon creating a guest, it is mandatory to input the guest’s name, gender, contact, ID details
    and credit card details. The rest of the guest’s information is less important and is optional.
21. If there is an early check out, we will try to if that would open a time slot for wait list
    reservation. If a reservation on waitlist can be fitted it, it will become a confirmed
    reservation.
22. Room service cannot be made for a reservation that is not checked in.

### 5. Additional features proposed

##### Required proposed feature - reservation reminder message #####

We can easily realize this feature by adding a method in `ConfirmedReservation` to describe the
relevant details of the reservation, and call this method at the time of creation of
ConfirmedReservation at the `ReservationController`. We can further retrive guests' contact by the
contact they have provided to send a SMS message to them.

```java
public class ConfirmedReservation {
    ...

    public String generateSMSMessage() {
        return String.format("You have successfully booked a reservation, " +
                        "below is your reservation information:" +
                        "\nReservation = %s " +
                        "\nRoom = %s" +
                        "\nSupposedCheckInDate: %s " +
                        "\nSupposedCheckOutDate: %s", this, getRoomNum(), confirmedCheckInTime,
                confirmedCheckOutTime);
    }
}
```

```java
public class ReservationController {
    public void createConfirmedReservation(...) {
        sendMessage(createConfirmedReservation().generateSMSMessage(), guest.getContact());
    }
}
```

##### Our own proposed features #####

1. **Membership point feature**
   The HRPS will realize a feature to give points to the guests who have stayed in the hotel. The
   points given are proportional to the amount of spending that the guest has made. Guest are
   allowed to redeem their points for discount on the next stay.

   In this case, we will need to store additional attributes `points_earned` and a method
   `accumulatePoints()`
   in the `Guest` class. This will allow HRPS to record the points earned by the each guest and
   allow Guests to accumulate points when they check out. Accumulation function can be realized at
   the checkout stage, `accumulatePoints()` method will be called by passing in the relevant `Guest`
   . We reap the benefits of adhering to open-closed principle to easily add new features here,
   without modification to exisitng code.

   Below shows a sample realization of this feature based on our current implementation.

```java
public class Guest {
    private double points_earned;

    public void accumulatePoints(double points) {
        this.points_earned += points;
    }
}
```

2. **Storing Past Reservation Information**
   We can store past reservation information by specifying an List<Reservation> in the
   `ReservationController`. This list will stores all the `ChekcedOutReservation` and
   `ExpiredReservation`. We would need to implement another Serializable reading and writing file to
   this list. In addition, we can implement methods like `getPastReservation()` in
   `ReservationController` that manipulates the List and support it by `checkPastReservation()`
   method in `ReservationUI` to retrieve the past reservation information. We again reap the
   benefits of open-closed principle to easily add novel features without modification to exisitng
   code.

// todo
