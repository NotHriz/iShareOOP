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
    private final ListView<Answer> answerListView;
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

        // Set up answer list view
        answerListView.setPrefHeight(200);
        answerListView.setPrefWidth(400);
        answerListView.setPlaceholder(new Label("No answers yet. Be the first to answer!"));

        // Display each answer nicely
        answerListView.setCellFactory(param -> new ListCell<Answer>() {
            @Override
            protected void updateItem(Answer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("Answer by " + item.getAuthor() + ": " + item.getBody());
                }
            }
        });

        // Triple-click to remove an answer (admin only)
        answerListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 3 && currentUser.isAdmin()) {
                Answer selected = answerListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    answerService.removeAnswerById(selected.getId());
                    refreshAnswers();
                }
            }
        });

        Label titleLabel = new Label("Post an Answer");

        TextArea bodyArea = new TextArea();
        bodyArea.setPromptText("Write your answer here...");
        bodyArea.setPrefRowCount(5);

        Button postButton = new Button("Post Answer");
        Label statusLabel = new Label();

        postButton.setOnAction(e -> {
            String body = bodyArea.getText().trim();
            if (body.isEmpty()) {
                statusLabel.setText("Answer cannot be empty.");
                return;
            }

            Answer answer = new Answer(questionId, body, currentUser.getUsername());
            answerService.addAnswer(answer);
            refreshAnswers();
            bodyArea.clear();
            statusLabel.setText("Answer posted successfully.");
        });

        // Admin button to remove the whole question and all answers
        if (currentUser.isAdmin()) {
            Button removeButton = new Button("Remove Question");
            removeButton.setOnAction(e -> {
                questionService.removeQuestion(questionId);
                answerService.removeAnswersByQuestionId(questionId);
                mainApp.changePage(new QuestionsPage(mainApp, currentUser, questionService, answerService).getView());
            });
            layout.getChildren().add(removeButton);
        }

        layout.getChildren().addAll(titleLabel, bodyArea, postButton, statusLabel, answerListView);
    }

    // Refresh the answer list
    public void refreshAnswers() {
        answerListView.getItems().clear();
        answerListView.getItems().addAll(answerService.getAnswersByQuestionId(questionId));
    }

    public Parent getView() {
        return layout;
    }
}
