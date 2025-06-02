package services;

import model.Answer;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AnswerService {
    public final List<Answer> answers = new ArrayList<>();
    private final String FILE_NAME = "databases/answers.txt";

    // Method to add a new answer and save it to the file
    public void addAnswer(Answer a) {
        answers.add(a);
        saveAnswers();  // Save after adding a new answer
    }

    // Method to save all answers to a file
    public void saveAnswers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Answer a : answers) {
                writer.write(a.getQuestionId());
                writer.newLine();
                writer.write(a.getBody());
                writer.newLine();
                writer.write(a.getAuthor());
                writer.newLine();
                writer.write(a.getId());
                writer.newLine();
                writer.write(a.getFormattedTimeStamp());
                writer.newLine();
                writer.write("---");
                writer.newLine();  // Separator for each answer
            }
        } catch (IOException e) {
            System.out.println("Error saving answers: " + e.getMessage());
        }
    }

    // Method to load answers from a file into memory
    public void loadAnswers() {
        answers.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String questionId;
            while ((questionId = reader.readLine()) != null) {
                String body = reader.readLine();
                String author = reader.readLine();
                String id = reader.readLine();
                String createdAtString = reader.readLine();
                String separator = reader.readLine();

                // Check if any of the lines are null (incomplete answer block)
                if (body == null || author == null || id == null || createdAtString == null || separator == null) {
                    System.out.println("Incomplete answer block. Skipping.");
                    break;
                }

                // Validate separator line
                if (!separator.trim().equals("---")) {
                    System.out.println("Invalid answer data format in file.");
                    continue;
                }

                // Add the answer to the list
                answers.add(new Answer(questionId.trim(), body.trim(), author.trim(), id.trim(), createdAtString.trim()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to get all answers
    public List<Answer> getAllAnswers() {
        return answers;
    }

    // Method to get answers for a specific question
    public List<Answer> getAnswersByQuestionId(String questionId) {
        List<Answer> questionAnswers = new ArrayList<>();
        for (Answer a : answers) {
            if (a.getQuestionId().equals(questionId)) {
                questionAnswers.add(a);
            }
        }
        return questionAnswers;
    }

    // Method to remove all answers associated with a specific question
    public void removeAnswersByQuestionId(String questionId) {
        answers.removeIf(a -> a.getQuestionId().equals(questionId));
        saveAnswers();  // Save after removing answers
    }

    // Method to remove a specific answer by its unique ID
    public void removeAnswerById(String id) {
        answers.removeIf(a -> a.getId().equals(id));
        saveAnswers();  // Save after removing an answer
    }
}
