package services;

import model.Question;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionService {
    private final List<Question> questions = new ArrayList<>();
    private final String FILE_NAME = "databases/questions.txt";

    public void addQuestion(Question q) {
        questions.add(q);
        saveQuestions();  // Save after adding a new question
    }

    // Method to get all questions
    public List<Question> getAllQuestions() {
        return questions;
    }

    public void saveQuestions() {
        try {
            File file = new File(FILE_NAME);
            File parent = file.getParentFile();

            if (parent != null && !parent.exists()) {
                boolean dirsCreated = parent.mkdirs();  // Create directories if they don't exist
                if (!dirsCreated) {
                    System.out.println("Warning: Failed to create directories for " + parent.getAbsolutePath());
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Question q : questions) {
                    writer.write(q.getTitle());
                    writer.newLine();
                    writer.write(q.getBody());
                    writer.newLine();
                    writer.write(q.getAuthor());
                    writer.newLine();
                    writer.write(q.getId());
                    writer.newLine();
                    writer.write(q.getFormattedTimestamp());
                    writer.newLine();
                    writer.write("---");
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving questions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadQuestions() {
        questions.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String title, body, author, id, createdAt;

            while ((title = reader.readLine()) != null) {
                body = reader.readLine();
                author = reader.readLine();
                id = reader.readLine();
                createdAt = reader.readLine();
                reader.readLine(); // Skip the '---' separator

                if (title != null && body != null && author != null && id != null && createdAt != null) {
                    Question q = new Question(title, body, author, id, createdAt);
                    questions.add(q);
                } else {
                    System.out.println("Skipping an incomplete question block.");
                }
            }

            System.out.println("Questions loaded: " + questions.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }   
}
