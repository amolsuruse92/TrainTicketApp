package com.example.trainticket.service;

import com.example.trainticket.model.Receipt;
import com.example.trainticket.model.TicketRequest;

import java.util.List;
import java.util.Optional;

public interface ITicketService {
    Receipt purchaseTicket(TicketRequest request);

    Optional<Receipt> getReceipt(String email);

    List<Receipt> getUsersBySection(String section);

    void removeUser(String email);

    Optional<Receipt> modifySeat(String email, String newSection, int newSeatNumber);
}
