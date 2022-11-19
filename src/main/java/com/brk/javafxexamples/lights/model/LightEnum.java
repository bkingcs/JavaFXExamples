
package com.brk.javafxexamples.lights.model;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * This class encapsulates an enumeration to manage representing different lights
 * and their state in the model a bit easier to manage
 */
public enum LightEnum {
    RED(Color.RED, "red"),
    BLUE(Color.DODGERBLUE, "blue"),
    GREEN(Color.LIMEGREEN, "green");
//    YELLOW(Color.YELLOW, "yellow");

    /** Is the button light on? */
    private BooleanProperty on;

    /** Internal use - {@link Color} used for on */
    private Color onColor;

    /** Internal use - {@link Color} used for off */
    private Color offColor;

    /** Name to use for this button */
    private StringProperty name;

    /** Property representing the current {@link Color} of the light */
    private ObjectProperty<Color> currentColor;

    /**
     * Construct a new Light. The color passed should be an
     * instance of {@link Color} representing the ON color state
     *
     * @param onColor the {@link Color} if the button is on
     * @param name a String name for this button
     */
    LightEnum(Color onColor, String name) {
        this.onColor = onColor;
        this.offColor = onColor.darker();
        this.name = new SimpleStringProperty(name);

        // Initialize the button properites to represent an "off" state
        this.on = new SimpleBooleanProperty(false);
        this.currentColor = new SimpleObjectProperty<>(this.offColor);
    }

    // Some getters for the current color property
    public Color getCurrentColor() {
        return currentColor.get();
    }
    public ObjectProperty<Color> currentColorProperty() {
        return currentColor;
    }

    // Some getters for the name property
    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }

    // Getters and setters for the on property
    public boolean isOn() {
        return on.get();
    }
    public BooleanProperty onProperty() {
        return on;
    }
    public Color getOnColor() {
        return onColor;
    }
    public Color getOffColor() {
        return offColor;
    }

    /**
     * Set the state of the button to be either on or off
     * @param setToOn is {@code true} if the button is on, {@code false} if off.
     */
    public void turnOn(boolean setToOn) {
        if (setToOn) {
            this.currentColor.setValue(onColor);
        }
        else {
            this.currentColor.setValue(offColor);
        }
        this.on.setValue(setToOn);
    }

    @Override
    public String toString() {
        return "ColorButton[" + name +
                "] on=" + this.isOn();
    }
}
