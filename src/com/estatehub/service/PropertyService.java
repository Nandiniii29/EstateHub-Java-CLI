
package com.estatehub.service;

import com.estatehub.model.Property;
import com.estatehub.repository.PropertyRepository;
import com.estatehub.exceptions.InvalidPropertyException;
import com.estatehub.exceptions.PropertyNotFoundException;
import com.estatehub.util.PropertyValidator;

import java.util.List;

public class PropertyService {

    private PropertyRepository repository;

    public PropertyService(PropertyRepository repository) {
        this.repository = repository;
    }

    public void addProperty(Property property) {

        validateProperty(property);

        if (repository.findById(property.getPropertyId()) != null) {
            throw new InvalidPropertyException("Property ID already exists.");
        }

        repository.addProperty(property);
    }

    public List<Property> getAllProperties() {
        return repository.findAll();
    }

    public Property getPropertyById(int propertyId) {

        if (!PropertyValidator.isValidPropertyId(propertyId)) {
            throw new InvalidPropertyException("Property ID must be positive.");
        }

        Property property = repository.findById(propertyId);

        if (property == null) {
            throw new PropertyNotFoundException(
                "Property not found with ID: " + propertyId
            );
        }

        return property;
    }

    public boolean updateProperty(Property property) {

        validateProperty(property);

        if (repository.findById(property.getPropertyId()) == null) {
            throw new PropertyNotFoundException(
                "Cannot update. Property not found: " + property.getPropertyId()
            );
        }

        return repository.updateProperty(property);
    }

    public boolean deleteProperty(int propertyId) {

        if (repository.findById(propertyId) == null) {
            throw new PropertyNotFoundException(
                "Cannot delete. Property not found: " + propertyId
            );
        }

        return repository.deleteProperty(propertyId);
    }

    private void validateProperty(Property property) {

        if (property == null) {
            throw new InvalidPropertyException("Property cannot be null.");
        }

        if (!PropertyValidator.isValidPropertyId(property.getPropertyId())) {
            throw new InvalidPropertyException("Invalid property ID.");
        }

        if (!PropertyValidator.isValidTitle(property.getTitle())) {
            throw new InvalidPropertyException("Title cannot be empty.");
        }

        if (property.getPurpose() == null ||
            property.getType() == null ||
            property.getFurnishingStatus() == null ||
            property.getListingStatus() == null) {
            throw new InvalidPropertyException("Property enum values cannot be null.");
        }

        if (!PropertyValidator.isValidLocation(property.getCity()) ||
            !PropertyValidator.isValidLocation(property.getLocality())) {
            throw new InvalidPropertyException("City and locality are required.");
        }

        if (!PropertyValidator.isValidPrice(property.getPrice())) {
            throw new InvalidPropertyException("Price must be greater than zero.");
        }

        if (!PropertyValidator.isValidArea(property.getArea())) {
            throw new InvalidPropertyException("Area must be greater than zero.");
        }

        if (!PropertyValidator.isValidRoomCount(property.getBedrooms()) ||
            !PropertyValidator.isValidRoomCount(property.getBathrooms())) {
            throw new InvalidPropertyException("Room counts cannot be negative.");
        }
    }
}
