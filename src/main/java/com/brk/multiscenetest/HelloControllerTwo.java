package com.brk.multiscenetest;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * The controller class for {@code hello-scene-two.fxml}
 */
public class HelloControllerTwo {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnPrevScreen;

    @FXML
    private GridPane gridPane;

    @FXML
    private Label lblStatusBar;

    private TextField[][] textFields;

    @FXML
    void initialize() {
        assert btnPrevScreen != null : "fx:id=\"btnPrevScreen\" was not injected: check your FXML file 'hello-scene-two.fxml'.";
        assert gridPane != null : "fx:id=\"gridPane\" was not injected: check your FXML file 'hello-scene-two.fxml'.";
        assert lblStatusBar != null : "fx:id=\"lblStatusBar\" was not injected: check your FXML file 'hello-scene-two.fxml'.";

        // I could populate my GridPane with individual TextField controls in SceneBuilder, but opted
        // to just populate it here for simplicity
        this.textFields = new TextField[gridPane.getRowCount()][gridPane.getColumnCount()];
        for (int row = 0; row < gridPane.getRowCount(); row++) {
            for (int col = 0; col < gridPane.getColumnCount(); col++) {
                TextField textField = new TextField();
                this.textFields[row][col] = textField;
                textField.setMaxSize(1000,1000);
                textField.setPrefColumnCount(1);
                textField.setAlignment(Pos.CENTER);

                // Let's expand it to the max size possible to take up the entire gridPane cell
                GridPane.setFillHeight(textField,true);
                GridPane.setFillWidth(textField,true);

                // Need to use GridPane static methods to assist with proper placement and index
                // labeling for each Node placed in a GridPane.
                GridPane.setRowIndex(textField,row);
                GridPane.setColumnIndex(textField,col);

                // Finally, add it to the gridPane object
                gridPane.add(textField, col, row);

                // Set up our mouse clicked controller to demonstrate how to retrieve the row and column
                textField.setOnMouseClicked(this::textFieldMouseClickHandler);
            }
        }
    }

    /**
     * This demonstrates getting the row and column of the object clicked, assuming
     * you properly set the row and column index of the object!
     *
     * @param event the {@link MouseEvent} that triggered this handler
     */
    private void textFieldMouseClickHandler(MouseEvent event) {
        TextField te = (TextField) event.getSource();

        lblStatusBar.setText(String.format("Clicked box [%d,%d], Mouse button: %s",
                                           GridPane.getRowIndex(te),
                                           GridPane.getColumnIndex(te),
                                           event.getButton()));
    }

    /**
     * Demonstrate how to change {@link javafx.scene.Scene} objects using a single {@link Stage}
     * @param event is the {@link ActionEvent} when the user clicks on the button to go back
     *              to the previous scene
     */
    @FXML
    void onBtnPrevScreen(ActionEvent event) {
        lblStatusBar.setText("Button clicked...");

        // Get the Stage object of this button
        Stage stage = (Stage) btnPrevScreen.getScene().getWindow();

        // Use our helper method to switch the scene
        MainApplication.loadSceneOnStage(stage, FXMLScenes.SCENE_ONE);
    }

}
