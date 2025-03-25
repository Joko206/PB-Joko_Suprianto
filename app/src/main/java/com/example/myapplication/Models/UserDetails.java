package com.example.myapplication.Models;

public class UserDetails {
    private String userId;
    private String username;
    private String email;
    private String password;
    private String nim;

    // No-argument constructor (required for Firebase)
    public UserDetails() {
        // This constructor is necessary for Firebase to deserialize the object
    }

    // Constructor with arguments to initialize fields
    public UserDetails(String userId, String username, String email, String password, String nim) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.nim = nim;
    }

    // Getter and Setter methods
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", nim='" + nim + '\'' +
                '}';
    }
}
