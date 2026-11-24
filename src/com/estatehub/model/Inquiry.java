
package com.estatehub.model;

import java.time.LocalDateTime;

/** Customer inquiry record. */
public class Inquiry {

    public enum Status {
        PENDING, SUBMITTED, CANCELLED
    }

    private final String inquiryId, userId, propertyId, inquiryType, message;
    private final LocalDateTime createdAt;
    private Status status;

    // Constructor for creating a new inquiry
    public Inquiry(String inquiryId, String userId, String propertyId,
                   String inquiryType, String message) {

        this.inquiryId = inquiryId;
        this.userId = userId;
        this.propertyId = propertyId;
        this.inquiryType = inquiryType;
        this.message = message;
        this.createdAt = LocalDateTime.now();
        this.status = Status.PENDING;
    }

    // Constructor for retrieving an existing inquiry from database
    public Inquiry(String inquiryId, String userId, String propertyId,
                   String inquiryType, String message,
                   LocalDateTime createdAt, Status status) {

        this.inquiryId = inquiryId;
        this.userId = userId;
        this.propertyId = propertyId;
        this.inquiryType = inquiryType;
        this.message = message;
        this.createdAt = createdAt;
        this.status = status;
    }

    public String getInquiryId() {
        return inquiryId;
    }

    public String getUserId() {
        return userId;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public String getInquiryType() {
        return inquiryType;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Inquiry{" + inquiryId +
                ", property=" + propertyId +
                ", type=" + inquiryType +
                ", status=" + status +
                ", created=" + createdAt + "}";
    }
}
