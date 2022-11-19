
package com.brk.javafxexamples.lights.model;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Callback;

import java.util.LinkedList;

/**
 * This simple class encapsulates the functionality required for managing a simple
 * sequence of lights, both for capturing it and for playing it back. The primary
 * reason for this class is to demonstrate how a {@link Runnable} can be generated
 * to be spawned on a separate thread to playback a sequence, and also to demonstrate
 * how it's critical to use the {@link Platform#runLater(Runnable)} method to ensure that
 * ANYTHING in a thread that triggers some sort of update on the GUI MUST be placed
 * on the JavaFX Application Thread.
 */
public class LightSequence {

    public static interface VoidFunction {
        void call();
    }

    /** The sequence will be managed with a simple List object */
    private LinkedList<LightEnum> lightSeq;

    /** The current length of the sequence recorded */
    private IntegerProperty length;

    /** When playing back, this is the current light being played */
    private IntegerProperty currentPlayPosition;

    public LightSequence() {
        this.lightSeq = new LinkedList<>();
        this.length = new SimpleIntegerProperty(0);
        this.currentPlayPosition = new SimpleIntegerProperty(0);
    }

    /**
     * Add another light to the sequence
     *
     * @param light the {@link LightEnum} to be added to the sequence
     */
    public void addLight(LightEnum light) {
        this.lightSeq.add(light);
        this.length.set(this.length.get()+1);
    }

    /**
     * Reset the sequence
     */
    public void clear() {
        this.lightSeq.clear();
        this.length.setValue(0);
        this.currentPlayPosition.setValue(0);
    }

    public int getLength() { return length.get(); }
    public IntegerProperty lengthProperty() { return length; }

    public int getCurrentPlayPosition() { return currentPlayPosition.get(); }
    public IntegerProperty currentPlayPositionProperty() { return currentPlayPosition; }

    /**
     * Return a {@link Runnable} object that is ready to be spawned on a {@link Thread}.
     *
     * @param msLightOnTime how long should a light stay on?
     * @param msDelayBetweenLights how long should we wait between lights?
     * @param onFinished - What should be called at the end of the sequence?
     * @return A runnable that is ready to play a sequence
     */
    public Runnable sequenceTask(long msLightOnTime, long msDelayBetweenLights, VoidFunction onFinished) {
        return new Runnable() {
            @Override
            public void run() {
                Platform.runLater(() -> currentPlayPosition.setValue(0));

                /////////////////////
                // IMPORTANT - Any property that propagates to change something in the JavaFX GUI
                //             must be placed in the JavaFX Application Thread! Use Platform.runLater()
                ////////////////////

                try {
                    for (LightEnum light : lightSeq) {
                        // Update the current position
                        Platform.runLater(() -> currentPlayPosition.setValue(currentPlayPosition.get()+1));

                        // Turn on the light
                        Platform.runLater(() -> light.turnOn(true));
                        Thread.sleep(msLightOnTime);

                        // ... and... turn it off
                        Platform.runLater(() -> light.turnOn(false));
                        Thread.sleep(msDelayBetweenLights);
                    }
                }
                catch (InterruptedException e) {
                    // Code reaches here if the user terminates the playback prematurely
                }
                finally {
                    Platform.runLater(() -> onFinished.call());
                }
            }
        };
    }

}
