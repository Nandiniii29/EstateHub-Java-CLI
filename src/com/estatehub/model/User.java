package com.estatehub.model;

/** Represents a registered EstateHub customer or administrator. */
public class User {
    public enum Role { CUSTOMER, ADMIN }

    private final String userId;
    private String name;
    private final String email;
    private String phone;
    private String passwordHash;
    private final Role role;

    public User(String userId, String name, String email, String phone, String passwordHash, Role role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.role = role;
    }
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public Role getRole() { return role; }
    public void updateProfile(String name, String phone) { this.name = name; this.phone = phone; }
    public boolean passwordMatches(String hash) { return passwordHash.equals(hash); }
    public void changePasswordHash(String hash) { this.passwordHash = hash; }
    @Override public String toString() {
        return "User{id='" + userId + "', name='" + name + "', email='" + email + "', phone='" + phone + "', role=" + role + "}";
    }
}
