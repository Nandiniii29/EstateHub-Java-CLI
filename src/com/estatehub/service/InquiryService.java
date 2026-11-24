
package com.estatehub.service;

import com.estatehub.exceptions.Member2ValidationException;
import com.estatehub.model.Inquiry;
import com.estatehub.repository.MySQLInquiryRepository;
import java.sql.SQLException;
import java.util.List;

public class InquiryService {

    private final MySQLInquiryRepository repository;

    public InquiryService(MySQLInquiryRepository repository) {
        this.repository = repository;
    }

    // Submit a new inquiry
    public Inquiry submit(String userId, String propertyId,
                          String type, String message)
            throws Member2ValidationException {

        require(userId, "User ID");
        require(propertyId, "Property ID");
        require(type, "Inquiry type");
        require(message, "Message");

        try {
            int id = Integer.parseInt(propertyId.trim());

            if (id <= 0) {
                throw new Member2ValidationException(
                        "Property ID must be positive."
                );
            }

            Inquiry inquiry = new Inquiry(
                    null,
                    userId.trim(),
                    String.valueOf(id),
                    type.trim(),
                    message.trim()
            );

            return repository.save(inquiry);

        } catch (NumberFormatException e) {
            throw new Member2ValidationException(
                    "Property ID must be a valid number."
            );

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error submitting inquiry: " + e.getMessage(), e
            );
        }
    }

    // Get inquiry history of a customer
    public List<Inquiry> getHistory(String userId) {

        try {
            return repository.findByUserId(userId);

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error retrieving inquiry history: " + e.getMessage(), e
            );
        }
    }

    // Get all customer inquiries for admin
    public List<Inquiry> getAllInquiries() {

        try {
            return repository.findAll();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error retrieving inquiries: " + e.getMessage(), e
            );
        }
    }

    // Cancel a pending inquiry
    public boolean cancel(String userId, String inquiryId)
            throws Member2ValidationException {

        require(userId, "User ID");
        require(inquiryId, "Inquiry ID");

        try {
            return repository.cancel(userId, inquiryId);

        } catch (NumberFormatException e) {
            throw new Member2ValidationException(
                    "Invalid inquiry ID."
            );

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error cancelling inquiry: " + e.getMessage(), e
            );
        }
    }

    private static void require(String value, String label)
            throws Member2ValidationException {

        if (value == null || value.isBlank()) {
            throw new Member2ValidationException(
                    label + " is required."
            );
        }
    }
}
