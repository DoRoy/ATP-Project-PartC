package View;

import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class HelpController implements Initializable {
    public javafx.scene.control.Label Characters;
    public javafx.scene.control.Label MenuBar;
    public javafx.scene.control.Button OK;
    public javafx.scene.image.ImageView soundImage;
    public javafx.scene.image.ImageView newMazeImage;
    public javafx.scene.image.ImageView getHintImage;
    public javafx.scene.image.ImageView solveImage;
    public javafx.scene.control.Label soundLabel;
    public javafx.scene.control.Label newMazeLabel;
    public javafx.scene.control.Label getHintLabel;
    public javafx.scene.control.Label solveLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Characters.setWrapText(true);// lets the label text to break row when it is in need.
        MenuBar.setWrapText(true);
        soundLabel.setWrapText(true);
        newMazeLabel.setWrapText(true);
        getHintLabel.setWrapText(true);
        solveLabel.setWrapText(true);

        Characters.setText("\nCharacters:\n\n" +
                            "Crash Bandicoot and Mask:\n" +
                            "   Bla bla.\n" +
                            "\n Ash ketchum:\n" +
                            "   Bla bla.");


        MenuBar.setText("\n" +
                "File - \n" +
                "   New: Opens up another window where you can specify your maze setting.\n" +
                "   Solve: Shows this maze solution on the board (until character moves).\n"+
                "   Save: You can either save this maze with the original or the current position.\n" +
                "   Load: Choose a maze from the previous saved mazes.\n" +
                "\nOptions -\n" +
                "   Lets you set properties regarding to the game such as: \n" +
                "   Maze generator algorithm \n" +
                "   Maze search algorithm - where you can choose between:\n" +
                "       BestFirstSearch\n" +
                "       DepthFirstSearch\n" +
                "       BreadthFirstSearch\n" +
                "   How many threads used in the game.\n" +
                "\nExit -\n" +
                "   Closes the application by clicking the *Leave* button\n" +
                "\nAbout -\n    Read about this Maze game and it's creators.\n\n" +
                "Beneath the maze you can see the current location on your character.\n" +
                "The Status Bar will help you understand what's going on in the game.");


        soundLabel.setText("\n" +
                "Sound On/Off:\n" +
                "   App provides a suitable background song according\n to the chosen characters.");

        newMazeLabel.setText("\nNew Maze:\n" +
                "   Opens up another window where you can:\n" +
                "       * Choose your character.\n" +
                "       * Set the maze's size.");

        getHintLabel.setText("\nGet Hint:\n" +
                "   Allows you to see the next steps towards the end." );

        solveLabel.setText("\nGet Solution:\n" +
                "   Shows this maze solution on the board (until character moves).");
        initImages();
    }


    private void initImages(){
        // Set soundBtn

        File file = new File("Resources/Icons/icon_sound" + "On" + ".png");
        Image image = new Image(file.toURI().toString());
        soundImage.setImage(image);


        //Set Solution Buttons
        file = new File("Resources/Icons/icon_partSolution.png");
        image = new Image(file.toURI().toString());
        getHintImage.setImage(image);
        //setHint();

        file = new File("Resources/Icons/icon_fullSolution.png");
        image = new Image(file.toURI().toString());
        solveImage.setImage(image);


        file = new File("Resources/Icons/icon_makeNewMaze.png");
        image = new Image(file.toURI().toString());
        newMazeImage.setImage(image);


    }

    public void close(){
        Stage s = (Stage)OK.getScene().getWindow();
        s.close();
    }
}