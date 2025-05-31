package pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Question;
import model.User;
import services.AnswerService;
import services.QuestionService;
import app.Main;

public class QuestionsPage {
    private VBox layout;
    private final Main mainApp;
    private final User currentUser;
    private final QuestionService questionService;
    private final ListView<Question> questionListView;
    private final AnswerService answerService;

    public QuestionsPage(Main mainApp, User user, QuestionService questionService, AnswerService answerService) {
        this.mainApp = mainApp;
        this.currentUser = user;
        this.questionService = questionService;
        this.answerService = answerService;
        this.questionListView = new ListView<>();
        buildUI();
        refreshQuestions();
    }

    private void buildUI() {
        layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);

        // Top bar: Welcome + Logout
        Label welcomeLabel = new Label("Welcome, " + currentUser.getUsername() + "!");
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            mainApp.changePage(new LoginPage(mainApp, mainApp.getUserService(), questionService, answerService).getView());
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox topBar = new HBox(10, welcomeLabel, spacer, logoutButton);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 10, 0));

        // Post question form
        TitledPane postSection = new TitledPane();
        postSection.setText("Post a New Question");
        postSection.setCollapsible(false);

        VBox formBox = new VBox(10);
        formBox.setAlignment(Pos.CENTER_LEFT);
        formBox.setPadding(new Insets(10));

        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        titleField.setMaxWidth(400);

        TextArea bodyArea = new TextArea();
        bodyArea.setPromptText("Write your question here...");
        bodyArea.setPrefRowCount(4);
        bodyArea.setMaxWidth(400);

        Button postButton = new Button("Post Question");
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: green;");

        postButton.setOnAction(e -> {
            String title = titleField.getText().trim();
            String body = bodyArea.getText().trim();

            if (title.length() > 100) {
                statusLabel.setText("Title cannot exceed 100 characters.");
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            if (title.isEmpty() || body.isEmpty()) {
                statusLabel.setText("Title and body cannot be empty.");
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            Question newQuestion = new Question(title, body, currentUser.getUsername());
            questionService.addQuestion(newQuestion);
            refreshQuestions();

            titleField.clear();
            bodyArea.clear();
            statusLabel.setText("Question posted!");
            statusLabel.setStyle("-fx-text-fill: green;");
        });

        formBox.getChildren().addAll(titleField, bodyArea, postButton, statusLabel);
        postSection.setContent(formBox);

        // Question list
        Label questionListLabel = new Label("All Questions:");
        questionListLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        questionListView.setPrefHeight(200);
        questionListView.setMaxWidth(600);
        questionListView.setPlaceholder(new Label("No questions posted yet."));
        questionListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Question selected = questionListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    AnswerPage answerPage = new AnswerPage(mainApp, currentUser, questionService, answerService, selected.getId());
                    mainApp.popUpNewWindow(answerPage.getView());
                }
            }
        });

        layout.getChildren().addAll(
                topBar,
                postSection,
                new Separator(),
                questionListLabel,
                questionListView
        );
    }

    private void refreshQuestions() {
        questionListView.getItems().setAll(questionService.getAllQuestions());
    }

    public Parent getView() {
        return layout;
    }
}
