package com.railway.model;

import java.time.LocalDateTime;

public class Booking {
    private final int id;
    private final int trainId;
    private final String passengerName;
    private final int seats;
    private final LocalDateTime bookingTime;

    public Booking(int id, int trainId, String passengerName, int seats, LocalDateTime bookingTime) {
        this.id = id;
        this.trainId = trainId;
        this.passengerName = passengerName;
        this.seats = seats;
        this.bookingTime = bookingTime;
    }

    public int getId() {
        return id;
    }

    public int getTrainId() {
        return trainId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public int getSeats() {
        return seats;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }
}
