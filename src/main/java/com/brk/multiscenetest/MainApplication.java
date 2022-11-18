package com.brk.multiscenetest;

import com.brk.multiscenetest.lights.LightsDemoController;
import com.brk.multiscenetest.lights.model.LightsModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * An enumeration to encapsulate the names of our fxml scene graph files
 */
enum FXMLScenes {
    SCENE_ONE("main-opening-scene.fxml"),
    GRIDPANE_EXAMPLE("gridpane-example.fxml"),
    ANIMATION_EXAMPLE("animation-demo.fxml"),
    LIGHTS_DEMO("lights-demo.fxml");

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
            // Get the root node of the scene graph
            Parent root = fxmlLoader.load();

            // Attach any possible model to the controller if necessary
            switch (fxmlScene) {
                case LIGHTS_DEMO -> {
                    LightsModel model = new LightsModel();
                    LightsDemoController controller = fxmlLoader.getController();
                    controller.setModel(model);
                }
            }

            // Create the scene for the scene graph
            Scene scene = new Scene(root);

            // Place it on the stage
            stage.setScene(scene);
        }
        catch (IOException e) {
            throw new RuntimeException("Could not load " + fxmlScene.getFileName());
        }
    }

}