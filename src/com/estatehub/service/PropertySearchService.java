package com.estatehub.service;

import com.estatehub.model.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PropertySearchService {

    private PropertyService propertyService;

    public PropertySearchService(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    public Property searchById(int id) {
        return propertyService.getPropertyById(id);
    }

    public List<Property> searchByCity(String city) {
        List<Property> result = new ArrayList<>();

        for (Property p : propertyService.getAllProperties()) {
            if (p.getCity().equalsIgnoreCase(city)) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Property> searchByLocality(String locality) {
        List<Property> result = new ArrayList<>();

        for (Property p : propertyService.getAllProperties()) {
            if (p.getLocality().equalsIgnoreCase(locality)) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Property> filterByPurpose(PropertyPurpose purpose) {
        List<Property> result = new ArrayList<>();

        for (Property p : propertyService.getAllProperties()) {
            if (p.getPurpose() == purpose) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Property> filterByType(PropertyType type) {
        List<Property> result = new ArrayList<>();

        for (Property p : propertyService.getAllProperties()) {
            if (p.getType() == type) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Property> filterByPrice(double min, double max) {
        List<Property> result = new ArrayList<>();

        for (Property p : propertyService.getAllProperties()) {
            if (p.getPrice() >= min && p.getPrice() <= max) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Property> sortByPrice() {
        List<Property> result =
                new ArrayList<>(propertyService.getAllProperties());

        result.sort(Comparator.comparingDouble(Property::getPrice));
        return result;
    }

    public List<Property> sortByArea() {
        List<Property> result =
                new ArrayList<>(propertyService.getAllProperties());

        result.sort(Comparator.comparingDouble(Property::getArea));
        return result;
    }
}