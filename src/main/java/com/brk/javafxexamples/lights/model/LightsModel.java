
package com.brk.javafxexamples.lights.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.TreeMap;

/**
 * This class is created mostly for the purpose of demonstrating how to connect
 * a model to a controller class when using FXML for your view. Its primary purpose
 * is to do the work of keeping track of click counts for each {@link LightEnum} and
 * to provide the means for recording and playing back a sequence of clicked lights
 *
 * @author Prof. King
 */
public class LightsModel {
    /** Map of light to a counter of mouse clicks */
    private TreeMap<LightEnum, IntegerProperty> mapLightToClickCount;

    /** Ability to record and playback a sequence of light clicks */
    private LightSequence lightSequence;

    /** Current state of the LightsModel */
    private ObjectProperty<LightModelState> state;

    /**
     * General constructor to initialize all light counters to 0
     */
    public LightsModel() {
        this.mapLightToClickCount = new TreeMap<>();
        for (LightEnum light : LightEnum.values()) {
            this.mapLightToClickCount.put(light,new SimpleIntegerProperty(0));
        }
        this.lightSequence = new LightSequence();
        this.state = new SimpleObjectProperty<>(LightModelState.READY);
    }

    /**
     * @param light that you want the IntegerProperty for
     * @return the IntegerProperty
     */
    public IntegerProperty getClickCountProperty(LightEnum light) {
        return this.mapLightToClickCount.get(light);
    }
    public int getClickCount(LightEnum light) {
        return getClickCountProperty(light).get();
    }
    public void setMapLightToClickCount(LightEnum light, int newValue) {
        getClickCountProperty(light).setValue(newValue);
    }

    /** @return the {@link LightSequence} */
    public LightSequence getLightSequence() {
        return lightSequence;
    }

    /**
     * Set up the model to enable recording the sequence of lights clicked
     */
    public void startRecordingSequence() {
        // Turn off all lights before starting to record
        this.turnAllLights(false);
        this.lightSequence.clear();
        setState(LightModelState.RECORD_SEQUENCE);
    }

    /**
     * Stop the model from being able to record light clicks
     */
    public void stopRecordingSequence() {
        setState(LightModelState.READY);
    }

    /**
     * Start playing back a sequence. This needs to be placed on its own thread.
     */
    public void startPlaybackSequence() {
        if (this.lightSequence.getLength() > 0) {
            setState(LightModelState.PLAYBACK_SEQUENCE);

            // Set up a new Runnable returned from sequenceTask. The third parameter is
            // a simple lambda called at the end of the sequence. I could have hard coded it,
            // but that creates an additional dependency between LightModel and LightSequence
            Thread t = new Thread(lightSequence.sequenceTask(500,
                                                             500,
                                                             () -> setState(LightModelState.READY)));
            t.setDaemon(true);
            t.start();
        }
    }

    /**
     * Stop playing back
     */
    public void stopPlaybackSequence() {
        if (getState() == LightModelState.PLAYBACK_SEQUENCE) {
            setState(LightModelState.READY);
        }
    }


    // Some getters and setters for the LightModelState
    public LightModelState getState() { return state.get(); }
    public ObjectProperty<LightModelState> stateProperty() { return state; }
    public void setState(LightModelState state) { this.state.set(state); }

    /**
     * Helper method to switch all lights either on or off
     * @param isOn is true to turn the lights on, false otherwise
     */
    public void turnAllLights(boolean isOn) {
        for (LightEnum light : LightEnum.values()) {
            light.turnOn(isOn);
        }
    }

    /**
     * Helper method to turn individual lights on or off
     *
     * @param light the {@link LightEnum} to switch
     * @param isOn is true to turn on, false to turn off
     */
    public void turnOn(LightEnum light, boolean isOn) {
        System.out.println("turn: " + isOn + " " + light.getName());
        switch (this.getState()) {
            case RECORD_SEQUENCE -> {
                light.turnOn(isOn);
            }
        }
    }

    /**
     * Increment the value of the light click count by 1
     *
     * @param light is the {@link LightEnum} enum value we're incrementing
     */
    public void click(LightEnum light) {
        System.out.println("click: " + light.getName());
        switch (this.getState()) {
            // READY? Just toggle the light
            case READY -> light.turnOn(!light.isOn());

            // RECORDING? Then send the light clicked to the sequencer
            // In this case, the controller will handle turning the light on or off
            case RECORD_SEQUENCE -> {
                getLightSequence().addLight(light);
            }
        }

        // Regardless of state, let's keep track of total times light was clicked
        this.setMapLightToClickCount(light,
                                     this.getClickCount(light) + 1);
    }

}
