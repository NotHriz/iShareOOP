package pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
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
        questionBox.setStyle("-fx-background-color: rgb(175, 214, 253); -fx-border-color: #ddd; -fx-border-radius: 15; -fx-background-radius: 15;");
        questionBox.setMaxWidth(500);
        answerListView.setPrefHeight(200);
        answerListView.setPrefWidth(500);
        answerListView.setPlaceholder(new Label("No answers yet. Be the first to answer!"));
        answerListView.setStyle("-fx-background-color:rgb(191, 121, 252); -fx-border-radius: 15; -fx-background-radius: 15;");

        answerListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Answer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("Answer by @" + item.getAuthor() + ": " + item.getBody());
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

        Label postAnswerLabel = new Label("Post Answers");
        postAnswerLabel.setStyle("-fx-font-weight: bold;");

        TextArea answerArea = new TextArea();
        answerArea.setPromptText("Write your answer here...");
        answerArea.setPrefRowCount(5);
        answerArea.setWrapText(true);

        Button postButton = new Button("Post Answer");
        postButton.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        postButton.setTextFill(Color.WHITE);
        postButton.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, new CornerRadii(15), Insets.EMPTY)));
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
        postAnswerBox.setStyle("-fx-background-color:rgb(175, 214, 253); -fx-border-color: #ccddee; -fx-border-radius: 15; -fx-background-radius: 15;");
        postAnswerBox.setMaxWidth(500);

        if (currentUser.isAdmin()) {
            Button removeQuestionButton = new Button("Remove Question");
            removeQuestionButton.setStyle("-fx-background-color: pink; -fx-text-fill: white; -fx-background-radius: 10;");
            removeQuestionButton.setOnMouseEntered(e -> {
                removeQuestionButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-background-radius: 10;");
            });
            removeQuestionButton.setOnMouseExited(e -> {
                removeQuestionButton.setStyle("-fx-background-color: pink; -fx-text-fill: white; -fx-background-radius: 10;");
            });

            removeQuestionButton.setOnAction(e -> {
                questionService.removeQuestion(questionId);
                answerService.removeAnswersByQuestionId(questionId);

    // Clear layout and show deletion message
                layout.getChildren().clear();

                Label deletedLabel = new Label("The Question has been deleted.");
                deletedLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

                Button okButton = new Button("OK");
                okButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-background-radius: 10;");
                okButton.setOnAction(okEvent -> {
                    Stage stage = (Stage) layout.getScene().getWindow();
                    stage.close();
                    mainApp.changePage(new QuestionsPage(mainApp, currentUser, questionService, answerService).getView());
                });

                VBox deletedBox = new VBox(20, deletedLabel, okButton);
                deletedBox.setAlignment(Pos.CENTER);
                deletedBox.setPadding(new Insets(50));

                layout.getChildren().add(deletedBox);
            });
            layout.getChildren().add(removeQuestionButton);
        }
        

        Label ansLabel = new Label("Answer");
        ansLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        ansLabel.setPrefSize(500, 15);
        ansLabel.setAlignment(Pos.CENTER);
        ansLabel.setTextFill(Color.WHITE);
        ansLabel.setBackground(new Background(new BackgroundFill(Color.BLUEVIOLET, new CornerRadii(5), Insets.EMPTY)));
        
        VBox answerBox = new VBox(1, ansLabel,answerListView);
        layout.getChildren().addAll(questionBox, answerBox, postAnswerBox);

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
