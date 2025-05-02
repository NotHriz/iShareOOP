public class UserTest {
    public static void main(String[] args) {
        UserManager userManager = new UserManager();

        // Try to register users
        boolean success1 = userManager.registerUser(new User("ayish", "ayish05@gmail.com", "pass123"));
        boolean success2 = userManager.registerUser(new User("bob", "bob@gmail.com", "pass456"));
        boolean success3 = userManager.registerUser(new User("ayish", "ayish21@gmail.com", "pass789"));

        System.out.println("ayish  registered: " + success1);  // true
        System.out.println("Bob registered: " + success2);    // true
        System.out.println("Duplicate ayish: " + success3);   // false

        // Find a user
        User found = userManager.findUserByUsername("adil");
        if (found != null) {
            System.out.println("Found user: " + found.getEmail());
        }
    }
}