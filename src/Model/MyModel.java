package Model;

import Server.*;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.*;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class MyModel extends Observable implements IModel {

    private int characterPositionRow;
    private int characterPositionCol;
    private String characterDirection = "front";
    private Maze maze;
    private boolean isAtTheEnd;
    int[][] mazeSolutionArr;

    public int[][] getMazeSolutionArr() {
        return mazeSolutionArr;
    }




    public MyModel(){
        Configurations.run(); // TODO should be removed
        isAtTheEnd = false;
    }

    private void startServers(){
        Server serverMazeGenerator = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        Server serverSolveMaze = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        serverMazeGenerator.start();
        serverSolveMaze.start();
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


}
