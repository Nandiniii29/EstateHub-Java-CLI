package com.estatehub.service;
import com.estatehub.model.Inquiry;
import com.estatehub.exceptions.Member2ValidationException;
import java.util.*;
/** Stores customer inquiries in memory and supports history and cancellation. */
public class InquiryService {
    private final Map<String, Inquiry> inquiries = new LinkedHashMap<>(); private int nextId=1;
    public Inquiry submit(String userId,String propertyId,String type,String message) throws Member2ValidationException { require(userId,"User ID"); require(propertyId,"Property ID"); require(type,"Inquiry type"); require(message,"Message"); Inquiry i=new Inquiry("I"+nextId++,userId.trim(),propertyId.trim(),type.trim(),message.trim()); inquiries.put(i.getInquiryId(),i); return i; }
    public List<Inquiry> getHistory(String userId){List<Inquiry> result=new ArrayList<>(); for(Inquiry i:inquiries.values()) if(i.getUserId().equals(userId)) result.add(i); return result;}
    public boolean cancel(String userId,String inquiryId) throws Member2ValidationException { require(userId,"User ID"); Inquiry i=inquiries.get(inquiryId); if(i==null || !i.getUserId().equals(userId)) return false; if(i.getStatus()!=Inquiry.Status.PENDING) return false; i.setStatus(Inquiry.Status.CANCELLED); return true; }
    private static void require(String value,String label) throws Member2ValidationException { if(value==null || value.isBlank()) throw new Member2ValidationException(label+" is required."); }
}
