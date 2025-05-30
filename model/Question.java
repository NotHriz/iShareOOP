package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Question {
    private final String id;
    private String title, body, author;
    private LocalDateTime createdAt;

    // Formatter for saving/loading
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Constructor for new questions
    public Question(String title, String body, String author) {
        this.title = title;
        this.body = body;
        this.author = author;
        this.createdAt = LocalDateTime.now(); // Use LocalDateTime for hours + minutes
        this.id = UUID.randomUUID().toString(); // Generate unique ID
    }

    // Constructor for loading from file
    public Question(String title, String body, String author, String id, String createdAtString) {
        this.title = title;
        this.body = body;
        this.author = author;
        this.createdAt = LocalDateTime.parse(createdAtString, formatter);
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getFormattedTimestamp() {
        return createdAt.format(formatter);
    }

    @Override
    public String toString() {
        return title + " by " + author + " on " + getFormattedTimestamp();
    }
}
