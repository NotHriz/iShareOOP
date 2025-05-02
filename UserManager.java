import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private final List<User> users = new ArrayList<>();

    public boolean registerUser(User newUser) {
        // Check if username already exists
        for (User existingUser : users) {
            if (existingUser.getUsername().equals(newUser.getUsername())) {
                return false;  // Username already taken
            }
        }
        users.add(newUser);
        return true;
    }

    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);  // Return a copy for safety
    }
}