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
        this.answerService = answerService;
        this.mainApp = mainApp;
        this.currentUser = user;
        this.questionService = questionService;
        this.questionListView = new ListView<>();
        buildUI();
        refreshQuestions();
    }

    private void buildUI() {
        Label titleLabel = new Label("Post a new Question");

        // Create a TextField for the question title
        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        // Create a TextArea for the question body
        TextArea bodyArea = new TextArea();
        bodyArea.setPromptText("Write your question here...");
        bodyArea.setPrefRowCount(5);

        // Create a Button to post the question
        Button postButton = new Button("Post Question");
        Label statusLabel = new Label();

        // Create Button action to post the question
        postButton.setOnAction(e -> {
            String title = titleField.getText().trim();
            String body = bodyArea.getText().trim();

            // Validate input
            if (title.length() > 100) {
                statusLabel.setText("Title cannot exceed 100 characters.");
                return;
            }
            if (title.isEmpty() || body.isEmpty()) {
                statusLabel.setText("Title and body cannot be empty.");
                return;
            }

            // Create a new Question object and add it to the service
            Question newQuestion = new Question(title, body, currentUser.getUsername());
            questionService.addQuestion(newQuestion);
            refreshQuestions();

            titleField.clear();
            bodyArea.clear();
            statusLabel.setText("Question posted!");
            refreshQuestions();
        });

        Label questionListLabel = new Label("All Questions:");

        questionListView.setPrefHeight(200);
        // Add clickable functionality to the question list
        questionListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {  // double-click
                Question selected = questionListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                // Pop up a new window to show question and answers
                AnswerPage answerPage = new AnswerPage(mainApp, currentUser, questionService, answerService,selected.getId());
                mainApp.popUpNewWindow(answerPage.getView());
            }
        }
    });

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
        System.out.println("Refreshing questions. Total: " + questionService.getAllQuestions().size());
        for (Question q : questionService.getAllQuestions()) {
            System.out.println(q);
        }
        questionListView.getItems().setAll(questionService.getAllQuestions());
    }



    public Parent getView() {
        return layout;
    }
}
