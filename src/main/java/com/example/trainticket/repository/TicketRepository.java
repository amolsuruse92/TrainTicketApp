package com.example.trainticket.repository;

import com.example.trainticket.model.Receipt;
import com.example.trainticket.model.Seat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
/**
 * Implementation of ITicketRepository that stores ticket data in-memory.
 */
@Repository
public class TicketRepository implements ITicketRepository{
    private static final Logger logger = LoggerFactory.getLogger(TicketRepository.class);
    private final Map<String, Receipt> tickets = new HashMap<>();
    private final AtomicInteger seatCounterA = new AtomicInteger(1);
    private final AtomicInteger seatCounterB = new AtomicInteger(1);

    /**
     * Save a receipt for the given user.
     *
     * @param receipt the receipt to store
     */
    public Receipt save(Receipt receipt) {
        logger.debug("Saving receipt for email: {}", receipt.getUser().getEmail());
        tickets.put(receipt.getUser().getEmail(), receipt);
        logger.debug("Total tickets after save: {}", tickets.size());
        return receipt;
    }

    /**
     * Retrieve a receipt by the user's email.
     *
     * @param email the user's email
     * @return the corresponding receipt, or null if not found
     */
    public Optional<Receipt> findByEmail(String email) {
        logger.debug("Fetching receipt for email: {}", email);
        return Optional.ofNullable(tickets.get(email));
    }
    /**
     * Retrieve a receipt by the section.
     *
     * @param section the section
     * @return the corresponding receipt, or null if not found
     */
    public List<Receipt> findBySection(String section) {
        return tickets.values().stream()
                .filter(r -> r.getSeat().getSection().equalsIgnoreCase(section))
                .collect(Collectors.toList());
    }
    /**
     * Delete a receipt based on the user's email.
     *
     * @param email the user's email
     */
    public void deleteByEmail(String email) {
        logger.debug("Deleting receipt for email: {}", email);
        tickets.remove(email);
        logger.debug("Total tickets after delete: {}", tickets.size());
    }

    /*public List<Receipt> findAll() {
        return new ArrayList<>(tickets.values());
    }*/
    /**
     * Allocate seat with section.
     */
    public Seat allocateSeat() {
        String section = seatCounterA.get() <= seatCounterB.get() ? "A" : "B";
        int seatNumber = section.equals("A") ? seatCounterA.getAndIncrement() : seatCounterB.getAndIncrement();
        return new Seat(section, seatNumber);
    }
}
