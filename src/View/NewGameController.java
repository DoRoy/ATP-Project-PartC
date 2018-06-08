package View;

import ViewModel.MyViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

import javax.swing.text.html.ImageView;
import java.io.File;
import java.net.URL;
import java.util.*;

public class NewGameController implements IView, Observer, Initializable {

    ArrayList<String> characterList = new ArrayList( Arrays.asList( new String[]{"Crash_", "Mask_"} ) );
    String mainCharacter = "Crash_";

    @FXML
    public javafx.scene.image.ImageView newGame_mainCharacter_imageView;
    public Button newGame_mainCharacter_prevBtn;
    public Button newGame_mainCharacter_nextBtn;
    public Button newGame_Button;

    private  MyViewModel myViewModel;

    public void setViewModel(MyViewModel myViewModel){
        myViewModel = myViewModel;
    }


    @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("Resources/Characters/Crash_front.png");
        Image image = new Image(file.toURI().toString());
        newGame_mainCharacter_imageView.setImage(image);
    }

    public void startGame(){


    }

    public void getNextMainCharacter(){
        int curIndex = characterList.indexOf(mainCharacter);
        String nextCharacter = characterList.get((curIndex + 1) % characterList.size());
        File file = new File("Resources/Characters/" + nextCharacter + "front.png");
        Image image = new Image(file.toURI().toString());
        newGame_mainCharacter_imageView.setImage(image);
        mainCharacter = nextCharacter;
    }

    public void getPrevMainCharacter(){
        int curIndex = characterList.indexOf(mainCharacter);
        if(curIndex == 0)
            curIndex = characterList.size();
        String prevCharacter = characterList.get((curIndex - 1) % characterList.size());
        File file = new File("Resources/Characters/" + prevCharacter + "front.png");
        Image image = new Image(file.toURI().toString());
        newGame_mainCharacter_imageView.setImage(image);
        mainCharacter = prevCharacter;


    }


}
