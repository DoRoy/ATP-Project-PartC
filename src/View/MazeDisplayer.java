package View;

import Model.MazeCharacter;
import Model.MyModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;

import javax.swing.text.Position;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class MazeDisplayer extends Canvas {


    private MazeCharacter mainCharacter = new MazeCharacter("Crash_",0,0);;
    private MazeCharacter secondCharacter = new MazeCharacter("Mask_",0,0);;
    private char[][] mazeCharArr;
    private int[][] mazeSolutionArr;
    private int goalPositionRow;
    private int goalPositionColumn;
    private int rowMazeSize;
    private int colMazeSize;
    private boolean hint;



    public int getMainCharacterRow() {
        return mainCharacter.getCharacterRow();
    }
    public int getMainCharacterColumn() {
        return mainCharacter.getCharacterCol();
    }

    public int getSecondCharacterRow() {
        return secondCharacter.getCharacterRow();
    }
    public int getSecondCharacterColumn() {
        return secondCharacter.getCharacterCol();
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


    public void setSecondCharacterDirection(String direction){
        secondCharacter.setCharacterDirection(direction);
    }

    public void setSecondCharacterPosition(int row, int column) {
        secondCharacter.setCharacterRow(row);
        secondCharacter.setCharacterCol(column);
    }

    public void setMainCharacterDirection(String direction){
        mainCharacter.setCharacterDirection(direction);
    }

    public void setMainCharacterPosition(int row, int column) {
        mainCharacter.setCharacterRow(row);
        mainCharacter.setCharacterCol(column);
    }

    public void setMainCharacterName(String name){
        mainCharacter.setCharacterName(name);
    }
    public void setSecondCharacterName(String name){
        secondCharacter.setCharacterName(name);
    }

    public void redraw() {
        if (mazeCharArr != null) {
            this.setHeight( this.getScene().getHeight() * 6/8);
            this.setWidth( this.getScene().getWidth() * 6/8);
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double minSize = Math.max(colMazeSize,rowMazeSize);
            double cellHeight = canvasHeight / minSize;
            double cellWidth = canvasWidth / minSize;
            double startRow = (canvasHeight/2 - (cellHeight * rowMazeSize / 2)) / cellHeight;
            double startCol = (canvasWidth/2 - (cellWidth * colMazeSize / 2)) / cellWidth;

            try {
                String mainCharacterName = mainCharacter.getCharacterName();
                String secondCharacterName = secondCharacter.getCharacterName();
                GraphicsContext graphicsContext2D = getGraphicsContext2D();
                graphicsContext2D.clearRect(0, 0, getWidth(), getHeight()); //Clears the canvas
                Image wallImage = new Image(new FileInputStream("Resources/Images/" + mainCharacterName + "wall.png"));
                Image solutionImage = new Image(new FileInputStream("Resources/Images/" + mainCharacterName + "showSolution.png"));

                //Draw Maze
                for (int i = 0; i < rowMazeSize; i++) {
                    for (int j = 0; j < colMazeSize; j++) {
                        if (mazeCharArr[i][j] == '1') {
                            graphicsContext2D.drawImage(wallImage, (startCol + j) * cellWidth, (startRow + i) * cellHeight, cellWidth, cellHeight);
                        }
                        else if( mazeCharArr[i][j] == 'E'){
                            setGoalPosition(i, j);
                        }
                    }
                }

                //draw solution
                for(int i = 1; mazeSolutionArr != null && i < mazeSolutionArr.length -1 ;i++){
                    graphicsContext2D.drawImage(solutionImage, (startCol + mazeSolutionArr[i][1]) * cellWidth, (startRow + mazeSolutionArr[i][0]) * cellHeight, cellWidth, cellHeight);
                }


                Image mainCharacterImage = new Image(new FileInputStream("Resources/Characters/" + mainCharacterName + mainCharacter.getCharacterDirection() + ".png"));
                graphicsContext2D.drawImage(mainCharacterImage, (startCol + getMainCharacterColumn()) * cellWidth, (startRow + getMainCharacterRow()) * cellHeight, cellWidth, cellHeight);

                Image secondCharacterImage = new Image(new FileInputStream("Resources/Characters/" + secondCharacterName + secondCharacter.getCharacterDirection() + ".png"));
                graphicsContext2D.drawImage(secondCharacterImage, (startCol + getSecondCharacterColumn()) * cellWidth, (startRow + getSecondCharacterRow()) * cellHeight, cellWidth, cellHeight);

                Image goalImage = new Image(new FileInputStream("Resources/Characters/" + mainCharacterName + "goal.png"));
                graphicsContext2D.drawImage(goalImage, (startCol + goalPositionColumn) * cellWidth, (startRow + goalPositionRow) * cellHeight, cellWidth, cellHeight);

            } catch (FileNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(String.format("Image doesn't exist: %s",e.getMessage()));
                alert.show();
            }
        }
    }


    public void setMazeSolutionArr(int[][] mazeSolutionArr) {
        this.mazeSolutionArr = mazeSolutionArr;
    }


}
