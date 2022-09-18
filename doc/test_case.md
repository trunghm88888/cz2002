1. Show how waiting reservation will be generated. (with diagram)
2. Show how a waiting reservataion will automatically become reserved if there is an early checkout


Error in input check
1. Credit card number check
2. Date check in (check in date is earlier than check out date)
3. Key in a room that does not exist on the given choices
4. Illegal status change
5. (All things in exception)

Show something for gender


minimum data entry - selection!

Good Engineering control: GitHub for version control and collaboration and doing documentation

**\* = error input checking**
1. Guest
   - Add a guest with a contact (**guest_1**)
   - Trying to add another guest with the same contact *
   - Credit card: 1.less than 16 digits 2.more than 16 digits *
   - Update **guest_1**'s credit card number and billing address
   - Find detail of the above created guest
   - Create two guests with name "John A", "John B", find guests by name with keyword "John", "john", "A", "jo"

2. Reservation
   - Create a confirmed reservation and check in that reservation
   - Call room status report to see the change
   - Create a walk-in reservation (**res_1**) on room (VIP, King, South) and input the wrong check-out date (earlier 
     than check-in date) *, try to create again with appropriate dates
   - Call room status report to see the change 
   - Create a waiting reservation on the same VIP room as the above reservation with staying period overlaps the above 
     reservation
3. Room
   - Create all rooms from data file
   - Maintain a OCCUPIED room *, a RESERVED room *, a VACANT room
   - Call roomStatusReport to see the change
   - Call maintain again on the just-called room *

4. Service
   - Display the menu
   - Create a new menu item with name same as an existing menu item *, remove the existing menu item and try again
   - Update a menu item price
   - Make an order on a non-occupied room *
   - Make an order on a occupied room, update its status to delivered

5. Check-out/Payment
   - Check out a guest with a non-existing name *
   - Check out a guest by contact, with check-out time earlier than check-in time *, try again with appropriate 
     check-out time
   - Call room occupancy, room status reports to see the change
   - Early check-out **res_1** and print all reservation to see the status of waiting reservation changed to 
     confirmed
   - Call the room status report to see the room status change.