import java.text.SimpleDateFormat;
import java.util.Date;

public class Answer {
    private int answerId;
    private String text;
    private Date dateCreated;
    private User author;
    private Question question;

    public Answer(int answerId, String text, User author, Question question) {
        this.answerId = answerId;
        this.text = text;
        this.dateCreated = new Date();
        this.author = author;
        this.question = question;
    }

    public int getAnswerId() {
        return answerId;
    }

    public String getText() {
        return text;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public User getAuthor() {
        return author;
    }

    public Question getQuestion() {
        return question;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void editAnswer(String newText) {
        this.text = newText;
    }

    public void shareAnswer() {
        System.out.println("Answer shared: " + text);
    }

    public void reactAnswer(String reaction) {
        System.out.println("Reaction '" + reaction + "' added to the answer.");
    }

    public void replyAnswer(String replyText) {
        System.out.println("Reply to answer: " + replyText);
    }

    public String toFileString() {
        return answerId + "|" + text + "|" + dateCreated.getTime() + "|" +
               author.getUsername() + "," + author.getEmail() + "|" +
               question.getId();
    }

    public static Answer fromFileString(String line, Question[] questions) {
        String[] parts = line.split("\\|", 5);
        if (parts.length != 5) return null;

        int id = Integer.parseInt(parts[0]);
        String text = parts[1];
        long timestamp = Long.parseLong(parts[2]);
        String[] authorData = parts[3].split(",");
        int questionId = Integer.parseInt(parts[4]);

        User author = new User(authorData[0], authorData[1]);
        Question linkedQuestion = null;

        for (Question q : questions) {
            if (q.getId() == questionId) {
                linkedQuestion = q;
                break;
            }
        }

        if (linkedQuestion == null) return null;

        Answer answer = new Answer(id, text, author, linkedQuestion);
        answer.dateCreated = new Date(timestamp); // set from file
        return answer;
    }

    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return "Answer ID: " + answerId + " | " + text + " (by " + author.getUsername() +
               ", on " + format.format(dateCreated) + ")";
    }
}