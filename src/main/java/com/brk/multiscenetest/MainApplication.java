package com.brk.multiscenetest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * An enumeration to encapsulate the names of our fxml scene graph files
 */
enum FXMLScenes {
    SCENE_ONE("hello-scene-one.fxml"),
    SCENE_TWO("hello-scene-two.fxml");

    private final String fileName;

    FXMLScenes(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}

/**
 * Just another standard {@link Application} class to fire up a new {@link Stage} and place
 * a {@link Scene} on it
 */
public class MainApplication extends Application {
    @Override
    public void start(Stage stage) {
        loadSceneOnStage(stage,FXMLScenes.SCENE_ONE);
        stage.setTitle("Hello!");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * This is a general static helper method that loads FXML files specified by
     * the{@link FXMLScenes} enumeration and places it on the {@link Stage}
     *
     * @param stage the {@link Stage} to place the Scene on
     * @param fxmlScene the {@link FXMLScenes} enum that speciies the {@code .fxml} file to load
     */
    public static void loadSceneOnStage(Stage stage, FXMLScenes fxmlScene) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxmlScene.getFileName()));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
        }
        catch (IOException e) {
            throw new RuntimeException("Could not load " + fxmlScene.getFileName());
        }
    }

}