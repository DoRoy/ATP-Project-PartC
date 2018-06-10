package View;

import Server.Configurations;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.security.auth.login.Configuration;
import java.net.URL;
import java.util.ResourceBundle;

public class PropertiesViewController extends Dialog implements Initializable {

    private String algorithmString;
    private String generatorString;
    private int threadNumString;
     public ChoiceBox algorithmChoiceBox;
     public ChoiceBox mazeGeneratorChoiceBox;
     public Spinner spinner;
     public SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
    private Stage stage;


    public void saveChanges(){
         System.out.println("Properties: saveChanges");
         algorithmString = (String)algorithmChoiceBox.getValue();
         generatorString = (String)mazeGeneratorChoiceBox.getValue();
        System.out.println(generatorString);
        System.out.println(algorithmString);
        System.out.println(spinner.getValue());
        Configurations.setProperties((Integer)spinner.getValue(),(String)algorithmChoiceBox.getValue(), (String)mazeGeneratorChoiceBox.getValue());
        stage.close();
     }

     public void setStage(Stage stage){
         this.stage = stage;
     }


    public void closeButton(){
        System.out.println("Properties: closeButton");
        stage.close();
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        spinner.setValueFactory(valueFactory);
        algorithmChoiceBox.getItems().addAll("BestFirstSearch","DepthFirstSearch","BreadthFirstSearch");
        mazeGeneratorChoiceBox.getItems().addAll("MyMazeGenerator","SimpleMazeGenerator");


    }
}
