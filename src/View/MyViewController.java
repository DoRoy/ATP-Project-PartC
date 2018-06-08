package View;

import ViewModel.*;
import com.sun.javafx.stage.EmbeddedWindow;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MyViewController implements IView, Observer, Initializable {

    @FXML
    private MyViewModel myViewModel;
    public MazeDisplayer mazeDisplayer;
    public javafx.scene.control.TextField txtfld_rowsNum;
    public javafx.scene.control.TextField txtfld_columnsNum;
    public Label lbl_characterRow;
    public Label lbl_characterColumn;
    public Label lbl_statusBar;
    public Button solve_btn;
    private  static Stage tempStage;

    //region String Property for Binding
    public StringProperty CharacterRow = new SimpleStringProperty();
    public StringProperty CharacterColumn = new SimpleStringProperty();



    public void setViewModel(MyViewModel myViewModel){
        this.myViewModel = myViewModel;
    }
    @Override
    public void update(Observable o, Object arg) {
        if(o == myViewModel) {
            mazeDisplayer.setMaze(myViewModel.getMaze());
            mazeDisplayer.setCharacterPosition(myViewModel.getCharacterPositionRow(), myViewModel.getCharacterPositionColumn());
            mazeDisplayer.setCharacterDirection(myViewModel.getCharacterDirection());
            CharacterColumn.set(myViewModel.getCharacterPositionColumn() + "");
            CharacterRow.set(myViewModel.getCharacterPositionRow() + "");
            if(myViewModel.getMazeSolutionArr() != null)
                mazeDisplayer.setMazeSolutionArr(myViewModel.getSolution());
            else
                mazeDisplayer.setMazeSolutionArr(null);
            if(myViewModel.isAtTheEnd()){
                solve_btn.setVisible(false);
                Alert alert = new Alert(Alert.AlertType.NONE);
                alert.setContentText(String.format("Congratulations!!"));
                alert.show();
            }
            mazeDisplayer.redraw();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Set Binding for Properties
        lbl_characterRow.textProperty().bind(CharacterRow);
        lbl_characterColumn.textProperty().bind(CharacterColumn);

    }

    public void KeyPressed(KeyEvent keyEvent){
        myViewModel.moveCharacter(keyEvent.getCode());
        mazeDisplayer.setMazeSolutionArr(null);
        lbl_statusBar.setText("");
        keyEvent.consume();
    }

    public void generateMaze(){
        int height = Integer.valueOf(txtfld_rowsNum.getText());
        int width = Integer.valueOf(txtfld_columnsNum.getText());
        mazeDisplayer.setMazeSolutionArr(null);
        solve_btn.setVisible(true);
        myViewModel.generateMaze(height, width);
        lbl_statusBar.setText("Lets see if you solve this!");
    }

    public void solveMaze(ActionEvent actionEvent){
        //TODO implement
        myViewModel.generateSolution();
        int[][] solutionArr = myViewModel.getSolution();
        mazeDisplayer.setMazeSolutionArr(solutionArr);
        lbl_statusBar.setText("Here's the solution");
    }

    public void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void setResizeEvent(Scene scene){
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mazeDisplayer.widthProperty().bind(scene.widthProperty());
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mazeDisplayer.heightProperty().bind(scene.heightProperty());
            }
        });
    }

    public void exitButton(ActionEvent event){
        //TODO implement currectly
        System.out.println("Exit button");
        Alert exitDialog = new Alert(Alert.AlertType.NONE);
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);

        exitDialog.setContentText("Are you sure you want to exit?");
        exitDialog.getButtonTypes().setAll(yesButton,noButton);
        exitDialog.showAndWait().ifPresent(buttonType -> {
            if(buttonType == ButtonType.YES)
                myViewModel.closeModel();
        });
        event.consume();

    }


    public void saveFile(ActionEvent event){
        System.out.println("saveFile");
        final int[] choose = {0};
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Save Maze");
        alert.setContentText("What maze do you want to save?");
        ButtonType okButton = new ButtonType("Current", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Original", ButtonBar.ButtonData.NO);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButton, noButton, cancelButton);
        alert.showAndWait().ifPresent(type -> {
            if (type == ButtonType.YES) {//Current
                choose[0] = 1;
            } else if (type == ButtonType.NO) {//Original
                choose[0] = 2;
            } else {//Cancel
                choose[0] = 0;
            }
        });
        if(choose[0] == 0) {
            lbl_statusBar.setText("Save was canceled");
            return;
        }
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Choose a directory to save the maze in");
        fileChooser.setInitialDirectory(new File("./Mazes/"));

        //Show save file dialog
        File file = fileChooser.showSaveDialog(new PopupWindow() {
        });

        if(file != null) {
            if(choose[0] == 1) {//Current
                myViewModel.saveOriginalMaze(file);
                lbl_statusBar.setText("Saved current maze");
            }
            else {//Original
                myViewModel.saveOriginalMaze(file);
                lbl_statusBar.setText("Saved original maze");
            }
        }

        event.consume();
    }



    public void loadFile(ActionEvent event){
        System.out.println("loadFile");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a maze to load");
        fileChooser.setInitialDirectory(new File("./Mazes/"));
        File file = fileChooser.showOpenDialog(new PopupWindow() {
        });
        if(file != null){
            myViewModel.loadFile(file);
            lbl_statusBar.setText("Loaded " + file.getName());
        }
        event.consume();
    }

    public void newMaze(){
        try {
            tempStage = new Stage();
            tempStage.setTitle("Create a new game");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("NewGame.fxml").openStream());
            Scene scene = new Scene(root, 600, 500);
            NewGameController gameController = fxmlLoader.getController();
            gameController.setViewModel(myViewModel);
            myViewModel.addObserver(gameController);
            tempStage.setScene(scene);
            tempStage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            tempStage.showAndWait();
        } catch (Exception e) {

        }
    }

    public static void closeTempStage(){
        tempStage.close();
    }


    public void About(ActionEvent actionEvent) {

    }

    public void setSound(){

    }
}
