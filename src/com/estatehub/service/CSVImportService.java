
package com.estatehub.service;

import com.estatehub.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CSVImportService {

    private final PropertyService propertyService;

    public CSVImportService(PropertyService propertyService) {
        if (propertyService == null) {
            throw new IllegalArgumentException(
                "PropertyService cannot be null."
            );
        }

        this.propertyService = propertyService;
    }

    public void importProperties(String filePath) {

        int successful = 0;
        int failed = 0;
        int rowNumber = 1;

        try (BufferedReader reader =
                     Files.newBufferedReader(Path.of(filePath))) {

            String header = reader.readLine();

            if (header == null) {
                System.out.println("CSV file is empty.");
                return;
            }

            String line;

            while ((line = reader.readLine()) != null) {

                rowNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    List<String> values = parseCSVLine(line);

                    if (values.size() != 15) {
                        throw new IllegalArgumentException(
                            "Expected 15 columns, found " + values.size()
                        );
                    }

                    Property property = new Property(
                        Integer.parseInt(values.get(0).trim()),
                        values.get(1).trim(),
                        PropertyPurpose.valueOf(
                            values.get(2).trim().toUpperCase()
                        ),
                        PropertyType.valueOf(
                            values.get(3).trim().toUpperCase()
                        ),
                        values.get(4).trim(),
                        values.get(5).trim(),
                        values.get(6).trim(),
                        Double.parseDouble(values.get(7).trim()),
                        Integer.parseInt(values.get(8).trim()),
                        Integer.parseInt(values.get(9).trim()),
                        Double.parseDouble(values.get(10).trim()),
                        FurnishingStatus.valueOf(
                            values.get(11).trim().toUpperCase()
                        ),
                        values.get(12).trim(),
                        ListingStatus.valueOf(
                            values.get(13).trim().toUpperCase()
                        ),
                        Integer.parseInt(values.get(14).trim())
                    );

                    propertyService.addProperty(property);

                    successful++;

                    System.out.println(
                        "Row " + rowNumber +
                        ": Property imported successfully."
                    );

                } catch (RuntimeException e) {

                    failed++;

                    System.out.println(
                        "Row " + rowNumber +
                        " failed: " + e.getMessage()
                    );
                }
            }

            System.out.println("\n========== CSV IMPORT REPORT ==========");
            System.out.println("Successful imports: " + successful);
            System.out.println("Failed imports: " + failed);
            System.out.println("========================================");

        } catch (IOException e) {

            System.out.println(
                "Error reading CSV file: " + e.getMessage()
            );

        } catch (RuntimeException e) {

            System.out.println(
                "CSV import could not be completed: " + e.getMessage()
            );
        }
    }

    private List<String> parseCSVLine(String line) {

        List<String> values = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();

        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {

            char current = line.charAt(i);

            if (current == '"') {

                if (insideQuotes
                        && i + 1 < line.length()
                        && line.charAt(i + 1) == '"') {

                    currentValue.append('"');
                    i++;

                } else {
                    insideQuotes = !insideQuotes;
                }

            } else if (current == ',' && !insideQuotes) {

                values.add(currentValue.toString());
                currentValue.setLength(0);

            } else {
                currentValue.append(current);
            }
        }

        if (insideQuotes) {
            throw new IllegalArgumentException(
                "Unclosed quotation mark."
            );
        }

        values.add(currentValue.toString());

        return values;
    }
}
