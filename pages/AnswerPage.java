package pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.*;
import services.*;
import app.Main;

public class AnswerPage {
    private VBox layout;
    private final Main mainApp;
    private final User currentUser;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final ListView<String> answerListView;
    private String questionId;

    // Constructor to initialize the AnswerPage
    public AnswerPage(Main mainApp, User user, QuestionService questionService, AnswerService answerService, String questionId) {
        this.mainApp = mainApp;
        this.currentUser = user;
        this.questionService = questionService;
        this.answerService = answerService;
        this.questionId = questionId;
        this.answerListView = new ListView<>();
        
        buildUI();
        refreshAnswers();
    }

    // Build the UI for the answer page
    private void buildUI() {
        layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        
        // Show the question title and body at the top
        Question currentQuestion = questionService.getCurrentQuestion(questionId);
        if (currentQuestion != null) {
            Label questionTitle = new Label("Question: " + currentQuestion.getTitle());
            Label questionBody = new Label("Body: " + currentQuestion.getBody());
            layout.getChildren().addAll(questionTitle, questionBody);
        } else {
            Label noQuestionLabel = new Label("No question selected.");
            layout.getChildren().add(noQuestionLabel);
            return; // Exit if no question is available
        }

        // Create a ListView to display other answers
        answerListView.setPrefHeight(200);
        answerListView.setPrefWidth(400);
        refreshAnswers();
        answerListView.setPlaceholder(new Label("No answers yet. Be the first to answer!"));
        answerListView.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                }
            }
        });

        Label titleLabel = new Label("Post an Answer");

        // Create a TextArea for the answer body
        TextArea bodyArea = new TextArea();
        bodyArea.setPromptText("Write your answer here...");
        bodyArea.setPrefRowCount(5);

        // Create a Button to post the answer
        Button postButton = new Button("Post Answer");
        Label statusLabel = new Label();

        // Create Button action to post the answer
        postButton.setOnAction(e -> {
            String body = bodyArea.getText().trim();

            // Validate input
            if (body.isEmpty()) {
                statusLabel.setText("Answer cannot be empty.");
                return;
            }

            // Create and add the answer
            Answer answer = new Answer(questionId, body, currentUser.getUsername());
            answerService.addAnswer(answer);
            refreshAnswers();
            bodyArea.clear();
            statusLabel.setText("Answer posted successfully.");
        });

        // Add components to the layout
        layout.getChildren().addAll(titleLabel, bodyArea, postButton, statusLabel, answerListView);
    }

    public void refreshAnswers() {
        answerListView.getItems().clear();
        for (Answer answer : answerService.getAnswersByQuestionId(questionId)) {
            String answerText = "Answer by " + answer.getAuthor() + ": " + answer.getBody();
            answerListView.getItems().add(answerText);
        }
    }

    public Parent getView() {
        return layout;
    }

    
}
