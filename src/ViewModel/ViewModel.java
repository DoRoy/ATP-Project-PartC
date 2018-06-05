package ViewModel;

import View.MazeDisplayer;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import Server.*;
import algorithms.mazeGenerators.MyMazeGenerator;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.security.auth.login.Configuration;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class ViewModel implements Initializable {

    @FXML
    public MazeDisplayer mazeDisplayer;
    public javafx.scene.control.TextField txtfld_rowsNum;
    public javafx.scene.control.TextField txtfld_columnsNum;
    public Label lbl_characterRow;
    public Label lbl_characterColumn;


    public Maze getNewMaze(int row, int col) {
        Configurations.run();
        IMazeGenerator mg = Configurations.getGenerators_mazeGenerator();
        Maze newMaze = mg.generate(col, row);
        return newMaze;
    }

    public void generateMaze() {
        int rows = Integer.valueOf(txtfld_rowsNum.getText());
        int columns = Integer.valueOf(txtfld_columnsNum.getText());
        this.mazeDisplayer.setMaze(getNewMaze(rows,columns),rows,columns);
    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void solveMaze(ActionEvent actionEvent) {
        showAlert("Solving maze..");
    }

    public void KeyPressed(KeyEvent keyEvent) {
        int characterRow = mazeDisplayer.getCharacterPositionRow();
        int characterColumn = mazeDisplayer.getCharacterPositionColumn();
        int characterRowNewPosition = characterRow;
        int characterColumnNewPosition = characterColumn;

        if (keyEvent.getCode() == KeyCode.UP) {
            characterRowNewPosition = characterRow - 1;
            characterColumnNewPosition = characterColumn;
        }
        else if (keyEvent.getCode() == KeyCode.DOWN) {
            characterRowNewPosition = characterRow + 1;
            characterColumnNewPosition = characterColumn;
        }
        else if (keyEvent.getCode() == KeyCode.RIGHT) {
            characterRowNewPosition = characterRow;
            characterColumnNewPosition = characterColumn+1;
        }
        else if (keyEvent.getCode() == KeyCode.LEFT) {
            characterRowNewPosition = characterRow;
            characterColumnNewPosition = characterColumn -1;
        }
        else if (keyEvent.getCode() == KeyCode.HOME){
            characterRowNewPosition = 0;
            characterColumnNewPosition = 0;
        }

        //Updates the MazeDisplayer
        mazeDisplayer.setCharacterPosition(characterRowNewPosition, characterColumnNewPosition);

        //Updates the labels
        CharacterRow.set(String.valueOf(mazeDisplayer.getCharacterPositionRow()));
        CharacterColumn.set(String.valueOf(mazeDisplayer.getCharacterPositionColumn()));
        keyEvent.consume();
    }

    //region String Property for Binding
    public StringProperty CharacterRow = new SimpleStringProperty();
    public StringProperty CharacterColumn = new SimpleStringProperty();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Set Binding for Properties
        lbl_characterRow.textProperty().bind(CharacterRow);
        lbl_characterColumn.textProperty().bind(CharacterColumn);
    }
    //endregion
}
