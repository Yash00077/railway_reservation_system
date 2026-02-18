# Railway Reservation System (Console, JDBC, MySQL)

A simple console-based Railway Reservation System built with **Core Java**, **JDBC**, and **MySQL**.  
It demonstrates a menu-driven application with basic CRUD operations using a relational database.

---

##  Features

- **Add Train**
  - Add new trains with name, source, destination, departure time, arrival time, and total seats.
- **View Trains**
  - List all trains with:
    - Train details
    - Total seats
    - Live calculation of available seats (total seats − booked seats).
- **Book Ticket**
  - Book seats on a selected train.
  - Validates that requested seats do not exceed available seats.
- **View Bookings**
  - List all bookings with booking ID, train ID, passenger name, seats, and booking time.
- **Cancel Ticket**
  - Cancel a booking by its booking ID.
  - Frees up seats on the related train.
- **Exit**
  - Cleanly closes the application.

---

##  Tech Stack

- **Language:** Core Java
- **Database:** MySQL
- **Access:** JDBC
- **UI:** Console-based, menu-driven

---

##  Project Structure

```text
.
├── src
│   └── com
│       └── railway
│           ├── App.java                  
│           ├── db
│           │   └── Database.java         
│           ├── dao
│           │   ├── TrainDao.java         
│           │   └── BookingDao.java       
│           └── model
│               ├── Train.java            
│               └── Booking.java          
└── (optional) out/                       
```

##  Configuration

The application reads database configuration from **environment variables** (with defaults):

- `RAILWAY_DB_URL`  
  - Default: `jdbc:mysql://localhost:3306/railway?createDatabaseIfNotExist=true&serverTimezone=UTC`
- `RAILWAY_DB_USER`  
  - Default: `root`
- `RAILWAY_DB_PASSWORD`  
  - Default: `""` (empty string) or whatever is set in code

You can override these by setting environment variables before running the app.

---

##  Usage Guide

When the app starts, you’ll see a menu like:

```text
1. Add Train
2. View Trains
3. Book Ticket
4. View Bookings
5. Cancel Ticket
6. Exit
Choose option:
```

### Add Train

- Input fields:
  - Train name
  - Source
  - Destination
  - Departure datetime
  - Arrival datetime
  - Total seats
- Datetime format (strict):
  - `yyyy-MM-dd HH:mm`
  - Example: `2026-02-01 09:30`

### View Trains

- Displays:
  - Train ID
  - Name
  - Source & Destination
  - Departure & Arrival time
  - Total seats
  - Available seats (calculated as `total_seats - booked`)

### Book Ticket

- Required:
  - Train ID (must exist)
  - Passenger name
  - Seats to book
- Validates:
  - `seats > 0`
  - `seats <= available_seats`
- On success, prints booking ID.

### View Bookings

- Shows a list of:
  - Booking ID
  - Train ID
  - Passenger name
  - Seats
  - Booking time

### Cancel Ticket

- Input:
  - Booking ID
- If the booking exists, it is deleted and seats are freed.

### Exit

- Option `6` cleanly terminates the app.

