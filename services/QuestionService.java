package services;

import model.Question;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionService {
    private final List<Question> questions = new ArrayList<>();
    private final String FILE_NAME = "questions.txt";

    public void addQuestion(Question q) {
        questions.add(q);
        saveQuestions();  // Save after adding a new question
    }

    // Method to get all questions
    public List<Question> getAllQuestions() {
        return questions;
    }

    public void saveQuestions() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Question q : questions) {
                writer.write(q.getTitle());
                writer.newLine();
                writer.write(q.getBody());
                writer.newLine();
                writer.write(q.getAuthor());
                writer.newLine();
                writer.write("---");
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving questions: " + e.getMessage());
        }
    }

    public void loadQuestions() {
        questions.clear(); // Clear any existing data
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String title, body, author, separator;
            while ((title = reader.readLine()) != null) {
                body = reader.readLine();
                author = reader.readLine();
                separator = reader.readLine(); // should be "---"
                if (title != null && body != null && author != null && separator.equals("---")) {
                    questions.add(new Question(title, body, author));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No saved file found.");
        } catch (IOException e) {
            System.out.println("Error loading questions: " + e.getMessage());
        }
    }
}
