package com.railway.dao;

import com.railway.model.Booking;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingDao {
    private final Connection conn;

    public BookingDao(Connection conn) {
        this.conn = conn;
    }

    public int createBooking(int trainId, String passengerName, int seats) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO bookings(train_id, passenger_name, seats, booking_time) VALUES (?,?,?,CURRENT_TIMESTAMP)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, trainId);
            ps.setString(2, passengerName);
            ps.setInt(3, seats);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public List<Booking> listBookings() throws SQLException {
        List<Booking> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT id,train_id,passenger_name,seats,booking_time FROM bookings ORDER BY id")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int trainId = rs.getInt("train_id");
                    String name = rs.getString("passenger_name");
                    int seats = rs.getInt("seats");
                    LocalDateTime time = rs.getTimestamp("booking_time").toLocalDateTime();
                    list.add(new Booking(id, trainId, name, seats, time));
                }
            }
        }
        return list;
    }

    public boolean cancelBooking(int bookingId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM bookings WHERE id=?")) {
            ps.setInt(1, bookingId);
            return ps.executeUpdate() > 0;
        }
    }
}
