
package com.estatehub.service;

import com.estatehub.model.Property;
import com.estatehub.model.ListingStatus;
import com.estatehub.repository.PropertyRepository;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class AdminDashboardService {

    private PropertyRepository repository;

    public AdminDashboardService(PropertyRepository repository) {
        this.repository = repository;
    }

    public Map<ListingStatus, Integer> getPropertyStatistics() {

        List<Property> properties = repository.findAll();

        Map<ListingStatus, Integer> statistics =
                new EnumMap<>(ListingStatus.class);

        // Initially set every status count to zero
        for (ListingStatus status : ListingStatus.values()) {
            statistics.put(status, 0);
        }

        // Count properties according to their status
        for (Property property : properties) {

            ListingStatus status = property.getListingStatus();

            statistics.put(
                status,
                statistics.get(status) + 1
            );
        }

        return statistics;
    }

    public int getTotalProperties() {
        return repository.findAll().size();
    }
}
