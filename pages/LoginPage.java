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
import pages.QuestionsPage;
public class LoginPage {
    private VBox layout;
    private final Main mainApp;
    private final UserService userService;
    private final QuestionService questionService;
    private final AnswerService answerService;

    // Constructor to initialize the LoginPage
    public LoginPage(Main mainApp, UserService userService, QuestionService questionService, AnswerService answerService) {
        this.answerService = answerService;
        this.mainApp = mainApp;
        this.userService = userService;
        this.questionService = questionService;
        buildUI();
    }

    // Build the UI for the login page
    public void buildUI() {
        layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("Login to Your Account");
        
        // Create a TextField for the username
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        // Create a PasswordField for the password
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        // Create a Button to submit the Login
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            // Validate input (check if fields are not null)
            if (username.isEmpty() || password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Username and password cannot be empty.");
                alert.showAndWait();
                return;
            }

            // Authenticate the user
            User user = userService.authenticate(username, password);
            if (user != null) {
                mainApp.setCurrentUser(user);

                // Check if the user is an admin
                if (user.isAdmin()) {
                    // mainApp.showAdminPage();
                } else {
                    QuestionsPage questionsPage = new QuestionsPage(mainApp, user, questionService, answerService);
                    mainApp.changePage(questionsPage.getView());;
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid username or password.");
                alert.showAndWait();
            }
        });

        layout.getChildren().addAll(
            titleLabel,
            usernameLabel, usernameField,
            passwordLabel, passwordField,
            loginButton
        );
    }

    public Parent getView() {
        return layout;
    }


}
