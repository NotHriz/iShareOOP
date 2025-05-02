public class User {
    private String username;
    private String email;
    private String password; // Store password, but not in questions.txt

    // Constructor
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Constructor without password (for use when reading from question file)
    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.password = "";  // Set to empty, password is not used in questions file
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Optionally, set the password if needed (this is a setter)
    public void setPassword(String password) {
        this.password = password;
    }

    // Override toString for easy printing
    @Override
    public String toString() {
        return username + " (" + email + ")";
    }
}
