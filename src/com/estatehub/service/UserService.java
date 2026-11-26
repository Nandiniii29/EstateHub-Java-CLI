
package com.estatehub.service;

import com.estatehub.database.DatabaseConnection;
import com.estatehub.model.User;
import com.estatehub.exceptions.Member2ValidationException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

public class UserService {

    private User currentUser;

    // CUSTOMER REGISTRATION

    public User register(String name, String email, String phone, String password)
            throws Member2ValidationException {

        validateName(name);
        validateEmail(email);
        validatePhone(phone);
        validatePassword(password);

        String key = email.trim().toLowerCase(Locale.ROOT);
        String userId = "U" + UUID.randomUUID();

        String sql = "INSERT INTO users "
                + "(user_id, name, email, phone, password_hash, role) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userId);
            ps.setString(2, name.trim());
            ps.setString(3, key);
            ps.setString(4, phone.trim());
            ps.setString(5, hash(password));
            ps.setString(6, "CUSTOMER");

            ps.executeUpdate();

            return new User(
                    userId, name.trim(), key, phone.trim(),
                    hash(password), User.Role.CUSTOMER
            );

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new Member2ValidationException(
                    "An account with this email already exists."
            );
        } catch (SQLException e) {
            throw new Member2ValidationException(
                    "Registration failed: " + e.getMessage()
            );
        }
    }

    // INITIAL ADMIN REGISTRATION

    public User registerAdmin(
            String name,
            String email,
            String phone,
            String password,
            String setupKey
    ) throws Member2ValidationException {

        validateName(name);
        validateEmail(email);
        validatePhone(phone);
        validatePassword(password);

        String configuredKey =
                System.getenv("ESTATEHUB_ADMIN_SETUP_KEY");

        if (configuredKey == null || configuredKey.isBlank()) {
            throw new Member2ValidationException(
                    "Admin setup key is not configured."
            );
        }

        if (setupKey == null || !configuredKey.equals(setupKey)) {
            throw new Member2ValidationException(
                    "Invalid admin setup key."
            );
        }

        String userId = "U" + UUID.randomUUID();

        String countSql =
                "SELECT COUNT(*) FROM users WHERE role = 'ADMIN'";

        String insertSql =
                "INSERT INTO users "
                + "(user_id, name, email, phone, password_hash, role) "
                + "VALUES (?, ?, ?, ?, ?, 'ADMIN')";

        try (Connection con = DatabaseConnection.getConnection()) {

            try (PreparedStatement countPs =
                         con.prepareStatement(countSql);
                 ResultSet rs = countPs.executeQuery()) {

                if (rs.next() && rs.getInt(1) > 0) {
                    throw new Member2ValidationException(
                            "An admin account already exists. "
                            + "Initial admin registration is disabled."
                    );
                }
            }

            try (PreparedStatement ps =
                         con.prepareStatement(insertSql)) {

                ps.setString(1, userId);
                ps.setString(2, name.trim());
                ps.setString(
                        3, email.trim().toLowerCase(Locale.ROOT)
                );
                ps.setString(4, phone.trim());
                ps.setString(5, hash(password));

                ps.executeUpdate();

                return new User(
                        userId,
                        name.trim(),
                        email.trim().toLowerCase(Locale.ROOT),
                        phone.trim(),
                        hash(password),
                        User.Role.ADMIN
                );
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new Member2ValidationException(
                    "An account with this email already exists."
            );
        } catch (SQLException e) {
            throw new Member2ValidationException(
                    "Admin registration failed: " + e.getMessage()
            );
        }
    }

    // LOGIN

    public User login(String email, String password)
            throws Member2ValidationException {

        if (email == null || password == null) {
            throw new Member2ValidationException(
                    "Email and password are required."
            );
        }

        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email.trim().toLowerCase(Locale.ROOT));

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    throw new Member2ValidationException(
                            "Invalid email or password."
                    );
                }

                String storedHash = rs.getString("password_hash");

                if (!storedHash.equals(hash(password))) {
                    throw new Member2ValidationException(
                            "Invalid email or password."
                    );
                }

                currentUser = new User(
                        rs.getString("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        storedHash,
                        User.Role.valueOf(rs.getString("role"))
                );

                return currentUser;
            }

        } catch (SQLException e) {
            throw new Member2ValidationException(
                    "Login failed: " + e.getMessage()
            );
        }
    }

    // LOGOUT

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // FIND USER BY EMAIL

    public User findByEmail(String email) {

        if (email == null) {
            return null;
        }

        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email.trim().toLowerCase(Locale.ROOT));

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return null;
                }

                return new User(
                        rs.getString("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("password_hash"),
                        User.Role.valueOf(rs.getString("role"))
                );
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Could not find user: " + e.getMessage(), e
            );
        }
    }

    // UPDATE PROFILE

    public User updateProfile(String name, String phone)
            throws Member2ValidationException {

        requireLoggedIn();
        validateName(name);
        validatePhone(phone);

        String sql =
                "UPDATE users SET name = ?, phone = ? WHERE user_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name.trim());
            ps.setString(2, phone.trim());
            ps.setString(3, currentUser.getUserId());

            ps.executeUpdate();

            currentUser.updateProfile(name.trim(), phone.trim());

            return currentUser;

        } catch (SQLException e) {
            throw new Member2ValidationException(
                    "Profile update failed: " + e.getMessage()
            );
        }
    }

    // CHANGE PASSWORD

    public void changePassword(String oldPassword, String newPassword)
            throws Member2ValidationException {

        requireLoggedIn();
        validatePassword(newPassword);

        if (oldPassword == null
                || !currentUser.passwordMatches(hash(oldPassword))) {

            throw new Member2ValidationException(
                    "Current password is incorrect."
            );
        }

        String newHash = hash(newPassword);

        String sql =
                "UPDATE users SET password_hash = ? WHERE user_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newHash);
            ps.setString(2, currentUser.getUserId());

            ps.executeUpdate();

            currentUser.changePasswordHash(newHash);

        } catch (SQLException e) {
            throw new Member2ValidationException(
                    "Password change failed: " + e.getMessage()
            );
        }
    }

    // GET ALL USERS

    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                        rs.getString("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("password_hash"),
                        User.Role.valueOf(rs.getString("role"))
                ));
            }

            return Collections.unmodifiableList(users);

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Could not retrieve users: " + e.getMessage(), e
            );
        }
    }

    // LOGIN VALIDATION

    private void requireLoggedIn()
            throws Member2ValidationException {

        if (currentUser == null) {
            throw new Member2ValidationException(
                    "Please log in first."
            );
        }
    }

    // NAME VALIDATION

    private static void validateName(String name)
            throws Member2ValidationException {

        if (name == null || name.trim().length() < 2
                || !name.trim().matches("[A-Za-z][A-Za-z .'-]*")) {

            throw new Member2ValidationException(
                    "Name must be at least 2 characters and contain valid letters."
            );
        }
    }

    // EMAIL VALIDATION

    private static void validateEmail(String email)
            throws Member2ValidationException {

        if (email == null || !email.trim().matches(
                "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")) {

            throw new Member2ValidationException(
                    "Enter a valid email address."
            );
        }
    }

    // PHONE VALIDATION

    private static void validatePhone(String phone)
            throws Member2ValidationException {

        if (phone == null || !phone.trim().matches("[0-9+() .-]{7,20}")) {

            throw new Member2ValidationException(
                    "Enter a valid phone number (7-20 characters)."
            );
        }
    }

    // PASSWORD VALIDATION

    private static void validatePassword(String password)
            throws Member2ValidationException {

        if (password == null || password.length() < 8
                || !password.matches(".*[A-Za-z].*")
                || !password.matches(".*\\d.*")) {

            throw new Member2ValidationException(
                    "Password must be at least 8 characters and contain a letter and a number."
            );
        }
    }

    // PASSWORD HASHING

    private static String hash(String password) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] bytes = md.digest(
                    password.getBytes(StandardCharsets.UTF_8)
            );

            StringBuilder out = new StringBuilder();

            for (byte b : bytes) {
                out.append(String.format("%02x", b));
            }

            return out.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "SHA-256 is unavailable", e
            );
        }
    }
}
