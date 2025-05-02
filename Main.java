import java.util.Date;
import java.util.Scanner;

public class Main {
    private static final String USERS_FILE = "users.txt";
    private static final String ADMINS_FILE = "admins.txt";  // list of admin usernames

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        QuestionManager questionManager = new QuestionManager();
        UserManager userManager = new UserManager();

        // Load users and questions
        questionManager.loadUsersFromFile();
        questionManager.loadFromFile();

        System.out.println("Welcome to the Q&A System!");
        System.out.print("Enter your username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter your email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine().trim();

        // Load list of admin usernames
        boolean isAdmin = false;
        try (Scanner fileScanner = new Scanner(new java.io.File(ADMINS_FILE))) {
            while (fileScanner.hasNextLine()) {
                String adminName = fileScanner.nextLine().trim();
                if (adminName.equals(username)) {
                    isAdmin = true;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Admin list not found. Continuing as user.");
        }

        User currentUser;
        if (isAdmin) {
            currentUser = new Admin(username, email, password);
            System.out.println("Logged in as Admin.");
            adminMenu((Admin) currentUser, questionManager, userManager, scanner);
        } else {
            currentUser = new User(username, email, password);
            System.out.println("Logged in as User.");
            userMenu(currentUser, questionManager, scanner);
        }

        scanner.close();
    }

    private static void adminMenu(Admin admin, QuestionManager qm, UserManager um, Scanner sc) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. View All Questions");
            System.out.println("2. Delete a Question by ID");
            System.out.println("3. Delete a User by Username");
            System.out.println("4. Exit");

            System.out.print("Choose an option: ");
            int option = Integer.parseInt(sc.nextLine());

            switch (option) {
                case 1:
                    for (Question q : qm.getQuestions()) {
                        System.out.println(q);
                    }
                    break;
                case 2:
                    System.out.print("Enter question ID to delete: ");
                    int id = Integer.parseInt(sc.nextLine());
                    if (admin.deleteQuestion(id, qm)) {
                        System.out.println("Question deleted.");
                    } else {
                        System.out.println("Question not found.");
                    }
                    break;
                case 3:
                    System.out.print("Enter username to delete: ");
                    String uname = sc.nextLine();
                    if (admin.deleteUser(uname, um)) {
                        System.out.println("User deleted.");
                    } else {
                        System.out.println("User not found or is an admin.");
                    }
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void userMenu(User user, QuestionManager qm, Scanner sc) {
        while (true) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. View Questions");
            System.out.println("2. Ask a Question");
            System.out.println("3. Exit");

            System.out.print("Choose an option: ");
            int option = Integer.parseInt(sc.nextLine());

            switch (option) {
                case 1:
                    for (Question q : qm.getQuestions()) {
                        System.out.println(q);
                    }
                    break;
                case 2:
                    System.out.print("Enter your question: ");
                    String text = sc.nextLine();
                    int id = qm.getNextId();
                    Question q = new Question(id, text, new Date(), user);
                    qm.addQuestion(q);
                    System.out.println("Question added.");
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
