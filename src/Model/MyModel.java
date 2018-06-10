package Model;

import Client.*;
import IO.MyDecompressorInputStream;
import Server.*;
import algorithms.mazeGenerators.Maze;
import algorithms.search.*;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;

public class MyModel extends Observable implements IModel {

    private MazeCharacter mainCharacter;
    private MazeCharacter secondCharacter;
    private Maze maze;
    private Solution mazeSolution;
    private boolean isAtTheEnd;
    private int[][] mazeSolutionArr;
    private boolean multiPlayerMode = false;
    private String mainCharacterName = "Crash_";
    private String secondCharacterName = "Mask_";


    public int[][] getMazeSolutionArr() {
        return mazeSolutionArr;
    }

    @Override
    public void saveCurrentMaze(File file) {

    }

    @Override
    public void saveOriginalMaze(File file) {

    }


    public MyModel(){
        Configurations.run(); // TODO should be removed
        isAtTheEnd = false;
    }


    @Override
    public void generateMaze(int row, int col) {
        try {
            Client clientMazeGenerator = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{row, col};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[mazeDimensions[0] * mazeDimensions[1] + 12 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        maze = new Maze(decompressedMaze);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            clientMazeGenerator.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


        int mazeRow = maze.getStartPosition().getRowIndex();
        int mazeCol = maze.getStartPosition().getColumnIndex();
        mainCharacter = new MazeCharacter("Crash_",mazeRow,mazeCol);
        secondCharacter = new MazeCharacter("Mask_", mazeRow,mazeCol);

        isAtTheEnd = false;
        mazeSolutionArr = null;

        setChanged();
        notifyObservers();
    }

    @Override
    public void moveCharacter(KeyCode movement) {
        mazeSolutionArr = null;
        int mainCharacterPositionRow = mainCharacter.getCharacterRow();
        int mainCharacterPositionCol = mainCharacter.getCharacterCol();
        switch(movement){
            case UP:
            case NUMPAD8:
                mainCharacter.setCharacterDirection("back");
                if(isNotWall(mainCharacterPositionRow - 1, mainCharacterPositionCol )) {
                    mainCharacter.setCharacterRow(mainCharacterPositionRow - 1);
                    if(!multiPlayerMode){
                        secondCharacter.setCharacterRow(mainCharacterPositionRow);
                        secondCharacter.setCharacterCol(mainCharacterPositionCol);
                    }
                }
                break;
            case DOWN:
            case NUMPAD2:
                mainCharacter.setCharacterDirection("front");
                if(isNotWall(mainCharacterPositionRow + 1, mainCharacterPositionCol)) {
                    mainCharacter.setCharacterRow(mainCharacterPositionRow + 1);
                    if(!multiPlayerMode){
                        secondCharacter.setCharacterRow(mainCharacterPositionRow);
                        secondCharacter.setCharacterCol(mainCharacterPositionCol);
                    }
                }
                break;
            case LEFT:
            case NUMPAD4:
                mainCharacter.setCharacterDirection("left");
                if(isNotWall(mainCharacterPositionRow, mainCharacterPositionCol - 1)) {
                    mainCharacter.setCharacterCol(mainCharacterPositionCol - 1);
                    if(!multiPlayerMode){
                        secondCharacter.setCharacterRow(mainCharacterPositionRow);
                        secondCharacter.setCharacterCol(mainCharacterPositionCol);
                    }
                }
                break;
            case RIGHT:
            case NUMPAD6:
                mainCharacter.setCharacterDirection("right");
                if(isNotWall(mainCharacterPositionRow, mainCharacterPositionCol + 1)) {
                    mainCharacter.setCharacterCol(mainCharacterPositionCol + 1);
                    if(!multiPlayerMode){
                        secondCharacter.setCharacterRow(mainCharacterPositionRow);
                        secondCharacter.setCharacterCol(mainCharacterPositionCol);
                    }
                }
                break;
            case NUMPAD7:
                mainCharacter.setCharacterDirection("left");
                if(isNotWall(mainCharacterPositionRow - 1, mainCharacterPositionCol - 1) && (isNotWall(mainCharacterPositionRow, mainCharacterPositionCol - 1) || isNotWall(mainCharacterPositionRow - 1, mainCharacterPositionCol) )){
                    mainCharacter.setCharacterRow(mainCharacterPositionRow - 1);
                    mainCharacter.setCharacterCol(mainCharacterPositionCol - 1);
                    if(!multiPlayerMode){
                        secondCharacter.setCharacterRow(mainCharacterPositionRow);
                        secondCharacter.setCharacterCol(mainCharacterPositionCol);
                    }

                }
                break;
            case NUMPAD9:
                mainCharacter.setCharacterDirection("right");
                if(isNotWall(mainCharacterPositionRow - 1, mainCharacterPositionCol + 1) && (isNotWall(mainCharacterPositionRow, mainCharacterPositionCol + 1) || isNotWall(mainCharacterPositionRow - 1, mainCharacterPositionCol) )){
                    mainCharacter.setCharacterRow(mainCharacterPositionRow - 1);
                    mainCharacter.setCharacterCol(mainCharacterPositionCol + 1);
                    if(!multiPlayerMode){
                        secondCharacter.setCharacterRow(mainCharacterPositionRow);
                        secondCharacter.setCharacterCol(mainCharacterPositionCol);
                    }
                }
                break;
            case NUMPAD1:
                mainCharacter.setCharacterDirection("left");
                if(isNotWall(mainCharacterPositionRow + 1, mainCharacterPositionCol - 1) && (isNotWall(mainCharacterPositionRow, mainCharacterPositionCol - 1) || isNotWall(mainCharacterPositionRow + 1, mainCharacterPositionCol) )){
                    mainCharacter.setCharacterRow(mainCharacterPositionRow + 1);
                    mainCharacter.setCharacterCol(mainCharacterPositionCol - 1);
                    if(!multiPlayerMode){
                        secondCharacter.setCharacterRow(mainCharacterPositionRow);
                        secondCharacter.setCharacterCol(mainCharacterPositionCol);
                    }
                }
                break;
            case NUMPAD3:
                mainCharacter.setCharacterDirection("right");
                if(isNotWall(mainCharacterPositionRow + 1, mainCharacterPositionCol + 1) && (isNotWall(mainCharacterPositionRow, mainCharacterPositionCol + 1) || isNotWall(mainCharacterPositionRow + 1, mainCharacterPositionCol) )){
                    mainCharacter.setCharacterRow(mainCharacterPositionRow + 1);
                    mainCharacter.setCharacterCol(mainCharacterPositionCol + 1);
                    if(!multiPlayerMode){
                        secondCharacter.setCharacterRow(mainCharacterPositionRow);
                        secondCharacter.setCharacterCol(mainCharacterPositionCol);
                    }
                }
                break;

            default:
                break;
        }

        if(maze.getCharAt(mainCharacterPositionRow, mainCharacterPositionCol) == 'E')
            isAtTheEnd = true;

        setChanged();
        notifyObservers();

    }

    @Override
    public void generateSolution() {
        try {
            Client clientSolveMaze = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(new Maze(maze, mainCharacter.getCharacterRow(), mainCharacter.getCharacterCol())); //send maze to server
                        toServer.flush();
                        mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });
            clientSolveMaze.communicateWithServer();
        }catch (Exception e){

        }
        mazeSolutionArr = mazeSolution.getSolution();
        setChanged();
        notifyObservers();
    }

