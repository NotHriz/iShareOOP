package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class User {
    private String username, password;
    private boolean isAdmin, status;
    private LocalDate createdAt;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    

    // Contructor for new users
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isAdmin = false; // Default to non-admin
        this.status = true;
        this.createdAt = LocalDate.now(); // Set creation date to today
    }
    // COntructor for when loading from file
    public User(String username, String password, boolean isAdmin, boolean status, String createdAtString) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.status = status;
        this.createdAt = LocalDate.parse(createdAtString, formatter); // Set creation date
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
    public boolean getStatus(){
        return status;
    }
    public void setStatus(boolean status){
        this.status = status;
    }


    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public String toFileString() {
    return username + "\n" + password + "\n" + isAdmin + "\n" + status + "\n" + createdAt.format(formatter) + "\n---";
}

    @Override
    public String toString() {
        return username + (isAdmin ? " (Admin)" : "") + " - Created on: " + createdAt.format(formatter);
    }
}

