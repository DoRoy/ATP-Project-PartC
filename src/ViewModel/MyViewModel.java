package ViewModel;

import Model.IModel;
import Model.MyModel;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;
    private MediaPlayer gameSoundTrack;
    private boolean isPlayed;

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
        MyModel.setMainCharacterName(character);
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
        isPlayed = true;
    }

    public boolean setSound(){
        if (gameSoundTrack == null)
            return false;
        if (isPlayed) {
            gameSoundTrack.stop();
            isPlayed = false;
        }
        else{
            gameSoundTrack.play();
            isPlayed = true;
        }
        return isPlayed;
    }

    public void setMultiPlayerMode(boolean setMode){
        MyModel.setMultiPlayerMode(setMode);
    }

}
