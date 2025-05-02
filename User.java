import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class User {
    private final String username;  // username is final (immutable) to ensure uniqueness
    private String email;
    private String password;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.password = ""; // or handle as needed
    }
    

    // no setter for username (to prevent modification after creation)
    public String getUsername() {
        return username;
    }

    // other getters and setters...
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);  // Only compares usernames
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
            '}';
    }

    // Method to convert User object to a string for file storage
    public String toFileString() {
        return username + "|" + email + "|" + password;
    }

    // Static method to create a User object from a string read from a file
    public static User fromFileString(String line) {
        String[] parts = line.split("\\|", 3);
        if (parts.length != 3) return null;
        return new User(parts[0], parts[1], parts[2]);
    }

    // Method to save the User object to a file
    public void saveToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(this.toFileString());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving user to file: " + e.getMessage());
        }
    }
}
