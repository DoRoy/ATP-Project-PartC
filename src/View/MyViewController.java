package View;

import ViewModel.*;
import com.sun.javafx.stage.EmbeddedWindow;
import com.sun.javafx.stage.WindowCloseRequestHandler;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import java.util.*;

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
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
        //mazeDisplayer.widthProperty().bind(scene.widthProperty().multiply(6/8));
        //mazeDisplayer.heightProperty().bind(scene.heightProperty().multiply(7/8));
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mazeDisplayer.redraw();
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mazeDisplayer.redraw();
            }
        });
        mazeDisplayer.redraw();
    }

    public void exitButton(){
        System.out.println("Exit button");
        exitCorrectly();
    }


    public void exitCorrectly(){
        //TODO check how exit button in menu will send this a close request window event
        System.out.println("Exit Correctly");
        Alert alert = new Alert(Alert.AlertType.NONE    );
        ButtonType leaveButton = new ButtonType("Leave", ButtonBar.ButtonData.NO);
        ButtonType stayButton = new ButtonType("Stay", ButtonBar.ButtonData.YES);
        alert.getButtonTypes().setAll(stayButton,leaveButton);
        alert.setContentText("Are you sure you want to exit??");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == leaveButton){
            System.out.println("LeaveButton was clicked");
            // ... user chose OK
            // Close program
            myViewModel.closeModel();
            Platform.exit();
        } else {
            // ... user chose CANCEL or closed the dialog
            System.out.println("StayButton was clicked");
            alert.close();
        }

    }


    public void saveFile(ActionEvent event){
        System.out.println("saveFile");
        int[] choose = {0};
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Save Maze");
        alert.setContentText("What maze do you want to save?");
        ButtonType okButton = new ButtonType("Current", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Original", ButtonBar.ButtonData.NO);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButton, noButton, cancelButton);
        alert.showAndWait().ifPresent(type -> {
            if (type == okButton) {//Current
                choose[0] = 1;
            } else if (type == noButton) {//Original
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
        if(file != null && file.exists() && !file.isDirectory()){
            myViewModel.loadFile(file);
            lbl_statusBar.setText("Loaded " + file.getName());
        }
        event.consume();
    }

    public void newMaze(){

    }
}
