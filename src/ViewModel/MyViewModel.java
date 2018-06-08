package ViewModel;

import Model.IModel;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;

    public MyViewModel(IModel model) {
        this.model = model;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o == model){

        }


        setChanged();
        notifyObservers();
    }


    public void moveCharacter(KeyCode movement){
        model.moveCharacter(movement);
    }

    public void generateMaze(int row, int col){
        model.generateMaze(row, col);
    }

    public char[][] getMaze() {
        return model.getMaze();
    }

    public int getCharacterPositionRow() {
        return model.getCharacterPositionRow();
    }


    public String getCharacterDirection() {
        return model.getCharacterDirection();
    }


    public boolean isAtTheEnd() {
        return model.isAtTheEnd();
    }


    public int getCharacterPositionColumn() {
        return model.getCharacterPositionColumn();
    }

    public int[][] getSolution(){
        return model.getSolution();
    }

    public int[][] getMazeSolutionArr(){
        return model.getMazeSolutionArr();
    }

    public void generateSolution(){
        model.generateSolution();
    }

    public void saveMaze(File file){
        model.saveMaze(file);
    }

    public void loadFile(File file){
        model.loadMaze(file);
    }

}
