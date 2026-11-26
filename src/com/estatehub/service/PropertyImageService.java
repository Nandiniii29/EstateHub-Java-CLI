
package com.estatehub.service;

import com.estatehub.database.DatabaseConnection;
import com.estatehub.model.PropertyImage;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PropertyImageService {

    // ADD IMAGE
    public PropertyImage addImage(
            int propertyId,
            String imageUrl,
            String caption,
            boolean isPrimary
    ) {

        validate(propertyId, imageUrl, caption);

        String sql = "INSERT INTO property_images "
                + "(property_id, image_url, caption, is_primary) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection()) {

            if (isPrimary) {
                clearPrimaryImage(con, propertyId);
            }

            try (PreparedStatement ps = con.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, propertyId);
                ps.setString(2, imageUrl.trim());
                ps.setString(3, caption);
                ps.setBoolean(4, isPrimary);

                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        return getImageById(keys.getInt(1));
                    }
                }
            }

            throw new IllegalStateException(
                    "Image was added but its ID could not be retrieved."
            );

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Could not add property image: " + e.getMessage(), e
            );
        }
    }

    // GET IMAGE BY ID
    public PropertyImage getImageById(int imageId) {

        String sql =
                "SELECT * FROM property_images WHERE image_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, imageId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapImage(rs);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Could not retrieve image: " + e.getMessage(), e
            );
        }
    }

    // GET ALL IMAGES FOR A PROPERTY
    public List<PropertyImage> getImagesForProperty(int propertyId) {

        if (propertyId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid property ID."
            );
        }

        List<PropertyImage> images = new ArrayList<>();

        String sql = "SELECT * FROM property_images "
                + "WHERE property_id = ? "
                + "ORDER BY is_primary DESC, uploaded_at DESC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, propertyId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    images.add(mapImage(rs));
                }
            }

            return Collections.unmodifiableList(images);

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Could not retrieve property images: "
                    + e.getMessage(), e
            );
        }
    }

    // GET PRIMARY IMAGE
    public PropertyImage getPrimaryImage(int propertyId) {

        String sql = "SELECT * FROM property_images "
                + "WHERE property_id = ? AND is_primary = TRUE "
                + "LIMIT 1";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, propertyId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapImage(rs);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Could not retrieve primary image: "
                    + e.getMessage(), e
            );
        }
    }

    // SET PRIMARY IMAGE
    public boolean setPrimaryImage(int imageId, int propertyId) {

        String checkSql = "SELECT image_id FROM property_images "
                + "WHERE image_id = ? AND property_id = ?";

        String clearSql = "UPDATE property_images "
                + "SET is_primary = FALSE WHERE property_id = ?";

        String updateSql = "UPDATE property_images "
                + "SET is_primary = TRUE "
                + "WHERE image_id = ? AND property_id = ?";

        try (Connection con = DatabaseConnection.getConnection()) {

            con.setAutoCommit(false);

            try {
                try (PreparedStatement check =
                             con.prepareStatement(checkSql)) {

                    check.setInt(1, imageId);
                    check.setInt(2, propertyId);

                    try (ResultSet rs = check.executeQuery()) {
                        if (!rs.next()) {
                            con.rollback();
                            return false;
                        }
                    }
                }

                try (PreparedStatement clear =
                             con.prepareStatement(clearSql)) {
                    clear.setInt(1, propertyId);
                    clear.executeUpdate();
                }

                try (PreparedStatement update =
                             con.prepareStatement(updateSql)) {

                    update.setInt(1, imageId);
                    update.setInt(2, propertyId);

                    if (update.executeUpdate() == 0) {
                        con.rollback();
                        return false;
                    }
                }

                con.commit();
                return true;

            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Could not update primary image: "
                    + e.getMessage(), e
            );
        }
    }

    // DELETE IMAGE
    public boolean deleteImage(int imageId) {

        String sql =
                "DELETE FROM property_images WHERE image_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, imageId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Could not delete image: " + e.getMessage(), e
            );
        }
    }

    // CLEAR EXISTING PRIMARY IMAGE
    private void clearPrimaryImage(
            Connection con,
            int propertyId
    ) throws SQLException {

        String sql = "UPDATE property_images "
                + "SET is_primary = FALSE WHERE property_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, propertyId);
            ps.executeUpdate();
        }
    }

    // MAP DATABASE RECORD TO MODEL
    private PropertyImage mapImage(ResultSet rs)
            throws SQLException {

        Timestamp timestamp = rs.getTimestamp("uploaded_at");

        return new PropertyImage(
                rs.getInt("image_id"),
                rs.getInt("property_id"),
                rs.getString("image_url"),
                rs.getString("caption"),
                rs.getBoolean("is_primary"),
                timestamp == null ? null : timestamp.toLocalDateTime()
        );
    }

    // VALIDATION
    private void validate(
            int propertyId,
            String imageUrl,
            String caption
    ) {

        if (propertyId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid property ID."
            );
        }

        if (imageUrl == null || imageUrl.isBlank()
                || imageUrl.trim().length() > 500) {
            throw new IllegalArgumentException(
                    "Image URL must contain 1-500 characters."
            );
        }

        if (caption != null && caption.length() > 255) {
            throw new IllegalArgumentException(
                    "Caption cannot exceed 255 characters."
            );
        }
    }
}
