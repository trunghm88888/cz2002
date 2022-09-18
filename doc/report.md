![logo](../img/logo.jpeg)

# 1. Introduction
**Hotel Reservation and Payment System (HRPS)** is an application designed for hotel 
administrators to manage reservations of hotel rooms and orders of room service. 
The application covers the key features such as making hotel reservations, recording orders, and viewing records. 

This report covers our design considerations including object-oriented concepts, design principles and patterns, as well as our assumptions made when building this system. 
We also include feature description for user-friendliness, and UML diagrams for displaying the interactions and relationships among objects. Additionally, test cases are also well-presented 
to ensure that our application can meet certain requirements and constraints.
# 2. Design Considerations

## 2.1. Approach
In this project, **OOP** concepts are used comprehensively in both design and implementation. We also take immutability and information hiding into consideration in most classes to ensure safety.

<img align="middle" width="320" height="380" src="https://i.ibb.co/vmX6TxC/approach.png" alt="approach" border="0" />

We follow the **layerd (n-tier) architecture** which utilizes the **model–view–controller (MVC)** design pattern. 
The code is set up so that data enters at the top layer and works its way down through each layer until it reaches the bottom. 
Each layer has a distinct task along the route. 
In our application, the top layer **UI** classes receives commands from users and work dependently on **controller** classes, which manipulates the operations of **entity** class and **data storage**.


## 2.2. Object-Oriented Concepts

## 2.3. Design Principles

# 3. Assumptions made
Each **contact number** can only be registered for one guest.
Room is not created individually by UI, instead, when a new room is built, its data must be manually written to the 
RoomData.txt file.
# 4. Features introduction (User Guide)

## 4.1. functionalities

## 4.2. user friendliness

When we are selecting mulitple entries during check out and updating order status, instead of 
letting the user to key in lengthy details about check out or an order that a room made, we 
allow the user to select the index number of listed items.

(Give examples here)

# 5. UML diagram display

## 5.1. Class Diagram

## 5.2. Sequence Diagram

# 6. Test cases

## 6.1 Functionalities and User friendliness

### 6.1.1 Guest
| feature             |  user input | result display  
:-------------------------:|:-------------------------:|:-------------------------:
|add a new guest|![](../img/create_new_guest.png)  |  ![](../img/create_new_guest_display.png)
|update a guest's details|![](../img/update_guest_1.png) ![](../img/update_guest_2.png)|![](../img/update_guest_3.png)

### 6.1.2 Reservation
#### 6.1.2.1 Add a reservation
| feature             |  user input | result display
:-------------------------:|:-------------------------:|:-------------------------:
|add a reservation from a new guest|![](../img/add_reserve.png)  |  ![](../img/add_reserve2.png)
|add a reservation from an existing guest| ![](../img/add_reserve_guest.png)  |  ![](../img/add_reserve_guest2.png)

#### 6.1.2.2 Update a reservation
| feature             |  user input 
:-------------------------:|:-------------------------:
|search for a reservation to update by guest contact|![](../img/update_reserve.png)  
|search for a reservation with non-existing guest contact|![](../img/update_reserve_invalid.png)  

#### 6.1.2.3 update reservation status
| feature             |  user input | result display
:-------------------------:|:-------------------------:|:-------------------------:
|confirm a pending reservation|![](../img/pending_confirm1.png)  |  ![](../img/pending_confirm2.png)
|cancel a waitlisted reservation| ![](../img/pending_cancel1.png)  |  ![](../img/pending_cancel2.png)
|check in a confirmed reservation| ![](../img/pending_confirm1.png)  |  ![](../img/confirm_checkin2.png)
|cancel a confirmed reservation (customer does not check in by time)| ![](../img/confirm_cancel1.png)  |  ![](../img/confirm_cancel2.png)
|check out a checked in reservation| ![](../img/pending_confirm1.png)  |  ![](../img/checkin_checkout2.png)

### 6.1.3 Room
#### 6.1.3.1 Create all rooms
| feature             |  user input | result display
:-------------------------:|:-------------------------:|:-------------------------:
create all rooms| |![](../img/create_all_rooms.PNG)

#### 6.1.3.2 Check room's status
| feature             |  user input | result display
:-------------------------:|:-------------------------:|:-------------------------:

#### 6.1.3.3 Update room's details
| feature             |  user input | result display
:-------------------------:|:-------------------------:|:-------------------------:
|update room's rate|![](../img/update_room_rates_1.PNG) |![](../img/update_room_rates_2.PNG)
|maintain room|![](../img/maintain_room_1.PNG)|![](../img/maintain_room_2.PNG)

### 6.1.4 Room service order
#### 6.1.4.1 Entering order services
| feature             |  user input | result display
:-------------------------:|:-------------------------:|:-------------------------:
|select items for order|![](../img/order_items_selection_1.PNG) ![](../img/order_items_selection_2.PNG) ![](../img/order_items_selection_3.PNG)|![](../img/order_items_selection_4.PNG)
|check & update order's status|![](../img/check_update_order_1.PNG)|![](../img/check_update_order_2.PNG)

#### 6.1.4.2 Create/Update/Remove room service menu items
| feature             |  user input | result display
:-------------------------:|:-------------------------:|:-------------------------:
|create menu items|![](../img/create_menu_item_1.PNG)|![](../img/create_menu_item_2.PNG)
|update menu items|![](../img/update_menu_item_1.PNG)|![](../img/update_menu_item_2.PNG)
|remove menu items|![](../img/remove_menu_item_1.PNG)|![](../img/remove_menu_item_2.PNG)

## 6.2 Negative cases

### 6.2.1 Guest
| feature             |  user input | result display
:-------------------------:|:-------------------------:|:-------------------------:
|add a guest with contact existing in system| ![](../img/add_existing_guest.png)  |  ![](../img/add_existing_guest_display.png)
|invalid credit card number| ![](../img/invalid_add_guest.png)  |  ![](../img/invalid_add_guest_display.png)

### 6.2.2 Reservation
| feature             |  user input | result display
:-------------------------:|:-------------------------:|:-------------------------:
|invalid room/bed type input| ![](../img/invalid_room_type.png)  |  ![](../img/invalid_room_type2.png)

#7. Learning point and reflection