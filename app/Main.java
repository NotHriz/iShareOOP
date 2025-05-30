package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;
import services.QuestionService;
import pages.QuestionsPage;

public class Main extends Application {

    private Stage primaryStage;  
    private QuestionService questionService = new QuestionService();

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Temporary hardcoded user for demo purposes
        User loggedInUser = new User("Ali","password123");

        // Load QuestionsPage
        questionService.loadQuestions();  // Load questions from file at startup
        QuestionsPage questionsPage = new QuestionsPage(this, loggedInUser, questionService);
        Scene scene = new Scene(questionsPage.getView(), 700, 500);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Question Page");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
