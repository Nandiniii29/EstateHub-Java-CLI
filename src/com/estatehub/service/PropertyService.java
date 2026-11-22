package com.estatehub.service;

import com.estatehub.model.*;
import com.estatehub.repository.PropertyRepository;
import com.estatehub.util.PropertyValidator;

import java.util.List;

public class PropertyService {

    private PropertyRepository repository;

    public PropertyService(PropertyRepository repository) {
        this.repository = repository;
    }

    public void addProperty(Property property) {

        if (property == null) {
            throw new IllegalArgumentException("Property cannot be null");
        }

        if (property.getPropertyId() <= 0) {
            throw new IllegalArgumentException("Invalid property ID");
        }

        if (repository.findById(property.getPropertyId()) != null) {
            throw new IllegalArgumentException("Property ID already exists");
        }

        if (!PropertyValidator.isValidPrice(property.getPrice())) {
            throw new IllegalArgumentException("Invalid property price");
        }

        if (!PropertyValidator.isValidArea(property.getArea())) {
            throw new IllegalArgumentException("Invalid property area");
        }

        if (!PropertyValidator.isValidLocation(property.getCity())
                || !PropertyValidator.isValidLocation(property.getLocality())) {
            throw new IllegalArgumentException("Invalid location");
        }

        if (property.getTitle() == null
                || property.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }

        if (property.getPurpose() == null || property.getType() == null
                || property.getFurnishingStatus() == null
                || property.getListingStatus() == null) {
            throw new IllegalArgumentException("Property details are incomplete");
        }

        if (!PropertyValidator.isValidRoomCount(property.getBedrooms())
                || !PropertyValidator.isValidRoomCount(property.getBathrooms())) {
            throw new IllegalArgumentException("Invalid room count");
        }

        repository.addProperty(property);
    }

    public List<Property> getAllProperties() {
        return repository.findAll();
    }

    public Property getPropertyById(int propertyId) {
        return repository.findById(propertyId);
    }

    public boolean updateProperty(Property property) {
        return repository.updateProperty(property);
    }

    public boolean deleteProperty(int propertyId) {
        return repository.deleteProperty(propertyId);
    }
}