package com.estatehub.service;


import com.estatehub.model.User;
import com.estatehub.exceptions.Member2ValidationException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/** In-memory registration, login, logout and profile operations for Member 2. */
public class UserService {
    private final Map<String, User> usersByEmail = new HashMap<>();
    private User currentUser;
    private int nextId = 1;

    public User register(String name, String email, String phone, String password) throws Member2ValidationException {
        validateName(name); validateEmail(email); validatePhone(phone); validatePassword(password);
        String key = email.trim().toLowerCase(Locale.ROOT);
        if (usersByEmail.containsKey(key)) throw new Member2ValidationException("An account with this email already exists.");
        User user = new User("U" + nextId++, name.trim(), key, phone.trim(), hash(password), User.Role.CUSTOMER);
        usersByEmail.put(key, user);
        return user;
    }
    public User login(String email, String password) throws Member2ValidationException {
        if (email == null || password == null) throw new Member2ValidationException("Email and password are required.");
        User user = usersByEmail.get(email.trim().toLowerCase(Locale.ROOT));
        if (user == null || !user.passwordMatches(hash(password))) throw new Member2ValidationException("Invalid email or password.");
        currentUser = user;
        return user;
    }
    public void logout() { currentUser = null; }
    public User getCurrentUser() { return currentUser; }
    public User findByEmail(String email) { return email == null ? null : usersByEmail.get(email.trim().toLowerCase(Locale.ROOT)); }
    public User updateProfile(String name, String phone) throws Member2ValidationException {
        requireLoggedIn(); validateName(name); validatePhone(phone);
        currentUser.updateProfile(name.trim(), phone.trim());
        return currentUser;
    }
    public void changePassword(String oldPassword, String newPassword) throws Member2ValidationException {
        requireLoggedIn(); validatePassword(newPassword);
        if (!currentUser.passwordMatches(hash(oldPassword))) throw new Member2ValidationException("Current password is incorrect.");
        currentUser.changePasswordHash(hash(newPassword));
    }
    public List<User> getAllUsers() { return Collections.unmodifiableList(new ArrayList<>(usersByEmail.values())); }
    private void requireLoggedIn() throws Member2ValidationException { if (currentUser == null) throw new Member2ValidationException("Please log in first."); }
    private static void validateName(String name) throws Member2ValidationException { if (name == null || name.trim().length() < 2 || !name.trim().matches("[A-Za-z][A-Za-z .'-]*")) throw new Member2ValidationException("Name must be at least 2 characters and contain valid letters."); }
    private static void validateEmail(String email) throws Member2ValidationException { if (email == null || !email.trim().matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")) throw new Member2ValidationException("Enter a valid email address."); }
    private static void validatePhone(String phone) throws Member2ValidationException { if (phone == null || !phone.trim().matches("[0-9+() .-]{7,20}")) throw new Member2ValidationException("Enter a valid phone number (7-20 characters)."); }
    private static void validatePassword(String password) throws Member2ValidationException { if (password == null || password.length() < 8 || !password.matches(".*[A-Za-z].*") || !password.matches(".*\\d.*")) throw new Member2ValidationException("Password must be at least 8 characters and contain a letter and a number."); }
    private static String hash(String password) {
        try { MessageDigest md = MessageDigest.getInstance("SHA-256"); byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8)); StringBuilder out = new StringBuilder(); for (byte b : bytes) out.append(String.format("%02x", b)); return out.toString(); }
        catch (NoSuchAlgorithmException e) { throw new IllegalStateException("SHA-256 is unavailable", e); }
    }
}
