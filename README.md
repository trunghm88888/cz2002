# Hotel Reservation and Payment System (HRPS).

#### Setup guide
1. 

Let's use Visual Paradiam for UML diagram, intelliJ as the collaborating IDE.

Upload the UML diagram into the UML folder.

Todo:
1. Add exception classes
2. For attributes that we do not require a getter in our implementation, is it still needed?
3. Do we need javadoc?
    - I think we do, need to specify the assumptions and workflow

Note:
1. In controller, if no allow the user to input whether he wants driving license / passport
2. I add the apache common validator to check credit card number - Trung

Doubts:
1. Reservation class: delete checkInTime variable
2. CheckedOutReservation constructor: weird sequence of the parameters
3. WaitingReservation → PendingReservation
4. How do we search a pending order if there's no UUID for pending reservation?
5. CreditCard Payment Constructor no function body. 
6. There's no setter in the payment class, how do we set the amount?
7. How to show implements Serializable in UML Class Diag?
8. Do we allow change of order after replaced? If so, need to add setter for OrderedItems
9. What's the use of RoomServiceForDisplay class? If it is for display, shouldn't it be in UI?
10. Should be consistent on return type for setters
11. displayMenu() function can be put in UI?
12. We should be consistent in the ways of instantiating controller instances? PS: a bit typo in MenuController static var
13. In RoomController class's newRoom()func, hasWifi and isSmokingFree has wrong data type, updateCheckInTime()'s second param also a bit different from other funds
14. In which class is payment used? 
15. Overlapping functionality of DisplayMenu class and MenuManager Class
16. Can change the design of MenuController?
17. Must contact be a phone number?
18. The ReservationUI's getValidDateTime() and GuestUI's createNewGuestUI(): why are them public?
19. I suggest the fields rate, checkInDate, currentGuest (also a room may contain multiple guests) should not be 
    included in Room class as these fields can change frequently (say, if the INITIAL_RATE changes we have to 
    iterate through all the rooms and change their rates, then update on the text files, I think we should include 
    1 function in the RoomController for calculating and displaying the rates to customers) 
    and for checkInDate/currentGuest they could potentially be null.
