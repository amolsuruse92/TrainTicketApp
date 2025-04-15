package com.example.trainticket.model;

public class ModifySeatRequest {
    private String email;
    private int seatNumber;
    private String section;

    public String getEmail() {
        return email;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }




}
