
package com.estatehub.service;

import com.estatehub.model.Property;
import com.estatehub.model.ListingStatus;
import com.estatehub.repository.PropertyRepository;
import com.estatehub.exceptions.InvalidInputException;
//import com.estatehub.exceptions.PropertyNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class AdminPropertyService {

    private PropertyService propertyService;

    public AdminPropertyService(PropertyRepository repository) {
        this.propertyService = new PropertyService(repository);
    }

    // View all properties waiting for admin approval
    public List<Property> getPendingProperties() {

        List<Property> pendingProperties = new ArrayList<>();

        for (Property property : propertyService.getAllProperties()) {

            if (property.getListingStatus() == ListingStatus.PENDING) {
                pendingProperties.add(property);
            }
        }

        return pendingProperties;
    }

    // Approve a pending property
    public void approveProperty(int propertyId)
            throws InvalidInputException {

        if (propertyId <= 0) {
            throw new InvalidInputException("Property ID must be positive.");
        }

        Property property = propertyService.getPropertyById(propertyId);

        if (property.getListingStatus() != ListingStatus.PENDING) {
            throw new InvalidInputException(
                "Only pending properties can be approved."
            );
        }

        property.setListingStatus(ListingStatus.APPROVED);

        propertyService.updateProperty(property);
    }

    // Reject a pending property
    public void rejectProperty(int propertyId)
            throws InvalidInputException {

        if (propertyId <= 0) {
            throw new InvalidInputException("Property ID must be positive.");
        }

        Property property = propertyService.getPropertyById(propertyId);

        if (property.getListingStatus() != ListingStatus.PENDING) {
            throw new InvalidInputException(
                "Only pending properties can be rejected."
            );
        }

        property.setListingStatus(ListingStatus.REJECTED);

        propertyService.updateProperty(property);
    }
}
