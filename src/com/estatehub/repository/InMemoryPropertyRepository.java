package com.estatehub.repository;

import com.estatehub.model.Property;
import java.util.ArrayList;
import java.util.List;

public class InMemoryPropertyRepository implements PropertyRepository {

    private List<Property> properties = new ArrayList<>();

    @Override
    public void addProperty(Property property) {
        properties.add(property);
    }

    @Override
    public List<Property> findAll() {
        return new ArrayList<>(properties);
    }

    @Override
    public Property findById(int propertyId) {
        for (Property property : properties) {
            if (property.getPropertyId() == propertyId) {
                return property;
            }
        }
        return null;
    }

    @Override
    public boolean updateProperty(Property updatedProperty) {
        for (int i = 0; i < properties.size(); i++) {
            if (properties.get(i).getPropertyId()
                    == updatedProperty.getPropertyId()) {
                properties.set(i, updatedProperty);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteProperty(int propertyId) {
        return properties.removeIf(
                property -> property.getPropertyId() == propertyId
        );
    }
}