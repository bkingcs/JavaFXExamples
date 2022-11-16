/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Fall 2022
 *
 * Name: YOUR NAME
 * Date: 11/16/22
 * Time: 2:44 PM
 *
 * Project: MultiSceneTest
 * Package: com.brk.multiscenetest.lights.model
 * Class: LightsModel
 * Description:
 *
 * ****************************************
 */

package com.brk.multiscenetest.lights.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.TreeMap;

/**
 * This class is created mostly for the purpose of demonstrating how to connect
 * a model to a controller class when using FXML for your view. Its primary purpose
 * is to do the work of keeping track of click counts for each {@link LightEnum}
 */
public class LightsModel {
    /** Map of light to a counter of mouse clicks */
    private TreeMap<LightEnum, IntegerProperty> mapLightToClickCount;

    /**
     * General constructor to initialize all light counters to 0
     */
    public LightsModel() {
        this.mapLightToClickCount = new TreeMap<>();
        for (LightEnum light : LightEnum.values()) {
            this.mapLightToClickCount.put(light,new SimpleIntegerProperty(0));
        }
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

    /**
     * Increment the value of the light click count by 1
     *
     * @param light is the {@link LightEnum} enum value we're incrementing
     */
    public void click(LightEnum light) {
        // Let's turn the light on
        light.turnOn(!light.isOn());

        // And count the click
        this.setMapLightToClickCount(light,
                                     this.getClickCount(light) + 1);
    }

}
