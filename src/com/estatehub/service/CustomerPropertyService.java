package com.estatehub.service;


import com.estatehub.exceptions.Member2ValidationException;
import java.util.*;

/** Customer property interactions use property IDs to avoid coupling to the shared Property model. */
public class CustomerPropertyService {
    private final Map<String, Set<String>> favorites = new HashMap<>();
    private final Map<String, LinkedList<String>> recentlyViewed = new HashMap<>();
    private final Map<String, List<String>> comparison = new HashMap<>();

    public boolean addFavorite(String userId, String propertyId) throws Member2ValidationException { validate(userId, propertyId); return favorites.computeIfAbsent(userId, k -> new LinkedHashSet<>()).add(propertyId.trim()); }
    public boolean removeFavorite(String userId, String propertyId) throws Member2ValidationException { validate(userId, propertyId); Set<String> set = favorites.get(userId); return set != null && set.remove(propertyId.trim()); }
    public List<String> getFavorites(String userId) { return copyList(favorites.get(userId)); }
    public void recordViewed(String userId, String propertyId) throws Member2ValidationException {
        validate(userId, propertyId); LinkedList<String> history = recentlyViewed.computeIfAbsent(userId, k -> new LinkedList<>()); history.remove(propertyId.trim()); history.addFirst(propertyId.trim());
    }
    public List<String> getRecentlyViewed(String userId) { return copyList(recentlyViewed.get(userId)); }
    public boolean addToComparison(String userId, String propertyId) throws Member2ValidationException { validate(userId, propertyId); List<String> list = comparison.computeIfAbsent(userId, k -> new ArrayList<>()); if (list.contains(propertyId.trim())) return false; list.add(propertyId.trim()); return true; }
    public boolean removeFromComparison(String userId, String propertyId) throws Member2ValidationException { validate(userId, propertyId); List<String> list = comparison.get(userId); return list != null && list.remove(propertyId.trim()); }
    public List<String> getComparison(String userId) { return copyList(comparison.get(userId)); }
    private static List<String> copyList(Collection<String> source) { return source == null ? new ArrayList<>() : new ArrayList<>(source); }
    private static void validate(String userId, String propertyId) throws Member2ValidationException { if (userId == null || userId.isBlank()) throw new Member2ValidationException("User ID is required."); if (propertyId == null || propertyId.isBlank()) throw new Member2ValidationException("Property ID is required."); }
}
