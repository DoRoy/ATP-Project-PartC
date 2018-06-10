package View;

import ViewModel.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.*;

import java.io.*;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;

public class MyViewController implements IView, Observer, Initializable {

    @FXML
    private MyViewModel myViewModel;
    public MazeDisplayer mazeDisplayer;
    private NewGameController gameController;
    public javafx.scene.control.TextField txtfld_rowsNum;
    public javafx.scene.control.TextField txtfld_columnsNum;
    public Label lbl_characterRow;
    public Label lbl_characterColumn;
    public Label lbl_statusBar;
    private String soundOnOff;
    public javafx.scene.image.ImageView icon_sound;
    public javafx.scene.image.ImageView icon_partSolution;
    public javafx.scene.image.ImageView icon_fullSolution;
    //private static Stage tempStage;
    private Scene newGameScene;
    private Scene mainScene;




    //region String Property for Binding
    public StringProperty CharacterRow = new SimpleStringProperty();
    public StringProperty CharacterColumn = new SimpleStringProperty();



    public void setViewModel(MyViewModel myViewModel){
        this.myViewModel = myViewModel;
    }
    public void setNewGameScene(Scene scene){
        newGameScene = scene;
    }
    public void setMainScene(Scene scene){
        mainScene = scene;
    }
    @Override
    public void update(Observable o, Object arg) {
        if(o == myViewModel) {
            mazeDisplayer.setMaze(myViewModel.getMaze());
            mazeDisplayer.setMainCharacterPosition(myViewModel.getMainCharacterPositionRow(), myViewModel.getMainCharacterPositionColumn());
            mazeDisplayer.setMainCharacterDirection(myViewModel.getMainCharacterDirection());
            mazeDisplayer.setMainCharacterName(myViewModel.getMainCharacterName());
            mazeDisplayer.setSecondCharacterName(myViewModel.getSecondCharacterName());
            CharacterColumn.set(myViewModel.getMainCharacterPositionColumn() + "");
            CharacterRow.set(myViewModel.getMainCharacterPositionRow() + "");
            if(myViewModel.getMazeSolutionArr() != null)
                mazeDisplayer.setMazeSolutionArr(myViewModel.getSolution());
            else
                mazeDisplayer.setMazeSolutionArr(null);
            if(myViewModel.isAtTheEnd()){
                Alert alert = new Alert(Alert.AlertType.NONE);
                alert.setContentText(String.format("Congratulations!!"));
                alert.show();
            }
            mazeDisplayer.redraw();

            if (!myViewModel.isPlayed()){
                soundOnOff = "On";
                File file = new File("Resources/Icons/icon_sound" + soundOnOff + ".png");
                Image image = new Image(file.toURI().toString());
                icon_sound.setImage(image);
            }
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Set Binding for Properties
        lbl_characterRow.textProperty().bind(CharacterRow);
        lbl_characterColumn.textProperty().bind(CharacterColumn);
        initImages();
    }

    private void initImages(){
        // Set soundBtn
        soundOnOff = "On";
        File file = new File("Resources/Icons/icon_sound" + soundOnOff + ".png");
        Image image = new Image(file.toURI().toString());
        icon_sound.setImage(image);
        setSound();

        //Set Solution Buttons
        file = new File("Resources/Icons/icon_partSolution.png");
        image = new Image(file.toURI().toString());
        icon_partSolution.setImage(image);
        setHint();

        file = new File("Resources/Icons/icon_fullSolution.png");
        image = new Image(file.toURI().toString());
        icon_fullSolution.setImage(image);
        setFullSolution();
    }

    public void KeyPressed(KeyEvent keyEvent){
        System.out.println("MyViewController: KeyPressed");
        myViewModel.moveCharacter(keyEvent.getCode());
        mazeDisplayer.setMazeSolutionArr(null);
        lbl_statusBar.setText("");
        keyEvent.consume();
    }

    //public void generateMaze(){
    //    int height = Integer.valueOf(txtfld_rowsNum.getText());
    //    int width = Integer.valueOf(txtfld_columnsNum.getText());
    //    mazeDisplayer.setMazeSolutionArr(null);
    //    myViewModel.generateMaze(height, width);
    //    lbl_statusBar.setText("Lets see if you solve this!");
    //
    //}

    public void solveMaze(ActionEvent actionEvent){
        //TODO implement
        solveMaze();
    }

    private void solveMaze(){
        myViewModel.generateSolution();
        int[][] solutionArr = myViewModel.getSolution();
        mazeDisplayer.setMazeSolutionArr(solutionArr);
        lbl_statusBar.setText("Here's the solution");
    }

    public void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void setResizeEvent(Scene scene){
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mazeDisplayer.redraw();
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mazeDisplayer.redraw();
            }
        });
    }

    public void exitButton(){
        System.out.println("Exit button");
        exitCorrectly();
    }


    public void exitCorrectly(){
        System.out.println("Exit Correctly");
        Alert alert = new Alert(Alert.AlertType.NONE    );
        ButtonType leaveButton = new ButtonType("Leave", ButtonBar.ButtonData.NO);
        ButtonType stayButton = new ButtonType("Stay", ButtonBar.ButtonData.YES);
        alert.getButtonTypes().setAll(stayButton,leaveButton);
        alert.setContentText("Are you sure you want to exit??");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == leaveButton){
            System.out.println("LeaveButton was clicked");
            // ... user chose OK
            // Close program
            myViewModel.closeModel();
            Platform.exit();
        } else {
            // ... user chose CANCEL or closed the dialog
            System.out.println("StayButton was clicked");
            alert.close();
        }

    }


    public void saveFile(ActionEvent event){
        System.out.println("saveFile");
        int[] choose = {0};
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Save Maze");
        alert.setContentText("What maze do you want to save?");
        ButtonType okButton = new ButtonType("Current", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Original", ButtonBar.ButtonData.NO);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButton, noButton, cancelButton);
        alert.showAndWait().ifPresent(type -> {
            if (type == okButton) {//Current
                choose[0] = 1;
            } else if (type == noButton) {//Original
                choose[0] = 2;
            } else {//Cancel
                choose[0] = 0;
            }
        });
        if(choose[0] == 0) {
            lbl_statusBar.setText("Save was canceled");
            return;
        }
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Choose a directory to save the maze in");
        fileChooser.setInitialDirectory(new File("./Mazes/"));

        //Show save file dialog
        File file = fileChooser.showSaveDialog(new PopupWindow() {
        });

        if(file != null) {
            if(choose[0] == 1) {//Current
                myViewModel.saveOriginalMaze(file);
                lbl_statusBar.setText("Saved current maze");
            }
            else {//Original
                myViewModel.saveOriginalMaze(file);
                lbl_statusBar.setText("Saved original maze");
            }
        }

        event.consume();
    }



    public void loadFile(ActionEvent event){
        System.out.println("loadFile");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a maze to load");
        fileChooser.setInitialDirectory(new File("./Mazes/"));
        File file = fileChooser.showOpenDialog(new PopupWindow() {
        });
        if(file != null && file.exists() && !file.isDirectory()){
            myViewModel.loadFile(file);
            lbl_statusBar.setText("Loaded " + file.getName());
        }
        event.consume();
    }


    public void onAction_Property(){
        try {
            Stage stage = new Stage();
            stage.setTitle("Properties");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Properties.fxml").openStream());
            Scene scene = new Scene(root,400,350);
            stage.setScene(scene);
            PropertiesViewController propertiesViewController = fxmlLoader.getController();
            propertiesViewController.setStage(stage);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }catch (Exception e){

        }

    }



    public void newMaze(){
        try {
            Stage tempStage = new Stage();
            tempStage.setTitle("Create a new game");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("NewGame.fxml").openStream());
            Scene scene = new Scene(root, 600, 500);
            NewGameController gameController = fxmlLoader.getController();
            gameController.setViewModel(myViewModel);
            tempStage.setScene(scene);
            gameController.setStage(tempStage);
            //myViewModel.addObserver(gameController);
            //tempStage.setScene(newGameScene);
            //tempStage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            tempStage.show();
            //System.out.println(event.getEventType().toString());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeTempStage(){
    //    tempStage.close();
    }

    public void help()
    {
        Stage helpStage = new Stage();
        helpStage.setAlwaysOnTop(true);
        helpStage.setResizable(false);
        helpStage.setTitle("Help Window");

        Parent root=null;
        try
        {
            //change MyView.fxml to help.fxml after designed
            root = FXMLLoader.load(getClass().getResource("../View/help.fxml"));
        }
        catch(IOException e)
        {
            showAlert("Exception!");
        }
        helpStage.setTitle("Help");
        Scene scene = new Scene(root,405,480);
        scene.getStylesheets().add(getClass().getResource("viewStyle.css").toExternalForm());
        helpStage.setScene(scene);
        helpStage.initModality(Modality.APPLICATION_MODAL);
        helpStage.show();
    }


    public void About()
    {
        Stage aboutStage = new Stage();
        aboutStage.setAlwaysOnTop(true);
        aboutStage.setResizable(false);
        aboutStage.setTitle("About Window");

        Parent root=null;
        try
        {
            //change MyView.fxml to help.fxml after designed
            root = FXMLLoader.load(getClass().getResource("../View/About.fxml"));
        }
        catch(IOException e)
        {
            showAlert("Exception!");
        }
        aboutStage.setTitle("About");
        Scene scene = new Scene(root,700,400);
        scene.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());
        aboutStage.setScene(scene);
        aboutStage.initModality(Modality.APPLICATION_MODAL);
        aboutStage.show();
    }

    public void setSound(){
        icon_sound.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if(myViewModel.setSound())
                    soundOnOff = "On";
                else
                    soundOnOff = "Off";

                File file = new File("Resources/Icons/icon_sound" + soundOnOff + ".png");
                Image image = new Image(file.toURI().toString());
                icon_sound.setImage(image);
                event.consume();
            }
        });
    }

    public void setFullSolution(){
        icon_fullSolution.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                solveMaze();
                event.consume();
            }
        });
    }

    public void setHint(){
        icon_partSolution.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                lbl_statusBar.setText("Here's the Hint");
                event.consume();
            }
        });
    }


    public void moveCharacter(KeyEvent keyEvent) {
        KeyPressed(keyEvent);
    }
}
