
package com.estatehub.service;

import com.estatehub.model.Inquiry;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CSVExportService {

    private final InquiryService inquiryService;

    public CSVExportService(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    public String exportInquiries() throws IOException {

        List<Inquiry> inquiries =
                inquiryService.getAllInquiries();

        Path folder = Paths.get("exports");

        Files.createDirectories(folder);

        Path file = folder.resolve("inquiries_report.csv");

        try (BufferedWriter writer = Files.newBufferedWriter(
                file, StandardCharsets.UTF_8)) {

            // CSV column headings
            writer.write("Inquiry ID,User ID,Property ID,Inquiry Type,Message,Status,Created At");
            writer.newLine();

            // Export each inquiry
            for (Inquiry inquiry : inquiries) {

                writer.write(
                        escape(inquiry.getInquiryId()) + "," +
                        escape(inquiry.getUserId()) + "," +
                        escape(inquiry.getPropertyId()) + "," +
                        escape(inquiry.getInquiryType()) + "," +
                        escape(inquiry.getMessage()) + "," +
                        escape(inquiry.getStatus().name()) + "," +
                        escape(inquiry.getCreatedAt().toString())
                );

                writer.newLine();
            }
        }

        return file.toAbsolutePath().toString();
    }

    // Handles commas, quotes and new lines in CSV values
    private String escape(String value) {

        if (value == null) {
            return "";
        }

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
