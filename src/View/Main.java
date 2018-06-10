package View;

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

        primaryStage.setTitle("The Crash Maze!");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("MyView.fxml").openStream());
        Scene scene = new Scene(root,1500,1500);
        scene.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());
        primaryStage.setScene(scene);

        primaryStage.resizableProperty();

        MyViewController myViewController = fxmlLoader.getController();

        FXMLLoader fxmlGameLoader = new FXMLLoader();
        Parent gameRoot = fxmlGameLoader.load(getClass().getResource("NewGame.fxml").openStream());
        NewGameController gameController = fxmlGameLoader.getController();
        gameController.setViewModel(myViewModel);

        Scene gameScene = new Scene(gameRoot, 600, 500);
        myViewController.setNewGameScene(gameScene);
        myViewController.setViewModel(myViewModel);
        myViewController.setMainScene(scene);
        myViewModel.addObserver(myViewController);
        myViewModel.addObserver(gameController);


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
