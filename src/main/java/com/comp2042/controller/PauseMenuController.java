package com.comp2042.controller;

import com.comp2042.util.AudioManager;
import com.comp2042.util.GameSettings;
import com.comp2042.util.HighScoreManager;
import com.comp2042.util.SceneLoader;
import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * Controls the interaction logic for the in-game Pause Menu overlay.
 * <p>
 * This controller handles actions for resuming the game, restarting the current session,
 * adjusting settings, or returning to the main menu. It receives core services via
 * Dependency Injection to allow for smooth navigation between scenes.
 * </p>
 */
public class PauseMenuController {

    private Stage stage;
    private GuiController guiController;

    // Dependencies
    private AudioManager audioManager;
    private GameSettings gameSettings;
    private HighScoreManager highScoreManager;

    /**
     * Initializes the controller with the required model and service dependencies.
     * <p>
     * These dependencies are required to navigate to other screens (like Settings or Main Menu)
     * and to maintain consistent audio state.
     * </p>
     *
     * @param audio    The AudioManager instance for button sounds.
     * @param settings The global GameSettings instance.
     * @param scores   The HighScoreManager instance (passed back to Main Menu).
     */
    public void initModel(AudioManager audio, GameSettings settings, HighScoreManager scores) {
        this.audioManager = audio;
        this.gameSettings = settings;
        this.highScoreManager = scores;
    }

    /**
     * Sets the primary stage for this controller.
     * @param stage The application window.
     */
    public void setStage(Stage stage) { this.stage = stage; }

    /**
     * Sets the reference to the main game controller.
     * <p>
     * This is required to trigger game actions like "Resume" (close overlay) or "Restart"
     * directly on the running game loop.
     * </p>
     *
     * @param guiController The active game controller.
     */
    public void setGuiController(GuiController guiController) { this.guiController = guiController; }

    /**
     * Handles the "Resume" button click.
     * <p>
     * Closes the pause overlay and resumes the game loop.
     * </p>
     */
    @FXML private void onResumeClicked() {
        audioManager.playButtonPress();
        guiController.closePauseMenu();
    }

    /**
     * Handles the "Restart" button click.
     * <p>
     * Resets the current game session immediately and closes the pause overlay.
     * </p>
     */
    @FXML private void onRestartClicked() {
        audioManager.playButtonPress();
        guiController.newGame();
        guiController.closePauseMenu();
    }

    /**
     * Handles the "Settings" button click.
     * <p>
     * Opens the settings panel, passing along the current game settings and audio manager.
     * </p>
     */
    @FXML private void onSettingsClicked() {
        audioManager.playButtonPress();
        try {
            SceneLoader.openSettings(stage, gameSettings, audioManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Main Menu" button click.
     * <p>
     * Aborts the current game and returns to the main menu screen, passing back all core dependencies.
     * </p>
     */
    @FXML private void onMainMenuClicked() {
        audioManager.playButtonPress();
        try {
            SceneLoader.openMainMenu(stage, audioManager, gameSettings, highScoreManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}