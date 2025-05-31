package pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
    private int loginAttempts = 0;

    public LoginPage(Main mainApp, UserService userService, QuestionService questionService, AnswerService answerService) {
        this.mainApp = mainApp;
        this.userService = userService;
        this.questionService = questionService;
        this.answerService = answerService;
        buildUI();
    }

    public void buildUI() {
        layout = new VBox(10);
        layout.setPadding(new Insets(40));
        layout.setAlignment(Pos.TOP_CENTER);
        
        Image logo = new Image("logo.jpg");
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(200);  
        logoView.setFitHeight(200);
       

        Label subtitleLabel = new Label("your answer is here");
        subtitleLabel.setTextFill(Color.GRAY);
        subtitleLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));

        VBox formBox = new VBox(10);
        formBox.setAlignment(Pos.CENTER);
        formBox.setMaxWidth(300);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(Double.MAX_VALUE);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(Double.MAX_VALUE);

        TextField visiblePasswordField = new TextField();
        visiblePasswordField.setPromptText("Password");
        visiblePasswordField.setVisible(false);
        visiblePasswordField.setManaged(false);
        visiblePasswordField.setMaxWidth(Double.MAX_VALUE);

        CheckBox showPasswordCheckBox = new CheckBox("Show Password");
        showPasswordCheckBox.setOnAction(e -> {
            boolean isSelected = showPasswordCheckBox.isSelected();
            visiblePasswordField.setText(passwordField.getText());
            visiblePasswordField.setVisible(isSelected);
            visiblePasswordField.setManaged(isSelected);
            passwordField.setVisible(!isSelected);
            passwordField.setManaged(!isSelected);
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

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(100);
        loginButton.setTextFill(Color.WHITE);  // White text
        loginButton.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE,new CornerRadii(20), null)));

        Button registerButton = new Button("Register");
        registerButton.setPrefWidth(100);
        registerButton.setTextFill(Color.WHITE);  
        registerButton.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE,new CornerRadii(20), null)));

        Label forgotPasswordLabel = new Label("Forgot password?");
        forgotPasswordLabel.setStyle("-fx-text-fill: blue; -fx-border-color: lightgray; -fx-padding: 5;");
        forgotPasswordLabel.setVisible(false);

        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = showPasswordCheckBox.isSelected() ? visiblePasswordField.getText().trim() : passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Username and password cannot be empty.");
                return;
            }

            User user = userService.authenticate(username, password);
            if (user != null) {
                loginAttempts = 0;
                mainApp.setCurrentUser(user);
                QuestionsPage questionsPage = new QuestionsPage(mainApp, user, questionService, answerService);
                mainApp.changePage(questionsPage.getView());
            } else {
                loginAttempts++;
                showAlert(Alert.AlertType.ERROR, "Invalid username or password.");
                if (loginAttempts >= 3) {
                    forgotPasswordLabel.setVisible(true);
                }
            }
        });

        registerButton.setOnAction(e -> {
            RegisterPage registerPage = new RegisterPage(mainApp, userService);
            mainApp.changePage(registerPage.getView());
        });

        buttonBox.getChildren().addAll(loginButton, registerButton);

        Label footer = new Label("iShare IT Tech Company");
        footer.setTextFill(Color.GREY);
        footer.setFont(Font.font("System", FontWeight.NORMAL, 14));
        footer.setAlignment(Pos.BOTTOM_CENTER);

        formBox.getChildren().addAll(
            usernameField,
            passwordField,
            visiblePasswordField,
            showPasswordCheckBox,
            buttonBox,
            forgotPasswordLabel
        );

        

        layout.getChildren().addAll(logoView, subtitleLabel, formBox, footer);
        layout.setAlignment(Pos.CENTER);
    }

    private void showAlert(Alert.AlertType type, String message) {
        new Alert(type, message).showAndWait();
    }

    public Parent getView() {
        return layout;
    }
}
