package pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Question;
import model.User;
import services.QuestionService;
import app.Main;


public class QuestionsPage {
    private VBox layout;
    private final Main mainApp;
    private final User currentUser;
    private final QuestionService questionService;
    private final ListView<Question> questionListView;

    public QuestionsPage(Main mainApp, User user, QuestionService questionService) {
        this.mainApp = mainApp;
        this.currentUser = user;
        this.questionService = questionService;
        this.questionListView = new ListView<>();
        buildUI();
        refreshQuestions();
    }

    private void buildUI() {
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

            if (title.isEmpty() || body.isEmpty()) {
                statusLabel.setText("Title and body cannot be empty.");
                return;
            }

            Question newQuestion = new Question(title, body, currentUser.getUsername());
            questionService.addQuestion(newQuestion);

            titleField.clear();
            bodyArea.clear();
            statusLabel.setText("Question posted!");
            refreshQuestions();
        });

        Label questionListLabel = new Label("All Questions:");

        questionListView.setPrefHeight(200);

        layout = new VBox(10,
                titleLabel,
                titleField,
                bodyArea,
                postButton,
                statusLabel,
                new Separator(),
                questionListLabel,
                questionListView
        );
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);
    }

    private void refreshQuestions() {
        questionListView.getItems().setAll(questionService.getAllQuestions());
    }


    public Parent getView() {
        return layout;
    }
}
