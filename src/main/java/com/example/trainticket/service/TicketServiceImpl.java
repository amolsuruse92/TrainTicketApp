package com.example.trainticket.service;

import com.example.trainticket.exception.UserAlreadyBookedException;
import com.example.trainticket.exception.UserNotFoundException;
import com.example.trainticket.model.*;
import com.example.trainticket.repository.ITicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import java.util.List;
import java.util.Optional;
/**
 * Implementation of the ITicketService that handles business logic for ticket operations.
 */
@Service
public class TicketServiceImpl implements ITicketService {
    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);
    @Autowired
    private ITicketRepository ticketRepository;
    @Value("${train.from}")
    private String FROM_LOCATION;
    @Value("${train.to}")
    private String TO_LOCATION;
    @Value("${train.price}")
    private double TICKET_PRICE;

    /**
     * Purchases a ticket for the given user and assigns a seat.
     *
     * @param request the ticket purchase request
     * @return a receipt containing ticket details
     */
    public Receipt purchaseTicket(TicketRequest request) {
        logger.debug("Attempting to purchase ticket for email: {}", request.getEmail());
        Optional<Receipt> existingReceipt = ticketRepository.findByEmail(request.getEmail());
        if (existingReceipt.isPresent()) {
            logger.error("Duplicate booking attempt for email: {}", request.getEmail());
            // If the user has already booked a ticket, throw a validation exception
            throw new UserAlreadyBookedException("User with email " + request.getEmail() + " has already booked a ticket.");
        }
        Seat seat = ticketRepository.allocateSeat();
        User user = new User(request.getFirstName(), request.getLastName(), request.getEmail());
        Receipt receipt = new Receipt(FROM_LOCATION, TO_LOCATION, user, TICKET_PRICE, seat);
        logger.debug("Ticket booked successfully for {} {}", request.getFirstName(), request.getLastName());
        return ticketRepository.save(receipt);
    }
    /**
     * Retrieves the receipt for a specific user by email.
     *
     * @param email the user's email address
     * @return the corresponding receipt, or null if not found
     */
    public Optional<Receipt> getReceipt(String email) {
        logger.debug("Retrieving receipt for email: {}", email);
        Optional<Receipt> optionalReceipt = ticketRepository.findByEmail(email);
        if (optionalReceipt.isEmpty()) {
            logger.warn("No receipt found for email: {}", email);
            throw new UserNotFoundException("User with email " + email + " not found.");
        }
        return optionalReceipt;
    }
    /**
     * Returns a list of users allocated in the specified section.
     *
     * @param section the section to filter by (e.g., "A", "B")
     * @return list of users in the section
     */
    public List<Receipt> getUsersBySection(String section) {
        logger.debug("Fetching users in section: {}", section);
        return ticketRepository.findBySection(section);
    }
    /**
     * Removes a user from the train by deleting their receipt.
     *
     * @param email the user's email
     */
    public void removeUser(String email) {
        logger.debug("Attempting to remove user with email: {}", email);
        Optional<Receipt> optionalReceipt = ticketRepository.findByEmail(email);
        if (optionalReceipt.isEmpty()) {
            throw new UserNotFoundException("User with email " + email + " not found.");
        }
        ticketRepository.deleteByEmail(email);
        logger.debug("User removed successfully: {}", email);

    }
    /**
     * Modifies the seat section for an existing user.
     * @return the updated receipt
     */
    public Optional<Receipt> modifySeat(String email, String newSection, int newSeatNumber) {
        logger.debug("Modifying seat for user: {}", email);
        Optional<Receipt> optionalReceipt = ticketRepository.findByEmail(email);
        if (optionalReceipt.isEmpty()) {
            logger.warn("No user found with email: {}", email);
            throw new UserNotFoundException("User with email " + email + " not found.");
        }
        if (!newSection.equalsIgnoreCase("A") && !newSection.equalsIgnoreCase("B")) {
            logger.error("Invalid section provided: {}", newSection);
            throw new IllegalArgumentException("Invalid section. Please choose either 'A' or 'B'.");
        }
        optionalReceipt.ifPresent(receipt -> {
            receipt.setSeat(new Seat(newSection, newSeatNumber));
            ticketRepository.save(receipt);
        });
        logger.debug("Seat modified successfully for email: {}", email);
        return optionalReceipt;
    }
}
