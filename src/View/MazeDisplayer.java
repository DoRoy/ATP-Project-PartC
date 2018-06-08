package View;

import Model.MazeCharacter;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;


public class MazeDisplayer extends Canvas {


    private MazeCharacter mainCharacter;
    private char[][] mazeCharArr;
    private int[][] mazeSolutionArr;
    private int characterPositionRow;
    private int characterPositionColumn;
    private int goalPositionRow;
    private int goalPositionColumn;
    private int rowMazeSize;
    private int colMazeSize;
    private String characterDirection;
    private static String characterName = "Crash_";
    private static boolean multiPlayerMode = false;

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }



    public void setMaze(char[][] maze){
        mazeCharArr = maze;
        rowMazeSize = maze.length;
        colMazeSize = maze[0].length;
    }

    public void setGoalPosition(int row, int column){
        goalPositionRow = row;
        goalPositionColumn = column;
    }

    public void setCharacterDirection(String direction){
        characterDirection = direction;
    }

    public void setCharacterPosition(int row, int column) {
        characterPositionRow = row;
        characterPositionColumn = column;
    }


    public void redraw() {
        if (mazeCharArr != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / rowMazeSize;
            double cellWidth = canvasWidth / colMazeSize;

            try {
                GraphicsContext graphicsContext2D = getGraphicsContext2D();
                graphicsContext2D.clearRect(0, 0, getWidth(), getHeight()); //Clears the canvas
                Image wallImage = new Image(new FileInputStream("Resources/Images/" + characterName + "wall.png"));
                Image solutionImage = new Image(new FileInputStream("Resources/Images/" + characterName + "showSolution.png"));

                //Draw Maze
                for (int i = 0; i < rowMazeSize; i++) {
                    for (int j = 0; j < colMazeSize; j++) {
                        if (mazeCharArr[i][j] == '1') {
                            graphicsContext2D.drawImage(wallImage, j * cellHeight, i * cellWidth, cellHeight, cellWidth);
                        }
                        else if( mazeCharArr[i][j] == 'E'){
                            setGoalPosition(i, j);
                        }
                    }
                }

                //draw solution
                for(int i = 1; mazeSolutionArr != null && i < mazeSolutionArr.length -1 ;i++){
                    graphicsContext2D.drawImage(solutionImage, mazeSolutionArr[i][1] * cellHeight, mazeSolutionArr[i][0] * cellWidth, cellHeight, cellWidth);
                }


                //Draw Character
                //gc.setFill(Color.RED);
                //gc.fillOval(characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);
                Image characterImage = new Image(new FileInputStream("Resources/Characters/" + characterName + characterDirection + ".png"));
                graphicsContext2D.drawImage(characterImage, characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);

                Image goalImage = new Image(new FileInputStream("Resources/Characters/" + characterName + "goal.png"));
                graphicsContext2D.drawImage(goalImage, goalPositionColumn * cellHeight, goalPositionRow * cellWidth, cellHeight, cellWidth);

            } catch (FileNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(String.format("Image doesn't exist: %s",e.getMessage()));
                alert.show();
            }
        }
    }

    //region Properties
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();
    private StringProperty ImageFileNameGoal = new SimpleStringProperty();
    private StringProperty ImageFileNameSolution = new SimpleStringProperty();

    public String getImageFileNameSolution() {
        return ImageFileNameSolution.get();
    }

    public void setImageFileNameSolution(String imageFileNameSolution) {
        this.ImageFileNameSolution.set(imageFileNameSolution);
    }

    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }

    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);
    }

    public String getImageFileNameGoal() {
        return ImageFileNameGoal.get();
    }

    public void setImageFileNameGoal(String imageFileNameGoal) {
        this.ImageFileNameGoal.set(imageFileNameGoal);
    }

    public void setMazeSolutionArr(int[][] mazeSolutionArr) {
        this.mazeSolutionArr = mazeSolutionArr;
    }

    public static void setCharacterName(String name) {
        characterName = name;
    }

    public static void setMultiPlayerMode(boolean setMode){
        multiPlayerMode = setMode;
    }

    //endregion

}
