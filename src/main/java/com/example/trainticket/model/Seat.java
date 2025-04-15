package com.example.trainticket.model;

public class Seat {
    private String section;
    private int seatNumber;

    // Constructor
    public Seat(String section, int seatNumber) {
        this.section = section;
        this.seatNumber = seatNumber;
    }

    // Getters and setters
    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }

    public int getSeatNumber() { return seatNumber; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }
}
