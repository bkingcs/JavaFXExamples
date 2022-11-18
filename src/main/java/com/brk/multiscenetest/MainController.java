package com.brk.multiscenetest;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * The controller class for {@code main-opening-scene.fxml} scene graph
 */
public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnAnimation;

    @FXML
    private Button btnGridPane;

    @FXML
    private Button btnLightsDemo;

    @FXML
    private Label lblStatusBar;

    @FXML
    void initialize() {
        assert btnAnimation != null : "fx:id=\"btnAnimation\" was not injected: check your FXML file 'main-opening-scene.fxml'.";
        assert btnGridPane != null : "fx:id=\"btnGridPane\" was not injected: check your FXML file 'main-opening-scene.fxml'.";
        assert btnLightsDemo != null : "fx:id=\"btnLightsDemo\" was not injected: check your FXML file " +
                "'main-opening-scene.fxml'.";
        assert lblStatusBar != null : "fx:id=\"lblStatusBar\" was not injected: check your FXML file 'main-opening-scene.fxml'.";
    }

    /**
     * {@link ActionEvent} handler for when we're ready to change to the next
     * scene.
     *
     * @param event is the {@link ActionEvent} passed
     */
    @FXML
    void onBtnNextSceneAction(ActionEvent event) {
        // Just a quick indication that the button was clicked
        lblStatusBar.setText("Button clicked... changing to next scene...");

        // Get the Stage object of this button
        Stage stage = (Stage) btnGridPane.getScene().getWindow();

        if (event.getSource() == btnGridPane)
            MainApplication.loadSceneOnStage(stage,FXMLScenes.GRIDPANE_EXAMPLE);
        else if (event.getSource() == btnAnimation)
            MainApplication.loadSceneOnStage(stage,FXMLScenes.ANIMATION_EXAMPLE);
        else if (event.getSource() == btnLightsDemo)
            MainApplication.loadSceneOnStage(stage,FXMLScenes.LIGHTS_DEMO);
    }

}
