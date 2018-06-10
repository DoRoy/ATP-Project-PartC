package View;

import Model.MyModel;
import Server.*;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
        Scene scene = new Scene(root,800,800);
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
        myViewController.setResizeEvent(scene);
        myViewModel.addObserver(myViewController);
        myViewModel.addObserver(gameController);

        SetStageCloseEvent(primaryStage, myViewController);
        primaryStage.show();

        //Rise Servers

    }

    private void SetStageCloseEvent(Stage primaryStage, MyViewController myViewController) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                myViewController.exitCorrectly();
                windowEvent.consume();
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
