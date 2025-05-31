package pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.User;
import services.AnswerService;
import services.QuestionService;
import services.UserService;
import app.Main;

public class LoginPage {
    private VBox layout;
    private final Main mainApp;
    private final UserService userService;
    private final QuestionService questionService;
    private final AnswerService answerService;

    public LoginPage(Main mainApp, UserService userService, QuestionService questionService, AnswerService answerService) {
        this.mainApp = mainApp;
        this.userService = userService;
        this.questionService = questionService;
        this.answerService = answerService;
        buildUI();
    }

    public void buildUI() {
        layout = new VBox(20);
        layout.setPadding(new Insets(40));
        layout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Login to Your Account");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setVgap(10);
        formGrid.setHgap(10);
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setPadding(new Insets(10));

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        TextField visiblePasswordField = new TextField();
        visiblePasswordField.setManaged(false);
        visiblePasswordField.setVisible(false);
        visiblePasswordField.setPromptText("Enter your password");

        CheckBox showPasswordCheckBox = new CheckBox("Show Password");
        showPasswordCheckBox.setOnAction(e -> {
            if (showPasswordCheckBox.isSelected()) {
                visiblePasswordField.setText(passwordField.getText());
                visiblePasswordField.setManaged(true);
                visiblePasswordField.setVisible(true);
                passwordField.setManaged(false);
                passwordField.setVisible(false);
            } else {
                passwordField.setText(visiblePasswordField.getText());
                passwordField.setManaged(true);
                passwordField.setVisible(true);
                visiblePasswordField.setManaged(false);
                visiblePasswordField.setVisible(false);
            }
        });

        passwordField.textProperty().addListener((obs, oldText, newText) -> {
            if (!showPasswordCheckBox.isSelected()) {
                visiblePasswordField.setText(newText);
            }
        });

        visiblePasswordField.textProperty().addListener((obs, oldText, newText) -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setText(newText);
            }
        });

        formGrid.add(usernameLabel, 0, 0);
        formGrid.add(usernameField, 1, 0);
        formGrid.add(passwordLabel, 0, 1);
        formGrid.add(passwordField, 1, 1);
        formGrid.add(visiblePasswordField, 1, 1);
        formGrid.add(showPasswordCheckBox, 1, 2);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = showPasswordCheckBox.isSelected()
                    ? visiblePasswordField.getText().trim()
                    : passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Username and password cannot be empty.");
                alert.showAndWait();
                return;
            }

            User user = userService.authenticate(username, password);
            if (user != null) {
                mainApp.setCurrentUser(user);
                QuestionsPage questionsPage = new QuestionsPage(mainApp, user, questionService, answerService);
                mainApp.changePage(questionsPage.getView());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid username or password.");
                alert.showAndWait();
            }
        });

        registerButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = showPasswordCheckBox.isSelected()
                    ? visiblePasswordField.getText().trim()
                    : passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Username and password cannot be empty.");
                alert.showAndWait();
                return;
            }

            if (userService.userExists(username)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "User already exists.");
                alert.showAndWait();
                return;
            }

            userService.addUser(username, password);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Registration successful! You can now log in.");
            alert.showAndWait();
        });

        buttonBox.getChildren().addAll(loginButton, registerButton);

        layout.getChildren().addAll(titleLabel, formGrid, buttonBox);
    }

    public Parent getView() {
        return layout;
    }
}
