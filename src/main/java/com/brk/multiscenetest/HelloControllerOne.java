package com.brk.multiscenetest;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * The controller class for {@code hello-scene-one.fxml} scene graph
 */
public class HelloControllerOne {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnNextScreen;

    @FXML
    private Label lblStatusBar;

    @FXML
    void initialize() {
        assert btnNextScreen != null : "fx:id=\"btnNextScreen\" was not injected: check your FXML file 'hello-scene-one.fxml'.";
        assert lblStatusBar != null : "fx:id=\"lblStatusBar\" was not injected: check your FXML file 'hello-scene-one.fxml'.";
    }

    /**
     * {@link ActionEvent} handler for when we're ready to change to the next
     * scene.
     *
     * @param event is the {@link ActionEvent} passed
     */
    @FXML
    void onBtnNextScreenAction(ActionEvent event) {
        // Just a quick indication that the button was clicked
        lblStatusBar.setText("Button clicked... changing to next scene...");

        // Get the Stage object of this button
        Stage stage = (Stage) btnNextScreen.getScene().getWindow();

        MainApplication.loadSceneOnStage(stage,FXMLScenes.SCENE_TWO);
    }


}
