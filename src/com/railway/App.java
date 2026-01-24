package com.railway;

import com.railway.dao.BookingDao;
import com.railway.dao.TrainDao;
import com.railway.db.Database;
import com.railway.model.Booking;
import com.railway.model.Train;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String url = System.getenv().getOrDefault("RAILWAY_DB_URL", "jdbc:mysql://localhost:3306/railway?createDatabaseIfNotExist=true&serverTimezone=UTC");
        String user = System.getenv().getOrDefault("RAILWAY_DB_USER", "root");
        String pass = System.getenv().getOrDefault("RAILWAY_DB_PASSWORD", "yash15op");
        try (Connection conn = Database.connect(url, user, pass)) {
            Database.initSchema(conn);
            TrainDao trainDao = new TrainDao(conn);
            BookingDao bookingDao = new BookingDao(conn);
            boolean running = true;
            while (running) {
                System.out.println("1. Add Train");
                System.out.println("2. View Trains");
                System.out.println("3. Book Ticket");
                System.out.println("4. View Bookings");
                System.out.println("5. Cancel Ticket");
                System.out.println("6. Exit");
                System.out.print("Choose option: ");
                String choice = scanner.nextLine().trim();
                try {
                    switch (choice) {
                        case "1":
                            addTrain(scanner, trainDao);
                            break;
                        case "2":
                            viewTrains(trainDao);
                            break;
                        case "3":
                            bookTicket(scanner, trainDao, bookingDao);
                            break;
                        case "4":
                            viewBookings(bookingDao);
                            break;
                        case "5":
                            cancelTicket(scanner, bookingDao);
                            break;
                        case "6":
                            running = false;
                            break;
                        default:
                            System.out.println("Invalid option");
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static void addTrain(Scanner scanner, TrainDao trainDao) throws SQLException {
        System.out.print("Train name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Source: ");
        String source = scanner.nextLine().trim();
        System.out.print("Destination: ");
        String destination = scanner.nextLine().trim();
        System.out.print("Departure (yyyy-MM-dd HH:mm): ");
        LocalDateTime departure = LocalDateTime.parse(scanner.nextLine().trim(), DT);
        System.out.print("Arrival (yyyy-MM-dd HH:mm): ");
        LocalDateTime arrival = LocalDateTime.parse(scanner.nextLine().trim(), DT);
        System.out.print("Total seats: ");
        int totalSeats = Integer.parseInt(scanner.nextLine().trim());
        Train t = new Train(0, name, source, destination, departure, arrival, totalSeats);
        int id = trainDao.addTrain(t);
        System.out.println("Train added with id " + id);
    }

    private static void viewTrains(TrainDao trainDao) throws SQLException {
        List<Train> trains = trainDao.listTrains();
        if (trains.isEmpty()) {
            System.out.println("No trains");
            return;
        }
        System.out.printf("%-5s %-20s %-12s %-12s %-17s %-17s %-6s %-9s%n",
                "ID", "Name", "Source", "Dest", "Departure", "Arrival", "Seats", "Avail");
        for (Train t : trains) {
            int avail = trainDao.availableSeats(t.getId());
            System.out.printf("%-5d %-20s %-12s %-12s %-17s %-17s %-6d %-9d%n",
                    t.getId(), t.getName(), t.getSource(), t.getDestination(),
                    DT.format(t.getDepartureTime()), DT.format(t.getArrivalTime()),
                    t.getTotalSeats(), avail);
        }
    }

    private static void bookTicket(Scanner scanner, TrainDao trainDao, BookingDao bookingDao) throws SQLException {
        System.out.print("Train id: ");
        int trainId = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Passenger name: ");
        String passenger = scanner.nextLine().trim();
        System.out.print("Seats to book: ");
        int seats = Integer.parseInt(scanner.nextLine().trim());
        int avail = trainDao.availableSeats(trainId);
        if (seats <= 0 || seats > avail) {
            System.out.println("Insufficient seats");
            return;
        }
        int bookingId = bookingDao.createBooking(trainId, passenger, seats);
        System.out.println("Booked with id " + bookingId);
    }

    private static void viewBookings(BookingDao bookingDao) throws SQLException {
        List<Booking> bookings = bookingDao.listBookings();
        if (bookings.isEmpty()) {
            System.out.println("No bookings");
            return;
        }
        System.out.printf("%-5s %-6s %-20s %-5s %-20s%n",
                "ID", "Train", "Passenger", "Seats", "BookedAt");
        for (Booking b : bookings) {
            System.out.printf("%-5d %-6d %-20s %-5d %-20s%n",
                    b.getId(), b.getTrainId(), b.getPassengerName(), b.getSeats(),
                    DT.format(b.getBookingTime()));
        }
    }

    private static void cancelTicket(Scanner scanner, BookingDao bookingDao) throws SQLException {
        System.out.print("Booking id: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        boolean ok = bookingDao.cancelBooking(id);
        System.out.println(ok ? "Cancelled" : "Not found");
    }
}
