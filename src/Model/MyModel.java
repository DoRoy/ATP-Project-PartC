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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MyModel extends Observable implements IModel {

    //TODO-Add Scroll with the mouse - 5 points
    //TODO-Drag the character with the mouse - 10 points





    private MazeCharacter mainCharacter;
    private MazeCharacter secondCharacter;
    //private int characterPositionRow;
    //private int characterPositionCol;
    //private String characterDirection = "front";
    private Maze maze;
    private Solution mazeSolution;
    private boolean isAtTheEnd;
    private int[][] mazeSolutionArr;
    private Server serverMazeGenerator;
    private Server serverSolveMaze;
    private String mainCharacterName = "Crash_";

    private ExecutorService threadPool = Executors.newCachedThreadPool();;

    public int[][] getMazeSolutionArr() {
        return mazeSolutionArr;
    }

    public MyModel(){
        Configurations.run(); // TODO should be removed
        isAtTheEnd = false;
        startServers(); //TODO remove all server things

    }

    private void startServers(){
        serverMazeGenerator = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        serverSolveMaze = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        serverMazeGenerator.start();
        serverSolveMaze.start();
    }

    private void closeServers(){
        serverMazeGenerator.stop();
        serverSolveMaze.stop();
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
                        toServer.close();
                        fromServer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            threadPool.execute(() ->{
                clientMazeGenerator.communicateWithServer();
                int mazeRow = maze.getStartPosition().getRowIndex();
                int mazeCol = maze.getStartPosition().getColumnIndex();
                mainCharacter = new MazeCharacter("Crash_",mazeRow,mazeCol);
                secondCharacter = new MazeCharacter("Mask_", mazeRow,mazeCol);

                isAtTheEnd = false;
                mazeSolutionArr = null;
                setChanged();
                notifyObservers("Maze");
            });
            //clientMazeGenerator.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


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
                if(isNotWall(mainCharacterPositionRow - 1, mainCharacterPositionCol ))
                    mainCharacter.setCharacterRow(mainCharacterPositionRow - 1);
                break;
            case DOWN:
            case NUMPAD2:
                mainCharacter.setCharacterDirection("front");
                if(isNotWall(mainCharacterPositionRow + 1, mainCharacterPositionCol))
                    mainCharacter.setCharacterRow(mainCharacterPositionRow + 1);
                break;
            case LEFT:
            case NUMPAD4:
                mainCharacter.setCharacterDirection("left");
                if(isNotWall(mainCharacterPositionRow, mainCharacterPositionCol - 1))
                    mainCharacter.setCharacterCol(mainCharacterPositionCol - 1);
                break;
            case RIGHT:
            case NUMPAD6:
                mainCharacter.setCharacterDirection("right");
                if(isNotWall(mainCharacterPositionRow, mainCharacterPositionCol + 1))
                    mainCharacter.setCharacterCol(mainCharacterPositionCol + 1);
                break;
            case NUMPAD7:
                mainCharacter.setCharacterDirection("left");
                if(isNotWall(mainCharacterPositionRow - 1, mainCharacterPositionCol - 1) && (isNotWall(mainCharacterPositionRow, mainCharacterPositionCol - 1) || isNotWall(mainCharacterPositionRow - 1, mainCharacterPositionCol) )){
                    mainCharacter.setCharacterRow(mainCharacterPositionRow - 1);
                    mainCharacter.setCharacterCol(mainCharacterPositionCol - 1);
                }
                break;
            case NUMPAD9:
                mainCharacter.setCharacterDirection("right");
                if(isNotWall(mainCharacterPositionRow - 1, mainCharacterPositionCol + 1) && (isNotWall(mainCharacterPositionRow, mainCharacterPositionCol + 1) || isNotWall(mainCharacterPositionRow - 1, mainCharacterPositionCol) )){
                    mainCharacter.setCharacterRow(mainCharacterPositionRow - 1);
                    mainCharacter.setCharacterCol(mainCharacterPositionCol + 1);
                }
                break;
            case NUMPAD1:
                mainCharacter.setCharacterDirection("left");
                if(isNotWall(mainCharacterPositionRow + 1, mainCharacterPositionCol - 1) && (isNotWall(mainCharacterPositionRow, mainCharacterPositionCol - 1) || isNotWall(mainCharacterPositionRow + 1, mainCharacterPositionCol) )){
                    mainCharacter.setCharacterRow(mainCharacterPositionRow + 1);
                    mainCharacter.setCharacterCol(mainCharacterPositionCol - 1);
                }
                break;
            case NUMPAD3:
                mainCharacter.setCharacterDirection("right");
                if(isNotWall(mainCharacterPositionRow + 1, mainCharacterPositionCol + 1) && (isNotWall(mainCharacterPositionRow, mainCharacterPositionCol + 1) || isNotWall(mainCharacterPositionRow + 1, mainCharacterPositionCol) )){
                    mainCharacter.setCharacterRow(mainCharacterPositionRow + 1);
                    mainCharacter.setCharacterCol(mainCharacterPositionCol + 1);
                }
                break;

            default:
                break;
        }

        if(maze.getCharAt(mainCharacter.getCharacterRow(), mainCharacter.getCharacterCol()) == 'E')
            isAtTheEnd = true;

        setChanged();
        notifyObservers("Character");

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
                        toServer.close();
                        fromServer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });
            threadPool.execute(()->{
                clientSolveMaze.communicateWithServer();
                mazeSolutionArr = mazeSolution.getSolution();
                setChanged();
                notifyObservers("Solution");
            });
            //clientSolveMaze.communicateWithServer();
        }catch (Exception e){
            e.printStackTrace();
        }
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

    @Override
    public int getCharacterPositionRow() {
        return mainCharacter.getCharacterRow();
    }

    @Override
    public String getCharacterDirection() {
        return mainCharacter.getCharacterDirection();
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
    public int getCharacterPositionColumn() {
        return mainCharacter.getCharacterCol();
    }

    @Override
    public void closeModel() {
        //TODO implement

        System.out.println("Close Model");
        closeServers();
        threadPool.shutdown();
    }

    @Override
    public void saveOriginalMaze(File file){
        try {
            FileOutputStream fileWriter = null;
            fileWriter = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileWriter);
            objectOutputStream.writeObject(maze);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void saveCurrentMaze(File file){
        try {
            FileOutputStream fileWriter = null;
            fileWriter = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileWriter);
            Maze currentMaze = getCurrentMaze();
            objectOutputStream.writeObject(currentMaze);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private Maze getCurrentMaze() {
        try{
            Maze currentMaze = new Maze(maze,mainCharacter.getCharacterRow(),mainCharacter.getCharacterCol());
            return currentMaze;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    public void loadMaze(File file){
        try{
            FileInputStream fin = new FileInputStream(file);
            ObjectInputStream oin = new ObjectInputStream(fin);
            Maze loadedMaze = (Maze) oin.readObject();
            if(loadedMaze != null) {
                maze = loadedMaze;
                mainCharacter.setCharacterRow(maze.getStartPosition().getRowIndex());
                mainCharacter.setCharacterCol(maze.getStartPosition().getColumnIndex());
                mainCharacter.setCharacterDirection("front");
                setChanged();
                notifyObservers("Maze");
            }
            else{
                //TODO maybe add something here
            }
            fin.close();
            oin.close();
        }catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            //TODO-LoadMaze: add alert
            e.printStackTrace();
        }
    }



    public String getMainCharacterName(){
        return mainCharacterName;
    }



}
