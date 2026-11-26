
package com.estatehub.service;

import com.estatehub.database.DatabaseConnection;
import com.estatehub.model.AdminActivityLog;
import com.estatehub.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminActivityLogService {

    // RECORD AN ADMIN ACTIVITY
    public void logActivity(
            User admin,
            Integer propertyId,
            String action,
            String description
    ) {

        if (admin == null || admin.getRole() != User.Role.ADMIN) {
            throw new IllegalArgumentException(
                    "Only administrators can record admin activities."
            );
        }

        if (action == null || action.isBlank()
                || action.length() > 100) {
            throw new IllegalArgumentException(
                    "Action must contain 1-100 characters."
            );
        }

        if (propertyId != null && propertyId <= 0) {
            throw new IllegalArgumentException("Invalid property ID.");
        }

        String sql = "INSERT INTO admin_activity_log "
                + "(admin_user_id, property_id, action, description) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, admin.getUserId());

            if (propertyId == null) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setInt(2, propertyId);
            }

            ps.setString(3, action);
            ps.setString(4, description);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Could not record admin activity: " + e.getMessage(), e
            );
        }
    }

    // GET ALL ADMIN ACTIVITIES
    public List<AdminActivityLog> getAllActivities() {

        List<AdminActivityLog> activities = new ArrayList<>();

        String sql = "SELECT * FROM admin_activity_log "
                + "ORDER BY activity_time DESC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                activities.add(mapActivity(rs));
            }

            return Collections.unmodifiableList(activities);

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Could not retrieve admin activities: " + e.getMessage(), e
            );
        }
    }

    // GET ACTIVITIES BY ADMIN
    public List<AdminActivityLog> getActivitiesByAdmin(String adminUserId) {

        if (adminUserId == null || adminUserId.isBlank()) {
            throw new IllegalArgumentException("Admin user ID is required.");
        }

        List<AdminActivityLog> activities = new ArrayList<>();

        String sql = "SELECT * FROM admin_activity_log "
                + "WHERE admin_user_id = ? "
                + "ORDER BY activity_time DESC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, adminUserId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    activities.add(mapActivity(rs));
                }
            }

            return Collections.unmodifiableList(activities);

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Could not retrieve admin history: " + e.getMessage(), e
            );
        }
    }

    // GET ACTIVITIES FOR A PROPERTY
    public List<AdminActivityLog> getActivitiesByProperty(int propertyId) {

        if (propertyId <= 0) {
            throw new IllegalArgumentException("Invalid property ID.");
        }

        List<AdminActivityLog> activities = new ArrayList<>();

        String sql = "SELECT * FROM admin_activity_log "
                + "WHERE property_id = ? "
                + "ORDER BY activity_time DESC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, propertyId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    activities.add(mapActivity(rs));
                }
            }

            return Collections.unmodifiableList(activities);

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Could not retrieve property activity history: "
                            + e.getMessage(), e
            );
        }
    }

    // MAP DATABASE RECORD TO MODEL
    private AdminActivityLog mapActivity(ResultSet rs) throws SQLException {

        int propertyValue = rs.getInt("property_id");
        Integer propertyId = rs.wasNull() ? null : propertyValue;

        Timestamp timestamp = rs.getTimestamp("activity_time");

        return new AdminActivityLog(
                rs.getInt("activity_id"),
                rs.getString("admin_user_id"),
                propertyId,
                rs.getString("action"),
                rs.getString("description"),
                timestamp == null ? null : timestamp.toLocalDateTime()
        );
    }
}
