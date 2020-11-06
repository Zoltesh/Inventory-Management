import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    static Stage window;
    static Scene mainWindow;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("MainUI.fxml"));

        window = primaryStage;
        window.setTitle("Inventory Management System");
        mainWindow = new Scene(root, 1400, 700);

        window.setScene(mainWindow);
        window.show();

    }








}
