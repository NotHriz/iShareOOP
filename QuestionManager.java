import java.io.*;
import java.util.*;

public class QuestionManager {
    private static final String FILE_NAME = "questions.txt";
    private static final String USERS_FILE = "users.txt";
    private List<Question> questions = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private int nextId = 1;

    // Load users from file
    public void loadUsersFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    users.add(new User(parts[0], parts[1]));  // Username, Email
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    // Load questions from file
    public void loadFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                Question question = Question.fromFileString(line);
                if (question != null) {
                    questions.add(question);
                    nextId = Math.max(nextId, question.getId() + 1); // Update nextId based on loaded questions
                }

                // Load users from questions file (if needed)
                String[] parts = line.split("\\|", 4);
                if (parts.length == 4) {
                    String[] authorData = parts[3].split(",");
                    if (authorData.length == 2) {
                        users.add(new User(authorData[0], authorData[1]));  // Username, Email
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading questions: " + e.getMessage());
        }
    }

    // Save questions to file
    public void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Question question : questions) {
                bw.write(question.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving questions: " + e.getMessage());
        }
    }

    // Add a new question
    public void addQuestion(Question question) {
        questions.add(question);
        nextId++;
        appendToFile(question);
    }

    // Append a new question to the file
    public void appendToFile(Question question) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(question.toFileString());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error appending question: " + e.getMessage());
        }
    }

    // Edit a question
    public void editQuestion(int index, String newText) {
        if (index < 0 || index >= questions.size()) {
            System.out.println("Invalid question number.");
            return;
        }

        // Update in memory
        Question question = questions.get(index);
        question.setText(newText);

        // Save to file
        saveToFile();

        System.out.println("Question updated successfully.");
    }

    // Delete a question
    public void deleteQuestion(int index) {
        if (index < 0 || index >= questions.size()) {
            System.out.println("Invalid question number.");
            return;
        }
        questions.remove(index);
        saveToFile();
    }

    // Get a list of all questions (texts only)
    public List<Question> getQuestions() {
        return questions;
    }

    // Get a list of all users
    public List<User> getUsers() {
        return users;
    }

    // Get the next available ID for a new question
    public int getNextId() {
        return nextId;
    }

    public List<Question> getAllQuestions() {
        return questions;
    }
    
}
