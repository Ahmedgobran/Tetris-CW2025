package com.comp2042.controller;

import com.comp2042.util.AudioManager;
import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * Controls the interactions for the "Controls" / Help screen.
 * <p>
 * This simple controller displays the game instructions and key bindings.
 * It handles the logic for playing button sounds via the injected {@link AudioManager}
 * and returning to the previous menu using a callback mechanism.
 * </p>
 */
public class ControlsController {

    private Stage stage;
    private Runnable onCloseCallback;
    private AudioManager audioManager; // Dependency

    /**
     * Initializes the controller with the required service dependencies.
     * <p>
     * This method is used to inject the {@link AudioManager} so that button
     * clicks on this screen can play the consistent UI sound effects.
     * </p>
     *
     * @param audioManager The service responsible for playing sound effects.
     */
    public void initModel(AudioManager audioManager) {
        this.audioManager = audioManager;
    }

    /**
     * Sets the primary stage for this controller.
     *
     * @param stage The application window.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets the callback to execute when the "Back" button is clicked.
     * <p>
     * This is typically used to navigate back to the Main Menu or the previous screen
     * without creating a hard dependency on a specific parent controller.
     * </p>
     *
     * @param callback The action to run (usually changing the scene back).
     */
    public void setOnCloseCallback(Runnable callback) {
        this.onCloseCallback = callback;
    }

    /**
     * Handles the "Back" button click event.
     * <p>
     * Plays a button press sound using the injected audio manager and executes
     * the close callback to return to the previous screen. If no callback is set,
     * it attempts to close the stage.
     * </p>
     */
    @FXML
    private void onBackClicked() {
        // Use instance variable
        if (audioManager != null) {
            audioManager.playButtonPress();
        }

        if (onCloseCallback != null) {
            onCloseCallback.run();
        } else if (stage != null) {
            stage.close();
        }
    }
}