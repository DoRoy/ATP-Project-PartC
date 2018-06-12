package View;

import ViewModel.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
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

    //TODO add generate button to secondary menu

    //TODO- DONE..
    //Configurations: fix the string he expect regarding the algorithms

    //TODO - set background

    //TODO - DONE..
    // add shadow character

    @FXML
    private MyViewModel myViewModel;
    public MazeDisplayer mazeDisplayer;
    public Label lbl_characterRow;
    public Label lbl_characterColumn;
    public Label lbl_statusBar;

    public Label label_mainCharacterRow;
    public Label label_mainCharacterCol;
    public MenuItem save_MenuItem;
    public MenuItem solve_MenuItem;
    private String soundOnOff = "On";
    public javafx.scene.image.ImageView icon_sound;
    public javafx.scene.image.ImageView icon_partSolution;
    public javafx.scene.image.ImageView icon_fullSolution;
    public javafx.scene.image.ImageView icon_makeNewMaze;


    //region String Property for Binding
    public StringProperty CharacterRow = new SimpleStringProperty();
    public StringProperty CharacterColumn = new SimpleStringProperty();



    public void setViewModel(MyViewModel myViewModel){
        this.myViewModel = myViewModel;
    }
    @Override
    public void update(Observable o, Object arg) {
        if(o == myViewModel) {
            if(arg != null){
                String argument = (String)arg;
                switch (argument){
                    case "Maze":
                        mazeDisplayer.setMaze(myViewModel.getMaze());
                        //MainCharacter
                        mazeDisplayer.setMainCharacterPosition(myViewModel.getMainCharacterPositionRow(), myViewModel.getMainCharacterPositionColumn());
                        mazeDisplayer.setMainCharacterDirection(myViewModel.getMainCharacterDirection());
                        mazeDisplayer.setMainCharacterName(myViewModel.getMainCharacterName());

                        //Second Character
                        mazeDisplayer.setSecondCharacterName(myViewModel.getSecondCharacterName());
                        mazeDisplayer.setSecondCharacterPosition(myViewModel.getSecondCharacterPositionRow(), myViewModel.getSecondCharacterPositionColumn());
                        mazeDisplayer.setSecondCharacterDirection(myViewModel.getSecondCharacterDirection());

                        mazeDisplayer.setMazeSolutionArr(null);
                        Platform.runLater(()->{
                            CharacterColumn.set(myViewModel.getMainCharacterPositionColumn() + "");
                            CharacterRow.set(myViewModel.getMainCharacterPositionRow() + "");
                            label_mainCharacterRow.setText(myViewModel.getMainCharacterName()+"Row");
                            label_mainCharacterCol.setText(myViewModel.getMainCharacterName()+"Col");
                            lbl_statusBar.setText("Lets see you solve this!");
                            solve_MenuItem.setDisable(false);
                            icon_fullSolution.setVisible(true);
                        });


                        break;
                    case "Character":
                        mazeDisplayer.setMainCharacterPosition(myViewModel.getMainCharacterPositionRow(), myViewModel.getMainCharacterPositionColumn());
                        mazeDisplayer.setSecondCharacterPosition(myViewModel.getSecondCharacterPositionRow(), myViewModel.getSecondCharacterPositionColumn());
                        mazeDisplayer.setMainCharacterDirection(myViewModel.getMainCharacterDirection());
                        mazeDisplayer.setSecondCharacterDirection(myViewModel.getSecondCharacterDirection());
                        mazeDisplayer.setMazeSolutionArr(null);
                        Platform.runLater(() -> {
                            CharacterColumn.set(myViewModel.getMainCharacterPositionColumn() + "");
                            CharacterRow.set(myViewModel.getMainCharacterPositionRow() + "");
                            label_mainCharacterRow.setText(myViewModel.getMainCharacterName()+"Row");
                            label_mainCharacterCol.setText(myViewModel.getMainCharacterName()+"Col");
                        });
                        break;
                    case "Solution":
                        mazeDisplayer.setMazeSolutionArr(myViewModel.getSolution());
                        Platform.runLater(() -> {
                            lbl_statusBar.setText("Here's the solution");
                            solve_MenuItem.setDisable(false);
                            icon_fullSolution.setVisible(true);
                        });

                        break;
                }
            }


            if(myViewModel.isAtTheEnd()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(String.format("Congratulations!!"));
                Platform.runLater(() -> {
                    save_MenuItem.setDisable(true);
                    solve_MenuItem.setDisable(true);
                    icon_fullSolution.setVisible(false);
                    lbl_statusBar.setText("Good Job! Try a different maze");
                    alert.show();

                    // TODO - add a popup of newMaze()
                });

            }
            mazeDisplayer.redraw();
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
        //setHint();

        file = new File("Resources/Icons/icon_fullSolution.png");
        image = new Image(file.toURI().toString());
        icon_fullSolution.setImage(image);
        setFullSolution();

        file = new File("Resources/Icons/icon_makeNewMaze.png");
        image = new Image(file.toURI().toString());
        icon_makeNewMaze.setImage(image);
        makeNewMaze();

    }

    public void KeyPressed(KeyEvent keyEvent){
        System.out.println("MyViewController: KeyPressed");
        if(!myViewModel.isAtTheEnd()){
            //TODO - DONE..
            // make it not able to move the character
            myViewModel.moveCharacter(keyEvent.getCode());
            mazeDisplayer.setMazeSolutionArr(null);
            lbl_statusBar.setText("");
        }
        keyEvent.consume();
    }

/*    public void generateMaze(){
        lbl_statusBar.setText("Generating maze, please wait.");
        int height = Integer.valueOf(txtfld_rowsNum.getText());
        int width = Integer.valueOf(txtfld_columnsNum.getText());
        mazeDisplayer.setMazeSolutionArr(null);
        myViewModel.generateMaze(height, width);
        save_MenuItem.setDisable(false);
        solve_MenuItem.setDisable(false);
        icon_fullSolution.setVisible(false);
    }*/

    public void solveMaze(Event event){
        Platform.runLater(() ->{
            lbl_statusBar.setText("Computing solution, please wait.");
            solve_MenuItem.setDisable(true);
            icon_fullSolution.setVisible(false);
        });

        myViewModel.generateSolution();


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
                myViewModel.saveCurrentMaze(file);
                lbl_statusBar.setText("Saved current maze");
            }
            else if(choose[0] == 2) {//Original
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

    public void newMaze(){
        System.out.println("New Maze");
        try{
            Stage stage = new Stage();
            stage.setTitle("New Maze Window");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("NewGame.fxml").openStream());
            Scene scene = new Scene(root,600,600);
            scene.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());
            stage.setScene(scene);
            NewGameController newGameController = fxmlLoader.getController();
            newGameController.setStage(stage);
            newGameController.setViewModel(myViewModel);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }catch (Exception e){

        }
    }

    public void onAction_Property(){
        try {
            Stage stage = new Stage();
            stage.setTitle("Properties");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Properties.fxml").openStream());
            Scene scene = new Scene(root,400,370);
            scene.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());
            stage.setScene(scene);
            PropertiesViewController propertiesViewController = fxmlLoader.getController();
            propertiesViewController.setStage(stage);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }catch (Exception e){

        }

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
        Scene scene = new Scene(root,500,550);
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
                solveMaze(event);
                event.consume();
            }
        });
    }

    public void makeNewMaze(){
        icon_makeNewMaze.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                newMaze();
                event.consume();
            }
        });
    }


    public void dragDetected(MouseEvent mouseEvent){
        System.out.println("dragDetected");

        /*
        double sceneX = mouseEvent.getSceneX();

        double sceneY = mouseEvent.getSceneY();


        System.out.println("\tsceneX: " + sceneX);
        System.out.println("\tsceneY: " + sceneY);
        double buttomX = myViewModel.getMainCharacterPositionColumn() * mazeDisplayer.getWidth() / Integer.valueOf(txtfld_columnsNum.getText()) ;
        double topX = myViewModel.getMainCharacterPositionColumn() * mazeDisplayer.getWidth() / Integer.valueOf(txtfld_columnsNum.getText()) + (mazeDisplayer.getWidth() / Integer.valueOf(txtfld_columnsNum.getText()));
        System.out.println(buttomX);
        System.out.println(topX);
        if(sceneX >= buttomX && sceneX <= topX){
            System.out.println("got the X right");
        }

        */


    }

    public void mouseReleased(MouseEvent mouseEvent){
        System.out.println("mouseReleased");
    }

    public void mouseDragOver(MouseEvent mouseEvent){
        System.out.println("mouseDragOver");
    }

    public void mouseDragExited(MouseEvent mouseEvent){
        System.out.println("mouseDragExited");
    }

    public void dragDropped(){
        System.out.println("dragDropped");
    }

    public void dragExited(){
        System.out.println("dragExited");
    }

    public void dragOver(){
        System.out.println("dragOver");
    }

}
