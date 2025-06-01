package pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
        Label homeLabel = new Label("Home");
        homeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 48)); // Bigger and bolder
        homeLabel.setPadding(new Insets(0, 0, 5, 0));
        homeLabel.setTextFill(Color.BLUEVIOLET);

        // Welcome label
        Label welcomeLabel = new Label("Welcome, @" + currentUser.getUsername());
        welcomeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        welcomeLabel.setTextFill(Color.DEEPSKYBLUE);


        Button logoutButton = new Button("Logout");
        logoutButton.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        logoutButton.setTextFill(Color.WHITE);
        logoutButton.setBackground(new Background(new BackgroundFill(Color.PLUM, new CornerRadii(10), Insets.EMPTY)));
        logoutButton.setOnMouseEntered(e -> {
                logoutButton.setBackground(new Background(new BackgroundFill(Color.BLUEVIOLET, new CornerRadii(10), Insets.EMPTY)));
            });
        logoutButton.setOnMouseExited(e -> {
                logoutButton.setBackground(new Background(new BackgroundFill(Color.PLUM, new CornerRadii(10), Insets.EMPTY)));
            });
        logoutButton.setOnAction(e -> {
            mainApp.changePage(new LoginPage(mainApp, mainApp.getUserService(), questionService, answerService).getView());
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox topBar = new HBox(10,homeLabel, spacer, logoutButton);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 10, 0));

        VBox welcomeBox = new VBox(5, topBar, welcomeLabel);
        welcomeBox.setAlignment(Pos.TOP_LEFT);

        TitledPane postSection = new TitledPane();
        postSection.setText("Post a New Question");
        postSection.setCollapsible(false);
        postSection.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        postSection.setTextFill(Color.DEEPSKYBLUE);
        postSection.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, new CornerRadii(5), Insets.EMPTY)));
        postSection.setBorder(new Border(new BorderStroke(Color.DEEPSKYBLUE,BorderStrokeStyle.SOLID, new CornerRadii(5),new BorderWidths(3))));

        
        Button feedB = new Button("My Feed");
        feedB.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        feedB.setTextFill(Color.WHITE);
        feedB.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, new CornerRadii(15), Insets.EMPTY)));
        
        Button questionB = new Button("Question");
        questionB.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        questionB.setTextFill(Color.WHITE);
        questionB.setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(15), Insets.EMPTY)));

        HBox catogery = new HBox(10, feedB, questionB);

        

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

        Button postButton = new Button("Post !");
        postButton.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        postButton.setTextFill(Color.WHITE);
        postButton.setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(10), Insets.EMPTY)));
        postButton.setOnMouseEntered(e -> {
                postButton.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, new CornerRadii(10), Insets.EMPTY)));
            });
        postButton.setOnMouseExited(e -> {
                postButton.setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(10), Insets.EMPTY)));
            });
            
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
        VBox postS = new VBox(10, postSection);

        // Question list
        Button questionListLabel = new Button("Question");
        questionListLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        questionListLabel.setTextFill(Color.WHITE);
        questionListLabel.setPadding(new Insets(5, 15, 5, 15));
        questionListLabel.setBackground(new Background(new BackgroundFill(Color.BLUEVIOLET, new CornerRadii(15), Insets.EMPTY)));

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

        VBox questionBox = new VBox(10);
        questionBox.setAlignment(Pos.BOTTOM_LEFT);

        feedB.setOnAction(e -> {
            feedB.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, new CornerRadii(15), Insets.EMPTY)));
            questionB.setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(15), Insets.EMPTY)));
            postS.getChildren().add(postSection);
            questionBox.getChildren().removeAll(questionListLabel,questionListView);
        });
        questionB.setOnAction(e -> {
            feedB.setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(15), Insets.EMPTY)));
            questionB.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, new CornerRadii(15), Insets.EMPTY)));
            postS.getChildren().remove(postSection);
            questionBox.getChildren().addAll(questionListLabel,questionListView);
        });

        layout.getChildren().addAll(
                welcomeBox,
                catogery,
                postS,
                new Separator(),
                questionBox
        );
    }

    private void refreshQuestions() {
        questionListView.getItems().setAll(questionService.getAllQuestions());
    }

    public Parent getView() {
        return layout;
    }
}
