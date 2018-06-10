package View;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HelpController implements Initializable {
    public javafx.scene.control.Label Info_About_The_Game;
    public javafx.scene.control.Label buttons_instruction;
    public javafx.scene.control.Label Load_Save_Game;
    public javafx.scene.control.Button OK;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Info_About_The_Game.setWrapText(true);// lets the label text to break row when it is in need.
        buttons_instruction.setWrapText(true);
        Load_Save_Game.setWrapText(true);
        Info_About_The_Game.setText("\nCrash Maze Game\n\n");
        buttons_instruction.setText("\nMenu Bar-\n" +
                "The menu bar of the game includes some options:\n" +
                "File- \n" +
                "New: let you to open the setting in the left while you are playing the game and you can fill the row and col test fields to create new maze\n" +
                "Save: let you to save your current maze (see more explanations in the instruction of save and load game).\n" +
                "Load: let you load a game you saved (see more explanations in the instruction of save and load game).\n" +
                "Options- let you to open the properties setting to decide which maze generator algorithm, which search algorithm or how much threads you want to use in your game\n" +
                "Exit- to exit the game application\n" +
                "\n" +
                "Left side setting-\n" +
                "The left side setting includes some fields that help you to play. Once you want to play you need to generate new maze or open a saved maze and then click the Start button to start playing. When you want to stop playing or open again the left side setting you need to click pause button or clear the maze and then you can see the solve of the maze or reset to start from the beginning or fill new size of maze and generate a new one.\n");
        Load_Save_Game.setText("\nSave-\n" +
                "After you have a maze at the application and you wish to save it, you need to press the “File” option at the menu bar and press “Save”.\n" +
                "Now you are requested to choose a name for the maze , so you can load it when you wish to.\n" +
                "If you decide that you don’t want to save the maze you can press “back” and go back to the maze.\n" +
                "Once you have picked your name, you need to press the “save” button, and you will see an alert that confirm your save.\n" +
                "\n" +
                "Load-\n" +
                "After you have successfully saved your maze, you can press “File” at the menu bar and press “Load”.\n" +
                "A File-chooser will appear and you will be requested to pick a file. After you choose one, press ok. Shortly, your chosen maze will load.");
    }
    public void close(){
        Stage s = (Stage)OK.getScene().getWindow();
        s.close();
    }
}