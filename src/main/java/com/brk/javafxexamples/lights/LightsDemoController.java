package com.brk.javafxexamples.lights;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import com.brk.javafxexamples.lights.model.LightEnum;
import com.brk.javafxexamples.lights.model.LightModelState;
import com.brk.javafxexamples.lights.model.LightsModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

/**
 * The controller that is tied to FXML view for the Light clicker demo
 */
public class LightsDemoController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnRecordSequence;

    @FXML
    private Button btnPlaybackSequence;

    @FXML
    private Label lblSequenceInfo;

    @FXML
    private Label lblLightName1;

    @FXML
    private Label lblLightName2;

    @FXML
    private Label lblLightName3;

    @FXML
    private Label lblStatusBar;

    @FXML
    private Label lblClickCounter1;

    @FXML
    private Label lblClickCounter2;

    @FXML
    private Label lblClickCounter3;

    @FXML
    private Circle light1;

    @FXML
    private Circle light2;

    @FXML
    private Circle light3;

    /** The model connected to this FXML view */
    private LightsModel theLightsModel;

    /**
     * A small, simple private nested class to help managing the labels
     */
    private class LightAndNameView {
        private Label lblName;
        private Circle circleLight;
        private Label lblClickCounter;
        private LightEnum lightEnumModel;

        public LightAndNameView(Label name, Circle light, Label clickCounter) {
            this.lblName = name;
            this.circleLight = light;
            this.lblClickCounter = clickCounter;
        }

        public LightEnum getLightEnumModel() { return lightEnumModel; }
        public void setLightEnumModel(LightEnum lightEnumModel) {this.lightEnumModel = lightEnumModel; }
    };

    private LinkedList<LightAndNameView> lightsListView;

    @FXML
    void initialize() {
        assert btnRecordSequence != null : "fx:id=\"btnRecordSequence\" was not injected: check your FXML file 'lights-demo.fxml'.";
        assert btnPlaybackSequence != null : "fx:id=\"btnPlaybackSequence\" was not injected: check your FXML file 'lights-demo.fxml'.";
        assert lblClickCounter1 != null : "fx:id=\"lblClickCounter1\" was not injected: check your FXML file 'lights-demo.fxml'.";
        assert lblClickCounter2 != null : "fx:id=\"lblClickCounter2\" was not injected: check your FXML file 'lights-demo.fxml'.";
        assert lblClickCounter3 != null : "fx:id=\"lblClickCounter3\" was not injected: check your FXML file 'lights-demo.fxml'.";
        assert lblLightName1 != null : "fx:id=\"lblLightName1\" was not injected: check your FXML file 'lights-demo.fxml'.";
        assert lblLightName2 != null : "fx:id=\"lblLightName2\" was not injected: check your FXML file 'lights-demo.fxml'.";
        assert lblLightName3 != null : "fx:id=\"lblLightName3\" was not injected: check your FXML file 'lights-demo.fxml'.";
        assert lblSequenceInfo != null : "fx:id=\"lblSequenceInfo\" was not injected: check your FXML file " +
                "'lights-demo.fxml'.";
        assert lblStatusBar != null : "fx:id=\"lblStatusBar\" was not injected: check your FXML file 'lights-demo.fxml'.";
        assert light1 != null : "fx:id=\"light1\" was not injected: check your FXML file 'lights-demo.fxml'.";
        assert light2 != null : "fx:id=\"light2\" was not injected: check your FXML file 'lights-demo.fxml'.";
        assert light3 != null : "fx:id=\"light3\" was not injected: check your FXML file 'lights-demo.fxml'.";

        // To make it easier to manage these lights, let's create a list of them that ties
        // together the light and their respective labels
        this.lightsListView = new LinkedList<>();
        lightsListView.add(new LightAndNameView(lblLightName1, light1, lblClickCounter1));
        lightsListView.add(new LightAndNameView(lblLightName2, light2, lblClickCounter2));
        lightsListView.add(new LightAndNameView(lblLightName3, light3, lblClickCounter3));

        System.out.println("LightsDemoController - initialize complete!");

    }

    /**
     * Connect the model to this controller. This is called by the main application class
     * after the FXML has been loaded.
     */
    public void setModel(LightsModel lightsModel) {
        System.out.println("LightsDemoController - setModel!");
        this.theLightsModel = lightsModel;

        // Let's properly associate each light in the view with its model
        for (int i = 0; i < lightsListView.size(); i++) {
            LightAndNameView lightView = lightsListView.get(i);
            LightEnum lightModel = LightEnum.values()[i];
            lightView.setLightEnumModel(lightModel);
        }

        // Now, it's safe to initialize the handlers
        initHandlers();
        initSequencingHandlers();
    }

    private void initHandlers() {
        // Just a little safety check...
        if (this.theLightsModel == null)
            throw new RuntimeException("ERROR! Did not call setModel on controller class!");

        // Now, we can easily go through and set up some bindings for our individual lights
        for (LightAndNameView lightView : lightsListView) {
            LightEnum lightModel = lightView.lightEnumModel;

            // Automatically connect the color of the light in the model to the view
            lightView.circleLight.fillProperty().bind(lightModel.currentColorProperty());

            // Connect the name...
            lightView.lblName.textProperty().bind(lightModel.nameProperty());

            // Connect the count for each button
            lightView.lblClickCounter.textProperty().bind(theLightsModel.getClickCountProperty(lightModel).asString());

            // Set up a click binding - notice that we simply need to call the click method in the model
            // and the binding above will handle changing the color
            lightView.circleLight.setOnMouseClicked(event -> {
                theLightsModel.click(lightView.lightEnumModel);
            });

            // The model might handle distinct mouse down and up separately from click...
            lightView.circleLight.setOnMousePressed(event -> {
                theLightsModel.turnOn(lightModel,true);
            });

            lightView.circleLight.setOnMouseReleased(event -> {
                theLightsModel.turnOn(lightModel,false);
            });

        }

        // Finally, let's set up a binding to automatically show the state of the lights model
        this.lblStatusBar.textProperty().bind(theLightsModel.stateProperty().asString("State: %s"));
    }

    public void initSequencingHandlers() {

        // Start and stop recording
        this.btnRecordSequence.setOnMouseClicked(event -> {
            if (theLightsModel.getState() == LightModelState.READY)
                theLightsModel.startRecordingSequence();
            else if (theLightsModel.getState() == LightModelState.RECORD_SEQUENCE)
                theLightsModel.stopRecordingSequence();
        });
        this.btnPlaybackSequence.setOnMouseClicked(event -> {
            if (theLightsModel.getState() == LightModelState.READY) {
                if (theLightsModel.getLightSequence().getLength() > 0)
                    theLightsModel.startPlaybackSequence();
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("No sequence recorded!");
                    alert.setContentText("Please record a sequence first.");
                    alert.showAndWait();
                }
            }
            else if (theLightsModel.getState() == LightModelState.PLAYBACK_SEQUENCE)
                theLightsModel.stopPlaybackSequence();
        });

        // Change the name of buttons depending on the state
        this.btnRecordSequence.textProperty().bind(
                Bindings.when(theLightsModel.stateProperty().isEqualTo(LightModelState.RECORD_SEQUENCE))
                        .then("Stop Recording")
                        .otherwise("Record Sequence"));

        // Disable the record button when we're playing back a sequence
        this.btnRecordSequence.disableProperty().bind(
                Bindings.when(theLightsModel.stateProperty().isEqualTo(LightModelState.PLAYBACK_SEQUENCE))
                        .then(true)
                        .otherwise(false));
        // Disable playback button when we're recording
        this.btnPlaybackSequence.disableProperty().bind(
                Bindings.when(theLightsModel.stateProperty().isEqualTo(LightModelState.RECORD_SEQUENCE))
                        .then(true)
                        .otherwise(false));

        // Change the label on the record button to switch between starting and stopping a sequence
        this.btnRecordSequence.textProperty().bind(
                Bindings.when(theLightsModel.stateProperty().isEqualTo(LightModelState.RECORD_SEQUENCE))
                        .then("Stop Recording")
                        .otherwise("Record Sequence"));
        // Change the label on the playback button to switch between starting and stopping
        this.btnPlaybackSequence.textProperty().bind(
                Bindings.when(theLightsModel.stateProperty().isEqualTo(LightModelState.PLAYBACK_SEQUENCE))
                        .then("Stop Playback")
                        .otherwise("Start Playback"));

        // Set up a binding to keep track of the length of the sequence
        this.lblSequenceInfo.textProperty().bind(
                Bindings.when(theLightsModel.stateProperty().isEqualTo(LightModelState.PLAYBACK_SEQUENCE))
                        .then(Bindings.concat("Playing ",
                                              theLightsModel.getLightSequence().currentPlayPositionProperty(),
                                              "/",
                                              theLightsModel.getLightSequence().lengthProperty()))
                        .otherwise(StringBinding.stringExpression(theLightsModel.getLightSequence().lengthProperty())));
    }

}
