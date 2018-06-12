package View;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HelpController implements Initializable {
    public javafx.scene.control.Label Characters;
    public javafx.scene.control.Label MenuBar;
    public javafx.scene.control.Label Shortcuts;
    public javafx.scene.control.Button OK;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Characters.setWrapText(true);// lets the label text to break row when it is in need.
        MenuBar.setWrapText(true);
        Shortcuts.setWrapText(true);
        Characters.setText("\nCharacters:\n\n" +
                            "Crash Bandicoot and Mask:\n" +
                            "   Bla bla.\n" +
                            "\n Ash ketchum:\n" +
                            "   Bla bla.");


        MenuBar.setText("\n" +
                "File - \n" +
                "   New: Opens up another window where you can specify your maze setting.\n" +
                "   Solve: Shows this maze solution on the board (until character moves).\n"+
                "   Save: You can either save this maze with the original startPosition or the current position.\n" +
                "   Load: Choose a maze from the previous saved mazes.\n" +
                "\nOptions -\n" +
                "   Lets you set properties regarding to the game such as: \n" +
                "   Maze generator algorithm\n" +
                "   Maze search algorithm\n" +
                "   How many threads used in the game\n" +
                "\nExit -\n" +
                "   Closes the application by clicking the *Leave* button\n" +
                "\nAbout -\n    Read about this Maze game \n");


        Shortcuts.setText("\n" +
                "Sound On/Off:\n" +
                "   App provides a suitable background song according to the chosen characters.\n" +
                "\nNew Maze:\n" +
                "   Opens up another window where you can specify your maze setting:\n" +
                "       * Choose your character.\n" +
                "       * Set the maze's size.\n"+
                "\nGet Hint:\n" +
                "   Allows you to see the next steps towards the end.\n" +
                "\nGet Solution:\n" +
                "   Shows this maze solution on the board (until character moves).");
    }
    public void close(){
        Stage s = (Stage)OK.getScene().getWindow();
        s.close();
    }
}