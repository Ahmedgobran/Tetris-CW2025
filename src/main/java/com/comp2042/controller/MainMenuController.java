package com.comp2042.controller;

import com.comp2042.util.AudioManager;
import com.comp2042.util.GameSettings;
import com.comp2042.util.SceneLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controls the interaction logic for the Main Menu screen.
 * <p>
 * This controller handles navigation to various parts of the application, including
 * the Level Selection, Settings, Controls, and High Scores screens. It also initializes
 * the global audio settings when the application starts.
 * </p>
 */
public class MainMenuController implements Initializable {

    @FXML private Button settingsButton;

    private Stage stage;

    /**
     * Initializes the controller class.
     * <p>
     * This method runs automatically after the FXML file has been loaded. It ensures
     * UI elements are in the correct state (e.g., hiding the controls panel) and applies
     * saved audio preferences to the {@link AudioManager} before starting background music.
     * </p>
     *
     * @param location  The location used to resolve relative paths for the root object, or null if unknown.
     * @param resources The resources used to localize the root object, or null if not found.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Enable settings button (was disabled)
        if (settingsButton != null) {
            settingsButton.setDisable(false);
        }

        // Start background music with saved volume settings
        GameSettings settings = GameSettings.getInstance();
        AudioManager audioManager = AudioManager.getInstance();

        // Apply saved settings to AudioManager
        audioManager.setMusicVolume(settings.getMusicVolume());
        audioManager.setSfxVolume(settings.getSfxVolume());
        audioManager.setMusicEnabled(settings.isMusicEnabled());
        audioManager.setSfxEnabled(settings.isSfxEnabled());
        audioManager.playMusic("/music/background.mp3");
    }

    /**
     * Sets the primary stage for this controller.
     * <p>
     * The stage reference is required to switch scenes (e.g., changing from the menu
     * to the game view).
     * </p>
     *
     * @param stage The primary application window.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Handles the "Play" button click event.
     * <p>
     * Plays a sound effect and navigates the user to the {@link LevelSelectionController}
     * to choose a game mode.
     * </p>
     */
    @FXML
    private void onPlayClicked() {
        AudioManager.getInstance().playPlayPress();
        try {
            SceneLoader.openLevelSelection(stage);
        } catch (Exception e) {
            System.err.println("Error opening level selection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Controls" button click event.
     * <p>
     * Navigates to the Controls/Instructions screen where key bindings are displayed.
     * </p>
     */
    @FXML
    private void onControlsClicked() {
        AudioManager.getInstance().playButtonPress();
        try {
            // Use the new scene loader we added in SceneLoader to open the controls screen
            SceneLoader.openControls(stage);
        } catch (Exception e) {
            System.err.println("Error opening controls: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Settings" button click event.
     * <p>
     * Navigates to the Settings configuration screen where users can adjust volume
     * and gameplay preferences.
     * </p>
     */
    @FXML
    private void onSettingsClicked() {
        AudioManager.getInstance().playButtonPress();
        try {
            SceneLoader.openSettings(stage);
        } catch (Exception e) {
            System.err.println("Error opening settings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "High Scores" button click event.
     * <p>
     * Navigates to the High Scores display screen to show top player records.
     * </p>
     */
    @FXML
    private void onHighScoresClicked() {
        AudioManager.getInstance().playButtonPress();
        try {
            SceneLoader.openHighScores(stage);
        } catch (Exception e) {
            System.err.println("Error opening high scores: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Exit" button click event.
     * <p>
     * Plays a button sound, stops any playing music, closes the application window,
     * and terminates the Java Virtual Machine.
     * </p>
     */
    @FXML
    private void onExitClicked() {
        AudioManager.getInstance().playButtonPress();
        AudioManager.getInstance().stopMusic();
        // Close the application
        stage.close();
        System.exit(0);
    }

}