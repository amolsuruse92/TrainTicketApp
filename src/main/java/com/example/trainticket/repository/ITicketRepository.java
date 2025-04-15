package com.example.trainticket.repository;

import com.example.trainticket.model.Receipt;
import com.example.trainticket.model.Seat;

import java.util.List;
import java.util.Optional;

public interface ITicketRepository {
    Receipt save(Receipt receipt);

    Optional<Receipt> findByEmail(String email);

    List<Receipt> findBySection(String section);

    void deleteByEmail(String email);

    //List<Receipt> findAll();

    Seat allocateSeat();
}
