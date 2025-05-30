import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Main extends Application {
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        showLoginPage();
        stage.setTitle("iShare");
        stage.show();
    }

    public void showLoginPage() {
        LoginPage login = new LoginPage(this);
        primaryStage.setScene(new Scene(login, 400, 300));
    }

    public void showMainPage() {
        MainPage main = new MainPage(this);
        primaryStage.setScene(new Scene(main, 800, 600));
    }

    
}
