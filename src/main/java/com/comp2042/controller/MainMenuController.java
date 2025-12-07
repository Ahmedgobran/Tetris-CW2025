package com.comp2042.controller;

import com.comp2042.util.AudioManager;
import com.comp2042.util.GameSettings;
import com.comp2042.util.HighScoreManager;
import com.comp2042.util.SceneLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controls the interaction logic for the Main Menu screen.
 * <p>
 * This controller serves as the central hub for the application, handling navigation to
 * Level Selection, Settings, Controls, and High Scores. It is responsible for initializing
 * and holding the core service dependencies (Audio, Settings, Scores) and passing them
 * to later screens.
 * </p>
 */
public class MainMenuController implements Initializable {

    @FXML private Button settingsButton;

    private Stage stage;

    // Dependencies
    private AudioManager audioManager;
    private GameSettings gameSettings;
    private HighScoreManager highScoreManager;

    /**
     * Called to initialize a controller after its root element has been completely processed.
     * <p>
     * Initializes UI state, such as enabling buttons that might be disabled by default.
     * Note: Core dependencies are not available here; they are injected via {@link #initModel}.
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
    }

    /**
     * Injects the required service dependencies and starts the background music.
     * <p>
     * This method must be called immediately after loading the controller to ensure
     * proper functioning of audio and state management.
     * </p>
     *
     * @param audioManager     The service responsible for playing music and SFX.
     * @param gameSettings     The global game configuration settings.
     * @param highScoreManager The manager handling high score persistence.
     */
    public void initModel(AudioManager audioManager, GameSettings gameSettings, HighScoreManager highScoreManager) {
        this.audioManager = audioManager;
        this.gameSettings = gameSettings;
        this.highScoreManager = highScoreManager;

        // Start music using the injected audio manager
        audioManager.playMusic("/music/background.mp3");
    }

    /**
     * Sets the primary stage for this controller.
     * <p>
     * The stage reference is required to perform scene transitions (e.g., changing from the menu
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
     * Plays a confirmation sound and navigates the user to the {@link LevelSelectionController},
     * passing along all core dependencies.
     * </p>
     */
    @FXML
    private void onPlayClicked() {
        audioManager.playPlayPress();
        try {
            // Updated call: pass highScoreManager
            SceneLoader.openLevelSelection(stage, audioManager, gameSettings, highScoreManager);
        } catch (Exception e) {
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
        audioManager.playButtonPress();
        try {
            // Controls screen doesn't need specific settings, just the stage
            SceneLoader.openControls(stage, audioManager);
        } catch (Exception e) {
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
        audioManager.playButtonPress();
        try {
            SceneLoader.openSettings(stage, gameSettings, audioManager);
        } catch (Exception e) {
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
        audioManager.playButtonPress();
        try {
            SceneLoader.openHighScores(stage, highScoreManager, audioManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Exit" button click event.
     * <p>
     * Plays a button sound, stops the music service, and terminates the application.
     * </p>
     */
    @FXML
    private void onExitClicked() {
        audioManager.playButtonPress();
        audioManager.stopMusic();
        stage.close();
        System.exit(0);
    }
}