package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("My Application!");
        Parent root = FXMLLoader.load(getClass().getResource("MyView.fxml"));
        Scene scene = new Scene(root,800,700);
        scene.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        //Rise Servers
    }

    public static void main(String[] args) {
        launch(args);
    }
}
