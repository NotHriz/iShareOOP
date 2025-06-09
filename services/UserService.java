package services;

import model.User;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class UserService {
    private final static List<User> users = new ArrayList<>();
    private final String FILE_NAME = "databases/users.txt";

    public void addUser(String username, String password) {
        User newUser = new User(username, password);
        users.add(newUser);
        saveUsers();  // Save after adding a new user
    }

    // Save the user to the file
    public void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (User user : users) {
                writer.write(user.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    // Load users from the file
    public void loadUsers() {
        users.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String username;
            while ((username = reader.readLine()) != null) {
                String password = reader.readLine();
                String isAdminStr = reader.readLine();
                String statusStr = reader.readLine();
                String createdAt = reader.readLine();
                String separator = reader.readLine();

                // Check if any of the lines are null (incomplete user block)
                if (password == null || isAdminStr == null || statusStr == null|| createdAt == null || separator == null) {
                    System.out.println("Incomplete user block. Skipping.");
                    break;
                }

                if (!separator.trim().equals("---")) {
                    System.out.println("Invalid user data format in file.");
                    continue;
                }

                boolean isAdmin = Boolean.parseBoolean(isAdminStr.trim());
                boolean status = Boolean.parseBoolean(statusStr.trim());
                users.add(new User(username.trim(), password.trim(), isAdmin, status, createdAt.trim()));
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Authenticate a user
    public User authenticate(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user; // Return the authenticated user
            }
        }
        return null; // Authentication failed
    }

    // Check if a username already exists
    public boolean userExists(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    // Check if a user is an admin
    public boolean isAdmin(User user) {
        return user != null && user.isAdmin();
    }

    // Check if a user is banned
    public boolean isBanned(User user) {
        return user != null && !user.getStatus();
    }

    public void banUser(String username) {
        loadUsers(); // Ensure latest data is loaded
        boolean found = false;
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                user.setStatus(false);
                found = true;
                break;
            }
        }
        if (found) {
            saveUsers(); // Save changes only if user was found and updated
            System.out.println("User '" + username + "' has been banned.");
        } else {
            System.out.println("User '" + username + "' not found.");
        }
    }

}