    private boolean isNotWall(int row, int col){
        char c = maze.getCharAt(row, col);
        return (c == 'S' || c == '0' || c == 'E');
    }

    @Override
    public char[][] getMaze() {
        return maze.getMaze();
    }

    @Override
    public int getMainCharacterPositionRow() {
        return mainCharacter.getCharacterRow();
    }

    @Override
    public int getMainCharacterPositionColumn() {
        return mainCharacter.getCharacterCol();
    }

    @Override
    public String getMainCharacterDirection() {
        return mainCharacter.getCharacterDirection();
    }



    public int getSecondCharacterPositionRow() {
        return secondCharacter.getCharacterRow();
    }

    public int getSecondCharacterPositionColumn() {
        return secondCharacter.getCharacterCol();
    }

    public String getSecondCharacterDirection() {
        return secondCharacter.getCharacterDirection();
    }


    @Override
    public int[][] getSolution() {
       return mazeSolution.getSolution();
    }

    @Override
    public boolean isAtTheEnd() {
        return isAtTheEnd;
    }


    @Override
    public void closeModel() {
        //TODO implement

    }


    public void saveMaze(File file){
        try {
            FileOutputStream fileWriter = null;
            fileWriter = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileWriter);
            objectOutputStream.writeObject(maze);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileWriter.close();
        } catch (IOException ex) {

        }
    }

    public void loadMaze(File file){
        try{
            FileInputStream fin = new FileInputStream(file);
            ObjectInputStream oin = new ObjectInputStream(fin);
            Maze loadedMaze = (Maze) oin.readObject();
            if(loadedMaze != null) {
                maze = loadedMaze;
                setChanged();
                notifyObservers();
            }
            else{
                //TODO maybe add something here
            }
            fin.close();
            oin.close();
        }catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setMultiPlayerMode(boolean setMode){
        multiPlayerMode = setMode;
    }

    public void setMainCharacterName(String name) {
        mainCharacterName = name;
    }
    public void setSecondCharacterName(String name) {
        secondCharacterName = name;
    }

    public String getMainCharacterName(){
        return mainCharacterName;
    }

    public String getSecondCharacterName(){
        return secondCharacterName;
    }

}
