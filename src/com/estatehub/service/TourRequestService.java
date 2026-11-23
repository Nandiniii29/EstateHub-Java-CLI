package com.estatehub.service;
import com.estatehub.model.TourRequest; import com.estatehub.exceptions.Member2ValidationException; import java.time.LocalDate; import java.util.*;
/** Collects tour requests for later coordination with the shared tour-processing module. */
public class TourRequestService {
    private final List<TourRequest> requests=new ArrayList<>(); private int nextId=1;
    public TourRequest requestTour(String userId,String propertyId,LocalDate date,String details) throws Member2ValidationException { if(userId==null||userId.isBlank()) throw new Member2ValidationException("User ID is required."); if(propertyId==null||propertyId.isBlank()) throw new Member2ValidationException("Property ID is required."); if(date==null||date.isBefore(LocalDate.now())) throw new Member2ValidationException("Preferred date must be today or later."); TourRequest r=new TourRequest("T"+nextId++,userId.trim(),propertyId.trim(),date,details==null?"":details.trim()); requests.add(r); return r; }
    public List<TourRequest> getRequestsForUser(String userId){List<TourRequest> result=new ArrayList<>(); for(TourRequest r:requests) if(r.getUserId().equals(userId)) result.add(r); return result;}
    public List<TourRequest> getAllRequests(){return Collections.unmodifiableList(new ArrayList<>(requests));}
}
