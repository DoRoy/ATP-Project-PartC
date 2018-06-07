package Model;

import Client.*;
import IO.MyDecompressorInputStream;
import Server.*;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.*;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class MyModel extends Observable implements IModel {

    private int characterPositionRow;
    private int characterPositionCol;
    private String characterDirection = "front";
    private Maze maze;
    private boolean isAtTheEnd;
    private int[][] mazeSolutionArr;



    private Client clientMazeGenerator;
    private Client clientSolveMaze;

    public int[][] getMazeSolutionArr() {
        return mazeSolutionArr;
    }




    public MyModel(){
        Configurations.run(); // TODO should be removed
        isAtTheEnd = false;
        startClient();
    }



    private void startClient(){
        try {
            clientMazeGenerator = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{10, 10};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[mazeDimensions[0] * mazeDimensions[1] + 12 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        Maze maze = new Maze(decompressedMaze);
                        System.out.println("xxxxxxxxxxxxx");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            clientSolveMaze = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        BufferedReader fromServer = new BufferedReader(new InputStreamReader(inFromServer));
                        PrintWriter toServer = new PrintWriter(outToServer);

                        String message = "Client Message";
                        String serverResponse;
                        toServer.write(message + "\n");
                        toServer.flush();
                        serverResponse = fromServer.readLine();
                        System.out.println(String.format("Server response: %s", serverResponse));
                        toServer.flush();
                        fromServer.close();
                        toServer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void generateMaze(int row, int col) {
        IMazeGenerator mg = Configurations.getGenerators_mazeGenerator();
        maze = mg.generate(row, col);
        characterPositionRow = maze.getStartPosition().getRowIndex();
        characterPositionCol = maze.getStartPosition().getColumnIndex();
        isAtTheEnd = false;
        mazeSolutionArr = null;

        setChanged();
        notifyObservers();
    }

    @Override
    public void moveCharacter(KeyCode movement) {
        mazeSolutionArr = null;
        switch(movement){
            case UP:
            case NUMPAD8:
                characterDirection = "back";
                if(isNotWall(characterPositionRow - 1, characterPositionCol ))
                    characterPositionRow--;
                break;
            case DOWN:
            case NUMPAD2:
                characterDirection = "front";
                if(isNotWall(characterPositionRow + 1, characterPositionCol))
                    characterPositionRow++;
                break;
            case LEFT:
            case NUMPAD4:
                characterDirection = "left";
                if(isNotWall(characterPositionRow, characterPositionCol - 1))
                    characterPositionCol--;
                break;
            case RIGHT:
            case NUMPAD6:
                characterDirection = "right";
                if(isNotWall(characterPositionRow, characterPositionCol + 1))
                    characterPositionCol++;
                break;
            case NUMPAD7:
                characterDirection = "left";
                if(isNotWall(characterPositionRow - 1, characterPositionCol - 1) && (isNotWall(characterPositionRow, characterPositionCol - 1) || isNotWall(characterPositionRow - 1, characterPositionCol) )){
                    characterPositionRow--;
                    characterPositionCol--;
                }
                break;
            case NUMPAD9:
                characterDirection = "right";
                if(isNotWall(characterPositionRow - 1, characterPositionCol + 1) && (isNotWall(characterPositionRow, characterPositionCol + 1) || isNotWall(characterPositionRow - 1, characterPositionCol) )){
                    characterPositionRow--;
                    characterPositionCol++;
                }
                break;
            case NUMPAD1:
                characterDirection = "left";
                if(isNotWall(characterPositionRow + 1, characterPositionCol - 1) && (isNotWall(characterPositionRow, characterPositionCol - 1) || isNotWall(characterPositionRow + 1, characterPositionCol) )){
                    characterPositionRow++;
                    characterPositionCol--;
                }
                break;
            case NUMPAD3:
                characterDirection = "right";
                if(isNotWall(characterPositionRow + 1, characterPositionCol + 1) && (isNotWall(characterPositionRow, characterPositionCol + 1) || isNotWall(characterPositionRow + 1, characterPositionCol) )){
                    characterPositionRow++;
                    characterPositionCol++;
                }
                break;

            default:
                break;
        }

        if(maze.getCharAt(characterPositionRow, characterPositionCol) == 'E')
            isAtTheEnd = true;

        setChanged();
        notifyObservers();

    }

    @Override
    public void generateSolution() {
        getSolution();
        setChanged();
        notifyObservers();
    }

    private boolean isNotWall(int row, int col){
        char c = maze.getCharAt(row, col);
        return (c == 'S' || c == '0' || c == 'E');
    }

    @Override
    public char[][] getMaze() {
        //TODO Implement
        String mazeString = maze.toString();
        String[] mazeRowsArr = mazeString.split("\n");
        char[][] mazeCharArray = new char[mazeRowsArr.length][mazeRowsArr[0].length()];
        for (int i = 0; i < mazeRowsArr.length; i ++){
            for(int j = 0; j <mazeRowsArr[0].length(); j++){
                if (mazeRowsArr[i].charAt(j) == 'S' || mazeRowsArr[i].charAt(j) == '0')
                    mazeCharArray[i][j] = '0';
                else if(mazeRowsArr[i].charAt(j) == 'E')
                    mazeCharArray[i][j] = 'E';
                else
                    mazeCharArray[i][j] = '1';
            }
        }
        mazeCharArray[maze.getStartPosition().getRowIndex()][maze.getStartPosition().getColumnIndex()] = 'S';
        return mazeCharArray;
    }

    @Override
    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    @Override
    public String getCharacterDirection() {
        return characterDirection;
    }


    @Override
    public int[][] getSolution() {
        Maze currentMaze = null;
        char[][] mazeCharArr = getMaze();
        mazeCharArr[maze.getStartPosition().getRowIndex()][maze.getStartPosition().getColumnIndex()] = '0';
        mazeCharArr[characterPositionRow][characterPositionCol] = 'S';

        try {
            currentMaze = new Maze(mazeCharArr, new Position(characterPositionRow, characterPositionCol),maze.getGoalPosition());

            // TODO change this to server
            ISearchable searchableMaze = new SearchableMaze(currentMaze);
            Solution solution = Configurations.getAlgorithms_solveAlgorithm().solve(searchableMaze);
            ArrayList<AState> solutionList = solution.getSolutionPath();
            mazeSolutionArr = new int[solutionList.size()][2];
            for(int i = 0; i < solutionList.size(); i++){
                mazeSolutionArr[i][0] = ((MazeState)solutionList.get(i)).getPosition().getRowIndex();
                mazeSolutionArr[i][1] = ((MazeState)solutionList.get(i)).getPosition().getColumnIndex();
            }
            return mazeSolutionArr;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isAtTheEnd() {
        return isAtTheEnd;
    }

    @Override
    public int getCharacterPositionColumn() {
        return characterPositionCol;
    }

    @Override
    public void closeModel() {
        //TODO implement

    }

    @Override
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


}
