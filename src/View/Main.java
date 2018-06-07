package View;

import Model.IModel;
import Model.MyModel;
import Server.*;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private Server serverMazeGenerator;
    private Server serverSolveMaze;
    @Override
    public void start(Stage primaryStage) throws Exception{
        MyModel model = new MyModel();
        MyViewModel myViewModel = new MyViewModel(model);
        model.addObserver(myViewModel);

        //TODO check if can be changed to IMODEL

        primaryStage.setTitle("My Application!");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("MyView.fxml").openStream());
        Scene scene = new Scene(root,800,700);
        scene.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());
        primaryStage.setScene(scene);

        MyViewController myViewController = fxmlLoader.getController();
        myViewController.setViewModel(myViewModel);
        myViewModel.addObserver(myViewController);
        primaryStage.show();
        //Rise Servers
        startServers(); //TODO remove all server things

    }
    private void startServers(){
        serverMazeGenerator = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        serverSolveMaze = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        serverMazeGenerator.start();
        serverSolveMaze.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
