package com.comp2042.controller;

import com.comp2042.util.AudioManager;
import com.comp2042.util.SceneLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controls the interaction logic for the in-game Pause Menu.
 * <p>
 * Handles actions for resuming the game, restarting, accessing settings,
 * or returning to the main menu.
 * </p>
 */
public class PauseMenuController implements Initializable {

    private Stage stage;
    private GuiController guiController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization logic if needed
    }

    /**
     * Sets the primary stage.
     * @param stage The application window.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets the reference to the main game controller.
     * Needed to trigger resume/restart actions on the game loop.
     *
     * @param guiController The active game controller.
     */
    public void setGuiController(GuiController guiController) {
        this.guiController = guiController;
    }

    /**
     * Handles the "Resume" button click.
     * Closes the pause overlay and resumes the game loop.
     */
    @FXML
    private void onResumeClicked() {
        AudioManager.getInstance().playButtonPress();
        // Instead of switching scenes, we just close the overlay
        guiController.closePauseMenu();
    }

    /**
     * Handles the "Restart" button click.
     * Resets the current game session immediately.
     */
    @FXML
    private void onRestartClicked() {
        AudioManager.getInstance().playButtonPress();
        // Start new game
        guiController.newGame();
        // Close the overlay
        guiController.closePauseMenu();
    }

    /**
     * Handles the "Settings" button click.
     * Opens the settings panel.
     */
    @FXML
    private void onSettingsClicked() {
        AudioManager.getInstance().playButtonPress();
        try {
            // This still needs to switch scenes so we levae as is
            SceneLoader.openSettings(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Main Menu" button click.
     * Aborts the current game and returns to the main menu screen.
     */
    @FXML
    private void onMainMenuClicked() {
        AudioManager.getInstance().playButtonPress();
        try {
            // This still needs to switch scenes so we levae as is
            SceneLoader.openMainMenu(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}