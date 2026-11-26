
package com.estatehub.service;

import com.estatehub.database.DatabaseConnection;
import com.estatehub.model.TourRequest;
import com.estatehub.exceptions.Member2ValidationException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/** Stores and retrieves customer tour requests using MySQL. */
public class TourRequestService {

    public TourRequest requestTour(
            String userId,
            String propertyId,
            LocalDate date,
            String details
    ) throws Member2ValidationException {

        if (userId == null || userId.isBlank()) {
            throw new Member2ValidationException("User ID is required.");
        }

        if (propertyId == null || propertyId.isBlank()) {
            throw new Member2ValidationException("Property ID is required.");
        }

        if (date == null || date.isBefore(LocalDate.now())) {
            throw new Member2ValidationException(
                    "Preferred date must be today or later."
            );
        }

        int numericPropertyId;

        try {
            numericPropertyId = Integer.parseInt(propertyId.trim());
        } catch (NumberFormatException e) {
            throw new Member2ValidationException(
                    "Property ID must be a valid number."
            );
        }

        String requestId = "T" + UUID.randomUUID();
        String cleanDetails = details == null ? "" : details.trim();

        String sql = "INSERT INTO tour_requests "
                + "(request_id, user_id, property_id, tour_date, details) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, requestId);
            ps.setString(2, userId.trim());
            ps.setInt(3, numericPropertyId);
            ps.setDate(4, Date.valueOf(date));
            ps.setString(5, cleanDetails);

            ps.executeUpdate();

            return new TourRequest(
                    requestId,
                    userId.trim(),
                    String.valueOf(numericPropertyId),
                    date,
                    cleanDetails
            );

        } catch (SQLException e) {
            throw new Member2ValidationException(
                    "Tour request could not be saved: " + e.getMessage()
            );
        }
    }

    public List<TourRequest> getRequestsForUser(String userId) {

        List<TourRequest> requests = new ArrayList<>();

        if (userId == null || userId.isBlank()) {
            return requests;
        }

        String sql = "SELECT * FROM tour_requests "
                + "WHERE user_id = ? ORDER BY created_at DESC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userId.trim());

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    requests.add(mapTourRequest(rs));
                }
            }

            return requests;

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Could not retrieve tour requests: " + e.getMessage(), e
            );
        }
    }

    public List<TourRequest> getAllRequests() {

        List<TourRequest> requests = new ArrayList<>();

        String sql = "SELECT * FROM tour_requests ORDER BY created_at DESC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                requests.add(mapTourRequest(rs));
            }

            return Collections.unmodifiableList(requests);

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Could not retrieve all tour requests: "
                            + e.getMessage(), e
            );
        }
    }

    private TourRequest mapTourRequest(ResultSet rs) throws SQLException {

        return new TourRequest(
                rs.getString("request_id"),
                rs.getString("user_id"),
                String.valueOf(rs.getInt("property_id")),
                rs.getDate("tour_date").toLocalDate(),
                rs.getString("details")
        );
    }
}
