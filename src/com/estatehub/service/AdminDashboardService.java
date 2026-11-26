
package com.estatehub.service;

import com.estatehub.model.ListingStatus;
import com.estatehub.model.Property;
import com.estatehub.repository.PropertyRepository;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminDashboardService {

   private final PropertyRepository repository;

public AdminDashboardService(PropertyRepository repository) {
    if (repository == null) {
        throw new IllegalArgumentException(
            "PropertyRepository cannot be null."
        );
    }

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
    // Count properties according to BUY or RENT
// Count properties according to BUY or RENT
// Count properties according to BUY or RENT
public Map<String, Long> getPropertiesByPurpose() {
    return repository.findAll()
            .stream()
            .collect(Collectors.groupingBy(
                    property -> property.getPurpose().name(),
                    Collectors.counting()
            ));
}
// Group properties according to city
public Map<String, Long> getPropertiesByCity() {
    return repository.findAll()
            .stream()
            .collect(Collectors.groupingBy(
                    Property::getCity,
                    Collectors.counting()
            ));
}

// Group properties according to property type
public Map<String, Long> getPropertiesByType() {
    return repository.findAll()
            .stream()
            .collect(Collectors.groupingBy(
                    property -> property.getType().name(),
                    Collectors.counting()
            ));
}
}
