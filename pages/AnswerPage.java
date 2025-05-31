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
    private final String questionId;
    private ScrollPane scrollPane;

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

    private void buildUI() {
        layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPrefWidth(600);

        Question currentQuestion = questionService.getCurrentQuestion(questionId);

        if (currentQuestion == null) {
            Label noQuestionLabel = new Label("No question selected.");
            layout.getChildren().add(noQuestionLabel);
            scrollPane = new ScrollPane(layout);
            scrollPane.setFitToWidth(true);
            return;
        }

        Label titleLabel = new Label("Question:");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Label questionTitle = new Label(currentQuestion.getTitle());
        questionTitle.setWrapText(true);
        questionTitle.setStyle("-fx-font-size: 14px;");

        Label bodyLabel = new Label("Details:");
        bodyLabel.setStyle("-fx-font-weight: bold;");
        Label questionBody = new Label(currentQuestion.getBody());
        questionBody.setWrapText(true);

        VBox questionBox = new VBox(5, titleLabel, questionTitle, bodyLabel, questionBody);
        questionBox.setPadding(new Insets(10));
        questionBox.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-radius: 5;");
        questionBox.setMaxWidth(500);

        answerListView.setPrefHeight(200);
        answerListView.setPrefWidth(500);
        answerListView.setPlaceholder(new Label("No answers yet. Be the first to answer!"));

        answerListView.setCellFactory(param -> new ListCell<>() {
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

        answerListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 3 && currentUser.isAdmin()) {
                Answer selected = answerListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    answerService.removeAnswerById(selected.getId());
                    refreshAnswers();
                }
            }
        });

        Label postAnswerLabel = new Label("Post an Answer");
        postAnswerLabel.setStyle("-fx-font-weight: bold;");

        TextArea answerArea = new TextArea();
        answerArea.setPromptText("Write your answer here...");
        answerArea.setPrefRowCount(5);
        answerArea.setWrapText(true);

        Button postButton = new Button("Post Answer");
        Label statusLabel = new Label();

        postButton.setOnAction(e -> {
            String body = answerArea.getText().trim();
            if (body.isEmpty()) {
                statusLabel.setText("Answer cannot be empty.");
                return;
            }

            Answer answer = new Answer(questionId, body, currentUser.getUsername());
            answerService.addAnswer(answer);
            refreshAnswers();
            answerArea.clear();
            statusLabel.setText("Answer posted successfully.");
        });

        VBox postAnswerBox = new VBox(10, postAnswerLabel, answerArea, postButton, statusLabel);
        postAnswerBox.setPadding(new Insets(10));
        postAnswerBox.setStyle("-fx-background-color: #f0f4ff; -fx-border-color: #ccddee; -fx-border-radius: 5; -fx-background-radius: 5;");
        postAnswerBox.setMaxWidth(500);

        if (currentUser.isAdmin()) {
            Button removeQuestionButton = new Button("Remove Question");
            removeQuestionButton.setStyle("-fx-background-color: #ffdddd;");
            removeQuestionButton.setOnAction(e -> {
                questionService.removeQuestion(questionId);
                answerService.removeAnswersByQuestionId(questionId);
                mainApp.changePage(new QuestionsPage(mainApp, currentUser, questionService, answerService).getView());
            });
            layout.getChildren().add(removeQuestionButton);
        }

        layout.getChildren().addAll(questionBox, new Label("Answers:"), answerListView, postAnswerBox);

        scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }

    public void refreshAnswers() {
        answerListView.getItems().setAll(answerService.getAnswersByQuestionId(questionId));
    }

    public Parent getView() {
        return scrollPane;
    }
}
