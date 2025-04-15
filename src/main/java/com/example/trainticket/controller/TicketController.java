package com.example.trainticket.controller;

import com.example.trainticket.model.ModifySeatRequest;
import com.example.trainticket.model.Receipt;
import com.example.trainticket.model.TicketRequest;
import com.example.trainticket.service.TicketServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * REST controller for managing train ticket operations.
 * Provides endpoints to purchase, view, modify, and delete tickets.
 */
@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);

    private final TicketServiceImpl ticketService;

    public TicketController(TicketServiceImpl service) {
        this.ticketService = service;
    }

    /**
     * Endpoint to purchase a ticket.
     *
     * @param request the ticket purchase request containing user info
     * @return a receipt with booking details
     */
    @PostMapping("/purchase")
    public ResponseEntity<Receipt> purchaseTicket(@RequestBody TicketRequest request) {
        logger.debug("POST /purchase - Request received for: {}", request.getEmail());
        return ResponseEntity.ok(ticketService.purchaseTicket(request));
    }

    /**
     * Endpoint to retrieve a receipt for a user by email.
     *
     * @param email the user's email
     * @return the user's ticket receipt
     */
    @GetMapping("/receipt/{email}")
    public ResponseEntity<Receipt> getReceipt(@PathVariable String email) {
        logger.debug("GET /receipt/{} - Retrieving receipt", email);
        return ticketService.getReceipt(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint to get users and their seat allocations in a specific section.
     *
     * @param section the train section (A or B)
     * @return list of users in that section
     */
    @GetMapping("/section/{section}")
    public ResponseEntity<List<Receipt>> getUsersBySection(@PathVariable String section) {
        logger.debug("GET /users/{} - Retrieving users in section", section);
        return ResponseEntity.ok(ticketService.getUsersBySection(section));
    }

    /**
     * Endpoint to remove a user from the train by email.
     *
     * @param email the user's email
     * @return confirmation message
     */
    @DeleteMapping("/remove/{email}")
    public ResponseEntity<Void> removeUser(@PathVariable String email) {
        logger.debug("DELETE /remove/{} - Removing user", email);
        ticketService.removeUser(email);
        logger.debug("User removed successfully: {}", email);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint to modify a user's seat section.
     *
     * @param request the seat modification request
     * @return updated receipt with new seat info
     */
    @PutMapping("/modify-seat")
    public ResponseEntity<Receipt> modifySeat(@RequestBody ModifySeatRequest request) {
        logger.debug("PUT /modify-seat - Modifying seat for: {}", request.getEmail());
        return ticketService.modifySeat(request.getEmail(), request.getSection(), request.getSeatNumber())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
