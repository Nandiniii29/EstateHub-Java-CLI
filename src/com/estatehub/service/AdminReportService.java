
package com.estatehub.service;

import com.estatehub.model.Inquiry;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminReportService {

    private InquiryService inquiryService;

    public AdminReportService(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    // Total number of inquiries
    public long getTotalInquiries() {
        return inquiryService.getAllInquiries()
                .stream()
                .count();
    }

    // Count pending inquiries
    public long getPendingInquiries() {
        return inquiryService.getAllInquiries()
                .stream()
                .filter(i -> i.getStatus() == Inquiry.Status.PENDING)
                .count();
    }

    // Group inquiries according to their type
    public Map<String, Long> getInquiriesByType() {
        return inquiryService.getAllInquiries()
                .stream()
                .collect(Collectors.groupingBy(
                        Inquiry::getInquiryType,
                        Collectors.counting()
                ));
    }

    // Count inquiries for each property
    public Map<String, Long> getInquiriesByProperty() {
        return inquiryService.getAllInquiries()
                .stream()
                .collect(Collectors.groupingBy(
                        Inquiry::getPropertyId,
                        Collectors.counting()
                ));
    }

    // Group inquiries according to their status
    public Map<String, Long> getInquiriesByStatus() {
        return inquiryService.getAllInquiries()
                .stream()
                .collect(Collectors.groupingBy(
                        inquiry -> inquiry.getStatus().name(),
                        Collectors.counting()
                ));
    }

}
