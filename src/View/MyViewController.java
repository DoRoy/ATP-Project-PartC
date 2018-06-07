package View;

import ViewModel.*;
import com.sun.javafx.stage.EmbeddedWindow;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.PopupWindow;
import javafx.stage.Window;

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
                // String musicFile = "StayTheNight.mp3";     // For example
//
                // Media sound = new Media(new File(musicFile).toURI().toString());
                // MediaPlayer mediaPlayer = new MediaPlayer(sound);
                // mediaPlayer.play();
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
        keyEvent.consume();
    }

    public void generateMaze(){
        int height = Integer.valueOf(txtfld_rowsNum.getText());
        int width = Integer.valueOf(txtfld_columnsNum.getText());
        mazeDisplayer.setMazeSolutionArr(null);
        solve_btn.setVisible(true);
        myViewModel.generateMaze(height, width);
    }

    public void solveMaze(ActionEvent actionEvent){
        //TODO implement
        myViewModel.generateSolution();
        int[][] solutionArr = myViewModel.getSolution();
        mazeDisplayer.setMazeSolutionArr(solutionArr);
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

            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

            }
        });
    }

    public void exitButton(ActionEvent event){
        System.out.println("Exit button");
        ChoiceDialog exitDialog = new ChoiceDialog();
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        exitDialog.setContentText("Are you sure you want to exit?");
        exitDialog.setSelectedItem(yesButton);
        exitDialog.setSelectedItem(noButton);
        exitDialog.showAndWait();
        event.consume();

    }

    public void saveFile(ActionEvent event){
        System.out.println("saveFile");
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Choose a directory to save the maze in");
        fileChooser.setInitialDirectory(new File("./Mazes/"));
        //Set extension filter
        //FileChooser.ExtensionFilter extFilter = new FileChooser();
        //fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(new PopupWindow() {
        });

        if(file != null) {
            myViewModel.saveMaze( file);
        }
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
        }
    }
}
