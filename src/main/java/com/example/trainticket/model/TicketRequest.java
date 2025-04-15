package com.example.trainticket.model;

public class TicketRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String from;
    private String to;

    // Constructor
    public TicketRequest(String firstName, String lastName, String email, String from, String to) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.from = from;
        this.to = to;
    }

    // Getters and setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }
}
