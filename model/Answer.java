package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Answer {
    private String questionId, body, author, id;
    private LocalDateTime createdAt;

    // Formatter for saving/loading
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Constructor for new answers
    public Answer(String questionId, String body, String author) {
        this.questionId = questionId;
        this.body = body;
        this.author = author;
        this.createdAt = LocalDateTime.now(); // Set creation date to today
        this.id = java.util.UUID.randomUUID().toString(); // Generate unique ID
    }
    // Constructor for loading from file
    public Answer(String questionId, String body, String author, String id, String createdAtString) {
        this.questionId = questionId;
        this.body = body;
        this.author = author;
        this.createdAt = LocalDateTime.parse(createdAtString, formatter);
        this.id = id;
    }
    public String getQuestionId() {
        return questionId;
    }
    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public static DateTimeFormatter getFormatter() {
        return formatter;
    }

    public String getFormattedTimeStamp() {
        return createdAt.format(formatter);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "questionId='" + questionId + '\'' +
                ", created at='" + createdAt + '\'' +
                ", author='" + author + '\'' +
                ", id='" + id + '\'' +
                ", body=" + body +
                '}';
    }

    public String toFileString() {
        return questionId + "\n" + body + "\n" + author + "\n" + id + "\n" + createdAt.format(formatter) + "\n---";
    }
    
}
