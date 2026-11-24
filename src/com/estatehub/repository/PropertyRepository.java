package com.estatehub.repository;

import com.estatehub.model.Property;
import java.util.List;

public interface PropertyRepository {

    void addProperty(Property property);

    List<Property> findAll();

    Property findById(int propertyId);

    boolean updateProperty(Property property);

    boolean deleteProperty(int propertyId);
}