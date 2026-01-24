package com.railway.dao;

import com.railway.model.Train;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TrainDao {
    private final Connection conn;

    public TrainDao(Connection conn) {
        this.conn = conn;
    }

    public int addTrain(Train t) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO trains(name, source, destination, departure_time, arrival_time, total_seats) VALUES (?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getName());
            ps.setString(2, t.getSource());
            ps.setString(3, t.getDestination());
            ps.setTimestamp(4, Timestamp.valueOf(t.getDepartureTime()));
            ps.setTimestamp(5, Timestamp.valueOf(t.getArrivalTime()));
            ps.setInt(6, t.getTotalSeats());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public List<Train> listTrains() throws SQLException {
        List<Train> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT id,name,source,destination,departure_time,arrival_time,total_seats FROM trains ORDER BY id")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String source = rs.getString("source");
                    String destination = rs.getString("destination");
                    LocalDateTime dep = rs.getTimestamp("departure_time").toLocalDateTime();
                    LocalDateTime arr = rs.getTimestamp("arrival_time").toLocalDateTime();
                    int seats = rs.getInt("total_seats");
                    list.add(new Train(id, name, source, destination, dep, arr, seats));
                }
            }
        }
        return list;
    }

    public int availableSeats(int trainId) throws SQLException {
        int total = 0;
        try (PreparedStatement ps = conn.prepareStatement("SELECT total_seats FROM trains WHERE id=?")) {
            ps.setInt(1, trainId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) total = rs.getInt(1);
            }
        }
        int booked = 0;
        try (PreparedStatement ps = conn.prepareStatement("SELECT COALESCE(SUM(seats),0) FROM bookings WHERE train_id=?")) {
            ps.setInt(1, trainId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) booked = rs.getInt(1);
            }
        }
        return Math.max(total - booked, 0);
    }
}
