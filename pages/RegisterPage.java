package pages;

import app.Main;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import services.UserService;


public class RegisterPage {
    private VBox layout;
    private final Main mainApp;
    private final UserService userService;

    public RegisterPage(Main mainApp, UserService userService) {
        this.mainApp = mainApp;
        this.userService = userService;
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
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");

        VBox formBox = new VBox(10);
        formBox.setAlignment(Pos.CENTER);
        formBox.setMaxWidth(300);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");

        Button registerButton = new Button("Register");
        registerButton.setPrefWidth(100);
        registerButton.setTextFill(Color.WHITE);  // White text
        registerButton.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE,new CornerRadii(20), null)));

        registerButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String confirm = confirmPasswordField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "All fields are required.");
                return;
            }

            if (!password.equals(confirm)) {
                showAlert(Alert.AlertType.ERROR, "Passwords do not match.");
                return;
            }

            if (userService.userExists(username)) {
                showAlert(Alert.AlertType.ERROR, "User already exists.");
                return;
            }

            userService.addUser(username, password);
            showAlert(Alert.AlertType.INFORMATION, "Registration successful!");
            mainApp.changePage(new LoginPage(mainApp, userService, null, null).getView());
        });

        Button cancelReg = new Button("Cancel Register");
        cancelReg.setPrefWidth(100);
        cancelReg.setTextFill(Color.WHITE);  // White text
        cancelReg.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE,new CornerRadii(20), null)));

        cancelReg.setOnAction(e -> {
            LoginPage loginPage = new LoginPage( mainApp, userService, null, null);
            mainApp.changePage(loginPage.getView());
        });

        HBox buttonBox = new HBox(10, registerButton, cancelReg);
        buttonBox.setAlignment(Pos.CENTER);

        Label footer = new Label("iShare IT Tech Company");
        footer.setTextFill(Color.GRAY);
        footer.setFont(Font.font("System", FontWeight.NORMAL, 14));
        footer.setAlignment(Pos.BOTTOM_CENTER);


        formBox.getChildren().addAll(
            usernameField,
            passwordField,
            confirmPasswordField,
            buttonBox
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
