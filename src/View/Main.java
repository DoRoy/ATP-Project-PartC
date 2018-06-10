package View;

import Model.MyModel;
import Server.*;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.File;
import java.util.Optional;

public class Main extends Application {
    private Server serverMazeGenerator;
    private Server serverSolveMaze;
    @Override
    public void start(Stage primaryStage) throws Exception{
        MyModel model = new MyModel();
        MyViewModel myViewModel = new MyViewModel(model);
        model.addObserver(myViewModel);

        primaryStage.setTitle("The Crash Maze!");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("MyView.fxml").openStream());
        Scene scene = new Scene(root,800,700);
        scene.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());
        primaryStage.setScene(scene);


        String musicFile = "Resources/Music/Crash_gameSound.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });
        mediaPlayer.play();

        MyViewController myViewController = fxmlLoader.getController();
        myViewController.setResizeEvent(scene);
        myViewController.setViewModel(myViewModel);
        myViewModel.addObserver(myViewController);


        //NewGameController gameController = fxmlLoader.getController();
        //gameController.setViewModel(myViewModel);
        //myViewModel.addObserver(gameController);
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
