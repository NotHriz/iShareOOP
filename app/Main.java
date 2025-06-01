package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.User;
import pages.QuestionsPage;
import javafx.scene.Parent;
import pages.LoginPage;
import services.*;

public class Main extends Application {

    private Stage primaryStage;  // Save reference so you can change pages later
    private QuestionService questionService = new QuestionService();
    private UserService userService = new UserService(); // Assuming you have a UserService to manage users
    private AnswerService answerService = new AnswerService(); // Assuming you have an AnswerService to manage answers
    private User currentUser;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Load Necessary Services
        questionService.loadQuestions();  // Load questions from file at startup
        userService.loadUsers();  // Load users from file at startup
        answerService.loadAnswers();  // Load answers from file at startup

        // Initialize the LoginPage
        LoginPage loginPage = new LoginPage(this, userService, questionService, answerService); 
        Parent root = loginPage.getView();
        Scene scene = new Scene(root, 800, 600);
        Image icon = new Image("logo.jpg");
        // Set the scene and show the primary stage
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Question Page");
        primaryStage.show();
    }

    // Method to change the current page
    public void changePage(Parent newPage) {
        Scene currentScene = primaryStage.getScene();
        currentScene.setRoot(newPage);
    }

    public void showMainPage() {
        QuestionsPage questionsPage = new QuestionsPage(this, currentUser, questionService, answerService );
        changePage(questionsPage.getView());
    }

    public void popUpNewWindow(Parent newPage) {
        Stage newStage = new Stage();
        Scene newScene = new Scene(newPage, 600, 400);
        newStage.setScene(newScene);
        newStage.show();
    }

    // Method to set the current user
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public UserService getUserService() {
        return userService;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
