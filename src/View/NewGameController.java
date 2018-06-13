package View;

import Server.Configurations;
import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;


import java.io.File;
import java.net.URL;
import java.util.*;

public class NewGameController implements IView, Initializable {

    ArrayList<String> mainCharacterList = new ArrayList( Arrays.asList( new String[]{"Crash_", "Ash_"}));
    String[] secondCharacterList = {"Crash_Second_","Ash_Second_"};
    String mainCharacter = "Crash_";
    String secondCharacter = "Crash_Second_";
    @FXML
    public javafx.scene.image.ImageView newGame_mainCharacter_imageView;
    public TextField newGame_rowsInput;
    public TextField newGame_colsInput;
    public Button newGame_mainCharacter_prevBtn;
    public Button newGame_mainCharacter_nextBtn;
    public Button newGame_Button;
    public CheckBox newGame_multiPlayer_checkBox;

    private  MyViewModel myViewModel;
    private Stage stage;


    public void setStage(Stage stage){ this.stage = stage;}
    public void setViewModel(MyViewModel myViewModel){
        this.myViewModel = myViewModel;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("Resources/Characters/Crash_character.png");
        Image image = new Image(file.toURI().toString());
        newGame_mainCharacter_imageView.setImage(image);
    }

    public void startGame(ActionEvent event){
        System.out.println("NewGameController: startGame");
        int rows = Integer.valueOf(newGame_rowsInput.getText());
        int cols = Integer.valueOf(newGame_colsInput.getText());
        myViewModel.setMainCharacterName(mainCharacter);
        myViewModel.setSecondCharacterName(secondCharacter);
        myViewModel.generateMaze(rows, cols);
        myViewModel.startSoundTrack(mainCharacter);

        //myViewModel.setMultiPlayerMode(newGame_multiPlayer_checkBox.isSelected());
        event.consume();

        //stage.close();

    }

    public void getNextMainCharacter(){
        int curIndex = mainCharacterList.indexOf(mainCharacter);
        String nextCharacter = mainCharacterList.get((curIndex + 1) % mainCharacterList.size());
        File file = new File("Resources/Characters/" + nextCharacter + "character.png");
        Image image = new Image(file.toURI().toString());
        newGame_mainCharacter_imageView.setImage(image);
        mainCharacter = nextCharacter;
        secondCharacter = nextCharacter + "Second_";
        //secondCharacter = secondCharacterList[(curIndex + 1)% secondCharacterList.length];
    }

    public void getPrevMainCharacter(){
        int curIndex = mainCharacterList.indexOf(mainCharacter);
        String prevCharacter = "";
        if(curIndex == 0){
            curIndex = mainCharacterList.size() - 1;
            prevCharacter = mainCharacterList.get((curIndex) % mainCharacterList.size());
            //secondCharacter = secondCharacterList[(curIndex) % mainCharacterList.size()];
        }else {
            prevCharacter = mainCharacterList.get((curIndex - 1) % mainCharacterList.size());
            //secondCharacter = secondCharacterList[(curIndex - 1) % mainCharacterList.size()];
        }
        secondCharacter = prevCharacter + "Second_";


        File file = new File("Resources/Characters/" + prevCharacter + "character.png");
        Image image = new Image(file.toURI().toString());
        newGame_mainCharacter_imageView.setImage(image);
        mainCharacter = prevCharacter;
    }



}
