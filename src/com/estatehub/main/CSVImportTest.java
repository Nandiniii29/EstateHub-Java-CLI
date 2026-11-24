package com.estatehub.main;

import com.estatehub.repository.PropertyRepository;
import com.estatehub.repository.MySQLPropertyRepository;
import com.estatehub.service.PropertyService;
import com.estatehub.service.CSVImportService;

public class CSVImportTest {

    public static void main(String[] args) {

        PropertyRepository repository =
                new MySQLPropertyRepository();

        PropertyService propertyService =
                new PropertyService(repository);

        CSVImportService csvImportService =
                new CSVImportService(propertyService);

        System.out.println("Starting CSV Import...");

        csvImportService.importProperties("sample-properties.csv");

        System.out.println("CSV Import Test Completed.");
    }
}