package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Question {
    private String title, body, author;
    private LocalDateTime createdAt;

    public Question(String title, String body, String author) {
        this.title = title;
        this.body = body;
        this.author = author;
        this.createdAt = LocalDateTime.now();
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

    @Override
    public String toString() {
        // format the date to a readable format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String createdAt = " (" + this.createdAt.format(formatter) + ")";
        
        return title + " " +"(by " + author + ")" + createdAt;
    }
}