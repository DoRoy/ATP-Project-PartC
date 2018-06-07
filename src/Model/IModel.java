package Model;

import algorithms.mazeGenerators.Maze;
import javafx.scene.input.KeyCode;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public interface IModel {

    void generateMaze(int width,int height);
    void moveCharacter(KeyCode movement);
    void generateSolution();
    char[][] getMaze();
    int getCharacterPositionRow();
    int getCharacterPositionColumn();
    void closeModel();
    boolean isAtTheEnd();
    String getCharacterDirection();
    int[][] getSolution();
    int[][] getMazeSolutionArr();
}
