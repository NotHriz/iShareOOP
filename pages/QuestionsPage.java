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
        layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);

        // 🔹 Welcome message and logout button
        Label welcomeLabel = new Label("Welcome, " + currentUser.getUsername() + "!");
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            mainApp.changePage(new LoginPage(mainApp, mainApp.getUserService(), questionService, answerService).getView());
        });

        HBox topBar = new HBox(10, welcomeLabel, logoutButton);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(0, 0, 10, 0));

        // 🔹 Form to post a question
        Label titleLabel = new Label("Post a new Question");

        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextArea bodyArea = new TextArea();
        bodyArea.setPromptText("Write your question here...");
        bodyArea.setPrefRowCount(5);

        Button postButton = new Button("Post Question");
        Label statusLabel = new Label();

        postButton.setOnAction(e -> {
            String title = titleField.getText().trim();
            String body = bodyArea.getText().trim();

            if (title.length() > 100) {
                statusLabel.setText("Title cannot exceed 100 characters.");
                return;
            }
            if (title.isEmpty() || body.isEmpty()) {
                statusLabel.setText("Title and body cannot be empty.");
                return;
            }

            Question newQuestion = new Question(title, body, currentUser.getUsername());
            questionService.addQuestion(newQuestion);
            refreshQuestions();

            titleField.clear();
            bodyArea.clear();
            statusLabel.setText("Question posted!");
        });

        Label questionListLabel = new Label("All Questions:");

        questionListView.setPrefHeight(200);
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
                titleLabel,
                titleField,
                bodyArea,
                postButton,
                statusLabel,
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
