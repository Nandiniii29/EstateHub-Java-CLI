
package com.estatehub.main;

import com.estatehub.model.*;
import com.estatehub.repository.*;
import com.estatehub.service.*;
import com.estatehub.util.EMICalculator;

public class PropertyModuleTest {

    public static void main(String[] args) {

        PropertyRepository repository =
                new InMemoryPropertyRepository();

        PropertyService propertyService =
                new PropertyService(repository);

        PropertySearchService searchService =
                new PropertySearchService(propertyService);

        // Add sample properties
        Property p1 = new Property(
                101, "Luxury Apartment",
                PropertyPurpose.BUY,
                PropertyType.APARTMENT,
                "Patiala", "Urban Estate", "Street 1",
                7500000, 3, 2, 1500,
                FurnishingStatus.FURNISHED,
                "Luxury apartment with modern facilities",
                ListingStatus.APPROVED, 1
        );

        Property p2 = new Property(
                102, "Family Villa",
                PropertyPurpose.RENT,
                PropertyType.VILLA,
                "Chandigarh", "Sector 35", "Street 2",
                35000, 4, 3, 2200,
                FurnishingStatus.SEMI_FURNISHED,
                "Spacious family villa",
                ListingStatus.APPROVED, 2
        );

        propertyService.addProperty(p1);
        propertyService.addProperty(p2);

        System.out.println("===== ALL PROPERTIES =====");
        for (Property p : propertyService.getAllProperties()) {
            System.out.println(p);
            System.out.println("----------------");
        }

        System.out.println("\n===== SEARCH BY CITY =====");
        System.out.println(
                searchService.searchByCity("Patiala").size()
        );

        System.out.println("\n===== SEARCH BY ID =====");
        System.out.println(
                searchService.searchById(101)
        );

        System.out.println("\n===== FILTER BY PURPOSE =====");
        System.out.println(
                searchService.filterByPurpose(
                        PropertyPurpose.BUY
                ).size()
        );

        System.out.println("\n===== FILTER BY TYPE =====");
        System.out.println(
                searchService.filterByType(
                        PropertyType.VILLA
                ).size()
        );

        System.out.println("\n===== FILTER BY PRICE =====");
        System.out.println(
                searchService.filterByPrice(30000, 50000).size()
        );

        System.out.println("\n===== SORT BY PRICE =====");
        for (Property p : searchService.sortByPrice()) {
            System.out.println(p.getTitle() + " - " + p.getPrice());
        }

        System.out.println("\n===== SORT BY AREA =====");
        for (Property p : searchService.sortByArea()) {
            System.out.println(p.getTitle() + " - " + p.getArea());
        }

        System.out.println("\n===== EMI CALCULATOR =====");
        double emi = EMICalculator.calculateEMI(
                4000000, 8.5, 20
        );
        System.out.printf("Monthly EMI: %.2f%n", emi);

        System.out.println("\n===== DELETE PROPERTY =====");
        System.out.println(
                propertyService.deleteProperty(102)
        );

        System.out.println(
                "Remaining properties: "
                + propertyService.getAllProperties().size()
        );
    }
}
