package com.estatehub.model;
import java.time.LocalDate;
/** Customer request to visit a property. */
public class TourRequest {
    private final String requestId,userId,propertyId,details; private final LocalDate preferredDate;
    public TourRequest(String requestId,String userId,String propertyId,LocalDate preferredDate,String details){this.requestId=requestId;this.userId=userId;this.propertyId=propertyId;this.preferredDate=preferredDate;this.details=details;}
    public String getRequestId(){return requestId;} public String getUserId(){return userId;} public String getPropertyId(){return propertyId;} public LocalDate getPreferredDate(){return preferredDate;} public String getDetails(){return details;}
    @Override public String toString(){return "TourRequest{" + requestId + ", property=" + propertyId + ", preferredDate=" + preferredDate + ", details='" + details + "'}";}
}
