
package com.estatehub.service;

import com.estatehub.database.DatabaseConnection;
import com.estatehub.exceptions.Member2ValidationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Handles customer favorites, recently viewed and property comparison. */
public class CustomerPropertyService {

    // Add a property to favorites
    public boolean addFavorite(String userId, String propertyId)
            throws Member2ValidationException {

        validate(userId, propertyId);

        String sql = "INSERT IGNORE INTO favorites (user_id, property_id) "
                + "VALUES (?, ?)";

        return insertProperty(sql, userId, propertyId);
    }

    // Remove a property from favorites
    public boolean removeFavorite(String userId, String propertyId)
            throws Member2ValidationException {

        validate(userId, propertyId);

        String sql = "DELETE FROM favorites "
                + "WHERE user_id = ? AND property_id = ?";

        return deleteProperty(sql, userId, propertyId);
    }

    // Get all favorite property IDs
    public List<String> getFavorites(String userId) {
        return getPropertyIds(
                "SELECT property_id FROM favorites "
                        + "WHERE user_id = ? ORDER BY created_at DESC",
                userId
        );
    }

    // Record a property as recently viewed
    public void recordViewed(String userId, String propertyId)
            throws Member2ValidationException {

        validate(userId, propertyId);

        String sql = "INSERT INTO recently_viewed "
                + "(user_id, property_id) VALUES (?, ?) "
                + "ON DUPLICATE KEY UPDATE viewed_at = CURRENT_TIMESTAMP";

        insertProperty(sql, userId, propertyId);
    }

    // Get recently viewed properties
    public List<String> getRecentlyViewed(String userId) {
        return getPropertyIds(
                "SELECT property_id FROM recently_viewed "
                        + "WHERE user_id = ? ORDER BY viewed_at DESC",
                userId
        );
    }

    // Add a property to comparison
    public boolean addToComparison(String userId, String propertyId)
            throws Member2ValidationException {

        validate(userId, propertyId);

        String sql = "INSERT IGNORE INTO property_comparisons "
                + "(user_id, property_id) VALUES (?, ?)";

        return insertProperty(sql, userId, propertyId);
    }

    // Remove a property from comparison
    public boolean removeFromComparison(String userId, String propertyId)
            throws Member2ValidationException {

        validate(userId, propertyId);

        String sql = "DELETE FROM property_comparisons "
                + "WHERE user_id = ? AND property_id = ?";

        return deleteProperty(sql, userId, propertyId);
    }

    // Get properties selected for comparison
    public List<String> getComparison(String userId) {
        return getPropertyIds(
                "SELECT property_id FROM property_comparisons "
                        + "WHERE user_id = ? ORDER BY added_at DESC",
                userId
        );
    }

    // Common method for inserting property records
    private boolean insertProperty(
            String sql, String userId, String propertyId) {

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userId.trim());
            ps.setInt(2, Integer.parseInt(propertyId.trim()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Database operation failed: " + e.getMessage(), e
            );
        }
    }

    // Common method for deleting property records
    private boolean deleteProperty(
            String sql, String userId, String propertyId) {

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userId.trim());
            ps.setInt(2, Integer.parseInt(propertyId.trim()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Database operation failed: " + e.getMessage(), e
            );
        }
    }

    // Retrieve property IDs from a customer-related table
    private List<String> getPropertyIds(String sql, String userId) {

        List<String> propertyIds = new ArrayList<>();

        if (userId == null || userId.isBlank()) {
            return propertyIds;
        }

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userId.trim());

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    propertyIds.add(
                            String.valueOf(rs.getInt("property_id"))
                    );
                }
            }

            return Collections.unmodifiableList(propertyIds);

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Could not retrieve properties: " + e.getMessage(), e
            );
        }
    }

    // Validate user and property IDs
    private static void validate(String userId, String propertyId)
            throws Member2ValidationException {

        if (userId == null || userId.isBlank()) {
            throw new Member2ValidationException("User ID is required.");
        }

        if (propertyId == null || propertyId.isBlank()) {
            throw new Member2ValidationException("Property ID is required.");
        }

        try {
            if (Integer.parseInt(propertyId.trim()) <= 0) {
                throw new Member2ValidationException(
                        "Property ID must be positive."
                );
            }
        } catch (NumberFormatException e) {
            throw new Member2ValidationException(
                    "Property ID must be a valid number."
            );
        }
    }
}
