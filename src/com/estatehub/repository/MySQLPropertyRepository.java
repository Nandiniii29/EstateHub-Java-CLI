
package com.estatehub.repository;

import com.estatehub.database.DatabaseConnection;
import com.estatehub.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLPropertyRepository implements PropertyRepository {

    @Override
    public void addProperty(Property property) {

        String sql = "INSERT INTO properties (property_id, title, purpose, property_type, city, locality, property_address, price, bedrooms, bathrooms, area, furnishing_status, description, listing_status, owner_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, property.getPropertyId());
            statement.setString(2, property.getTitle());
            statement.setString(3, property.getPurpose().name());
            statement.setString(4, property.getType().name());
            statement.setString(5, property.getCity());
            statement.setString(6, property.getLocality());
            statement.setString(7, property.getAddress());
            statement.setDouble(8, property.getPrice());
            statement.setInt(9, property.getBedrooms());
            statement.setInt(10, property.getBathrooms());
            statement.setDouble(11, property.getArea());
            statement.setString(12, property.getFurnishingStatus().name());
            statement.setString(13, property.getDescription());
            statement.setString(14, property.getListingStatus().name());
            statement.setInt(15, property.getOwnerId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error adding property: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Property> findAll() {

        List<Property> properties = new ArrayList<>();

        String sql = "SELECT * FROM properties";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                properties.add(mapProperty(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving properties: " + e.getMessage(), e);
        }

        return properties;
    }

    @Override
    public Property findById(int propertyId) {

        String sql = "SELECT * FROM properties WHERE property_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, propertyId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapProperty(resultSet);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding property: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public boolean updateProperty(Property property) {

        String sql = "UPDATE properties SET title=?, purpose=?, property_type=?, city=?, locality=?, property_address=?, price=?, bedrooms=?, bathrooms=?, area=?, furnishing_status=?, description=?, listing_status=?, owner_id=? WHERE property_id=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, property.getTitle());
            statement.setString(2, property.getPurpose().name());
            statement.setString(3, property.getType().name());
            statement.setString(4, property.getCity());
            statement.setString(5, property.getLocality());
            statement.setString(6, property.getAddress());
            statement.setDouble(7, property.getPrice());
            statement.setInt(8, property.getBedrooms());
            statement.setInt(9, property.getBathrooms());
            statement.setDouble(10, property.getArea());
            statement.setString(11, property.getFurnishingStatus().name());
            statement.setString(12, property.getDescription());
            statement.setString(13, property.getListingStatus().name());
            statement.setInt(14, property.getOwnerId());
            statement.setInt(15, property.getPropertyId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating property: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteProperty(int propertyId) {

        String sql = "DELETE FROM properties WHERE property_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, propertyId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting property: " + e.getMessage(), e);
        }
    }

    private Property mapProperty(ResultSet resultSet) throws SQLException {

        return new Property(
                resultSet.getInt("property_id"),
                resultSet.getString("title"),
                PropertyPurpose.valueOf(resultSet.getString("purpose")),
                PropertyType.valueOf(resultSet.getString("property_type")),
                resultSet.getString("city"),
                resultSet.getString("locality"),
                resultSet.getString("property_address"),
                resultSet.getDouble("price"),
                resultSet.getInt("bedrooms"),
                resultSet.getInt("bathrooms"),
                resultSet.getDouble("area"),
                FurnishingStatus.valueOf(
                        resultSet.getString("furnishing_status")),
                resultSet.getString("description"),
                ListingStatus.valueOf(
                        resultSet.getString("listing_status")),
                resultSet.getInt("owner_id")
        );
    }
}
