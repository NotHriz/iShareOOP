import java.text.SimpleDateFormat;
import java.util.Date;

public class Question {
    private int id;
    private String text;
    private Date dateCreated;
    private User author;

    // Constructor to initialize Question with ID, text, date, and author
    public Question(int id, String text, Date dateCreated, User author) {
        this.id = id;
        this.text = text;
        this.dateCreated = dateCreated;
        this.author = author;
    }

    // Constructor for creating Question without a timestamp (current time will be set)
    public Question(int id, String text, User author) {
        this(id, text, new Date(), author);
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public User getAuthor() {
        return author;
    }

    // Convert Question object to a string format for file storage: id|text|timestamp|username,email
    public String toFileString() {
        return id + "|" + text + "|" + dateCreated.getTime() + "|" + author.getUsername() + "," + author.getEmail();
    }

    // Static method to create Question from a line in the file
    public static Question fromFileString(String line) {
        String[] parts = line.split("\\|", 4);
        if (parts.length != 4) return null;

        int id = Integer.parseInt(parts[0]);
        String text = parts[1];
        long timestamp = Long.parseLong(parts[2]);
        String[] authorData = parts[3].split(",");
        User author = new User(authorData[0], authorData[1]);  // Assuming email is in the second part

        return new Question(id, text, new Date(timestamp), author);
    }

    // Override toString to display question nicely
    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return id + ". " + text + " (Created: " + format.format(dateCreated) + ") by " + author.getUsername();
    }
}
