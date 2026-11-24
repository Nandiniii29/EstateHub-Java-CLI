
package com.estatehub.repository;

import com.estatehub.database.DatabaseConnection;
import com.estatehub.model.Inquiry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLInquiryRepository {

    // Save a new inquiry into MySQL
    public Inquiry save(Inquiry inquiry) throws SQLException {

        String sql = "INSERT INTO inquiries " +
                "(user_id, property_id, inquiry_type, message, status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, inquiry.getUserId());
            statement.setInt(2, Integer.parseInt(inquiry.getPropertyId()));
            statement.setString(3, inquiry.getInquiryType());
            statement.setString(4, inquiry.getMessage());
            statement.setString(5, inquiry.getStatus().name());

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return findById(String.valueOf(keys.getInt(1)));
                }
            }
        }

        throw new SQLException("Unable to retrieve generated inquiry ID.");
    }

    // Find inquiry using its ID
    public Inquiry findById(String inquiryId) throws SQLException {

        String sql = "SELECT * FROM inquiries WHERE inquiry_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, Integer.parseInt(inquiryId));

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapInquiry(rs);
                }
            }
        }

        return null;
    }

    // Retrieve all inquiries
    public List<Inquiry> findAll() throws SQLException {

        List<Inquiry> inquiries = new ArrayList<>();

        String sql = "SELECT * FROM inquiries ORDER BY created_at DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                inquiries.add(mapInquiry(rs));
            }
        }

        return inquiries;
    }

    // Retrieve inquiries belonging to one customer
    public List<Inquiry> findByUserId(String userId) throws SQLException {

        List<Inquiry> inquiries = new ArrayList<>();

        String sql = "SELECT * FROM inquiries WHERE user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, userId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    inquiries.add(mapInquiry(rs));
                }
            }
        }

        return inquiries;
    }

    // Cancel a pending inquiry
    public boolean cancel(String userId, String inquiryId)
            throws SQLException {

        String sql = "UPDATE inquiries SET status = 'CANCELLED' " +
                "WHERE inquiry_id = ? AND user_id = ? AND status = 'PENDING'";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, Integer.parseInt(inquiryId));
            statement.setString(2, userId);

            return statement.executeUpdate() > 0;
        }
    }

    // Convert database record into Java Inquiry object
    private Inquiry mapInquiry(ResultSet rs) throws SQLException {

        return new Inquiry(
                String.valueOf(rs.getInt("inquiry_id")),
                rs.getString("user_id"),
                String.valueOf(rs.getInt("property_id")),
                rs.getString("inquiry_type"),
                rs.getString("message"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                Inquiry.Status.valueOf(rs.getString("status"))
        );
    }
}
