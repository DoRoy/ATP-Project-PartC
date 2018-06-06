package Model;

import algorithms.mazeGenerators.Maze;
import javafx.scene.input.KeyCode;

public interface IModel {

    void generateMaze(int width,int height);
    void moveCharacter(KeyCode movement);
    char[][] getMaze();
    int getCharacterPositionRow();
    int getCharacterPositionColumn();
    void closeModel();
    boolean isAtTheEnd();
    String getCharacterDirection();
}
