package ViewModel;

import Model.IModel;
import View.MazeDisplayer;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;
    private MediaPlayer gameSoundTrack;

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


    public void setCharacter(String character){
        MazeDisplayer.setCharacterName(character);
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

    public void saveOriginalMaze(File file){
        model.saveOriginalMaze(file);
    }
    public void saveCurrentMaze(File file){
        model.saveCurrentMaze(file);
    }

    public void loadFile(File file){
        model.loadMaze(file);
    }

    public void closeModel(){
        model.closeModel();
    }

    public void startSoundTrack(String character){
        if (gameSoundTrack != null)
            gameSoundTrack.stop();
        String musicFile = "Resources/Music/" + character + "gameSoundTrack.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        gameSoundTrack = new MediaPlayer(sound);
        gameSoundTrack.play();
    }



    public void setMultiPlayerMode(boolean setMode){
        MazeDisplayer.setMultiPlayerMode(setMode);
    }

}
