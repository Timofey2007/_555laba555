package org.example._555laba555.domain;

import java.time.Instant;
import java.util.Objects;

public class User {
    private long id;
    private String login;
    private String passwordHash;
    private String role;
    private Instant createdAt;
    private Instant lastLogin;

    public User() {
        this.role = "USER";
        this.createdAt = Instant.now();
    }

    public User(long id, String login, String passwordHash) {
        this.id = id;
        this.login = login;
        this.passwordHash = passwordHash;
        this.role = "USER";
        this.createdAt = Instant.now();
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getLastLogin() { return lastLogin; }
    public void setLastLogin(Instant lastLogin) { this.lastLogin = lastLogin; }

    public boolean isAdmin() { return "ADMIN".equals(role); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}