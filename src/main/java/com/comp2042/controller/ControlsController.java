package com.comp2042.controller;

import com.comp2042.util.AudioManager;
import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * Controls the interactions for the "Controls" / Help screen.
 * <p>
 * Primarily handles the logic for closing the screen and returning to the previous menu.
 * </p>
 */
public class ControlsController {

    private Stage stage;
    private Runnable onCloseCallback;

    /**
     * Sets the primary stage.
     * @param stage The application window.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets the callback to execute when the back button is clicked.
     * @param callback The action to run (usually changing the scene back).
     */
    public void setOnCloseCallback(Runnable callback) {
        this.onCloseCallback = callback;
    }

    /**
     * Handles the "Back" button click.
     * Executes the callback or closes the stage if no callback is set.
     */
    @FXML
    private void onBackClicked() {
        AudioManager.getInstance().playButtonPress();
        if (onCloseCallback != null) {
            onCloseCallback.run();
        } else if (stage != null) {
            stage.close();
        }
    }
}