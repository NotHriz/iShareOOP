import java.util.*;

public class QuestionTest {
    private static Scanner scanner = new Scanner(System.in);
    private static QuestionManager questionManager = new QuestionManager();

    public static void main(String[] args) {
        // Load data from file
        questionManager.loadUsersFromFile();
        questionManager.loadFromFile();

        // Menu-driven loop for user interaction
        while (true) {
            System.out.println("\n=== Question Manager ===");
            System.out.println("1. View Questions");
            System.out.println("2. Add Question");
            System.out.println("3. Edit Question");
            System.out.println("4. Delete Question");
            System.out.println("5. Exit");
            System.out.print("Please choose an option (1-5): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character after the number

            switch (choice) {
                case 1:
                    viewQuestions();
                    break;
                case 2:
                    addQuestion();
                    break;
                case 3:
                    editQuestion();
                    break;
                case 4:
                    deleteQuestion();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    questionManager.saveToFile();  // Save any changes before exiting
                    return;
                default:
                    System.out.println("Invalid choice! Please choose a valid option (1-5).");
            }
        }
    }

    // View all questions
    private static void viewQuestions() {
        System.out.println("\n--- All Questions ---");
        List<Question> questions = questionManager.getAllQuestions();
        if (questions == null || questions.isEmpty()) {
            System.out.println("No questions available.");
            return;
        }
        if (questions.isEmpty()) {
            System.out.println("No questions available.");
        } else {
            for (Question question : questions) {
                System.out.println(question);
            }
        }
    }

    // Add a new question
    private static void addQuestion() {
        System.out.print("Enter the question text: ");
        String text = scanner.nextLine();

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        // Find the user object based on the username
        User author = null;
        for (User user : questionManager.getUsers()) {
            if (user.getUsername().equals(username)) {
                author = user;
                break;
            }
        }

        if (author == null) {
            System.out.println("User not found. Please enter a valid username.");
            return;
        }

        Question newQuestion = new Question(questionManager.getNextId(), text, new Date(), author);
        questionManager.addQuestion(newQuestion);
        System.out.println("Question added successfully.");
    }

    // Edit an existing question
    private static void editQuestion() {
        System.out.print("Enter the index of the question you want to edit: ");
        int index = scanner.nextInt();
        scanner.nextLine();  // Consume the newline character

        System.out.print("Enter the new text for the question: ");
        String newText = scanner.nextLine();

        questionManager.editQuestion(index - 1, newText);  // Adjust for 0-based index
    }

    // Delete a question
    private static void deleteQuestion() {
        System.out.print("Enter the index of the question you want to delete: ");
        int index = scanner.nextInt();
        scanner.nextLine();  // Consume the newline character

        questionManager.deleteQuestion(index - 1);  // Adjust for 0-based index
        System.out.println("Question deleted successfully.");
    }
}
