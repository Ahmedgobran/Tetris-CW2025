package com.comp2042.controller;

import com.comp2042.model.NormalModeController;
import com.comp2042.model.ChallengeModeController;
import com.comp2042.util.AudioManager;
import com.comp2042.util.GameSettings;
import com.comp2042.util.HighScoreManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controls the interactions for the Level Selection screen.
 * <p>
 * This controller allows the user to choose between different game modes (Normal vs Challenge).
 * It is responsible for initializing the game environment, loading the game layout, and
 * injecting the necessary dependencies into the specific {@link com.comp2042.model.AbstractGameController}
 * subclass for the chosen mode.
 * </p>
 */
public class LevelSelectionController implements Initializable {

    private Stage stage;
    private Runnable onBackCallback;

    // Core Dependencies injected from Main Menu
    private AudioManager audioManager;
    private GameSettings gameSettings;
    private HighScoreManager highScoreManager;

    private static final int WINDOW_WIDTH = 610;
    private static final int WINDOW_HEIGHT = 515;

    /**
     * Standard FXML initialization hook.
     * <p>
     * Logic is deferred to {@link #initModel} as this controller relies on external dependencies.
     * </p>
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    /**
     * Initializes the controller with the required service dependencies.
     * <p>
     * These services are essential for the game loop and must be passed forward to the
     * {@link GuiController} when a game starts.
     * </p>
     *
     * @param audio      The AudioManager for game sounds and music.
     * @param settings   The GameSettings for configuration.
     * @param highScores The HighScoreManager for saving game results.
     */
    public void initModel(AudioManager audio, GameSettings settings, HighScoreManager highScores) {
        this.audioManager = audio;
        this.gameSettings = settings;
        this.highScoreManager = highScores;
    }

    /**
     * Sets the primary stage for scene transitions.
     * @param stage The application window.
     */
    public void setStage(Stage stage) { this.stage = stage; }

    /**
     * Registers a callback to execute when the "Back" button is clicked.
     * @param callback The action to run (usually returning to the Main Menu).
     */
    public void setOnBackCallback(Runnable callback) { this.onBackCallback = callback; }

    /**
     * Handles the "Normal Mode" selection event.
     * <p>
     * Starts a standard Tetris game with default scoring and gravity rules.
     * </p>
     */
    @FXML
    private void onNormalClicked() {
        audioManager.playPlayPress();
        try {
            startGame(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Challenge Mode" selection event.
     * <p>
     * Starts a game with special rules (e.g., Invisible Blocks, Score Multipliers).
     * </p>
     */
    @FXML
    private void onSpecialClicked() {
        audioManager.playPlayPress();
        try {
            startGame(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Back" button click event.
     * <p>
     * Executes the navigation callback to return to the previous screen.
     * </p>
     */
    @FXML
    private void onBackClicked() {
        audioManager.playButtonPress();
        if (onBackCallback != null) onBackCallback.run();
        else if (stage != null) stage.close();
    }

    /**
     * Helper method to initialize and launch the game scene.
     * <p>
     * Loads the common game layout, injects all dependencies into the {@link GuiController},
     * and instantiates the specific Game Controller logic based on the selected mode.
     * </p>
     *
     * @param isChallenge true to start Challenge Mode, false for Normal Mode.
     * @throws Exception If the FXML resource cannot be loaded.
     */
    private void startGame(boolean isChallenge) throws Exception {
        URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        GuiController guiController = fxmlLoader.getController();

        // fix Pass 'stage' here so it exists before GameUIManager is created
        guiController.initModel(stage, audioManager, gameSettings, highScoreManager);

        // removed guiController.setGameStage(stage); bcoz redundant

        stage.setTitle(isChallenge ? "TetrisJFX - Challenge Mode" : "TetrisJFX - Normal Mode");
        stage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        stage.show();

        if (isChallenge) {
            new ChallengeModeController(guiController, highScoreManager);
        } else {
            new NormalModeController(guiController, highScoreManager);
        }
    }
}