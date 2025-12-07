package com.comp2042.util;

import com.comp2042.controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

/**
 * Utility class for loading and managing scenes throughout the application.
 * <p>
 * Centralizes FXML loading logic and scene transitions (e.g., to Settings,
 * High Scores, Game Levels) to reduce code duplication across controllers.
 * This class also facilitates dependency injection by passing core services
 * (AudioManager, GameSettings, HighScoreManager) to the controllers of new scenes.
 * </p>
 */
public class SceneLoader {

    /**
     * Opens the Settings Panel, injecting necessary dependencies.
     * <p>
     * Saves the current scene to allow returning later.
     * </p>
     *
     * @param stage    The primary stage of the application.
     * @param settings The global GameSettings instance to be modified.
     * @param audio    The AudioManager instance for sound control.
     * @throws Exception If the FXML file cannot be loaded.
     */
    public static void openSettings(Stage stage, GameSettings settings, AudioManager audio) throws Exception {
        URL location = SceneLoader.class.getClassLoader().getResource("settingsPanel.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        SettingsController controller = fxmlLoader.getController();
        // Inject dependencies
        controller.initModel(settings, audio);

        // Save current scene to return to
        Scene currentScene = stage.getScene();
        controller.setOnCloseCallback(() -> stage.setScene(currentScene));
        controller.setStage(stage);

        stage.setScene(new Scene(root, 440, 510));
    }

    /**
     * Transitions the application to the Main Menu screen.
     * <p>
     * Initializes the main menu controller with all core game dependencies.
     * </p>
     *
     * @param stage       The primary stage.
     * @param audio       The AudioManager instance.
     * @param settings    The GameSettings instance.
     * @param highScores  The HighScoreManager instance.
     * @throws Exception If the FXML file cannot be loaded.
     */
    public static void openMainMenu(Stage stage, AudioManager audio, GameSettings settings, HighScoreManager highScores) throws Exception {
        URL location = SceneLoader.class.getClassLoader().getResource("mainMenu.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        MainMenuController controller = fxmlLoader.getController();
        controller.setStage(stage);
        controller.initModel(audio, settings, highScores);

        stage.setTitle("TetrisJFX");
        stage.setScene(new Scene(root, 500, 600));
    }

    /**
     * Opens the Level Selection screen.
     * <p>
     * Passes dependencies required to start a new game (Normal or Challenge mode).
     * </p>
     *
     * @param stage       The primary stage.
     * @param audio       The AudioManager instance.
     * @param settings    The GameSettings instance.
     * @param highScores  The HighScoreManager instance.
     * @throws Exception If the FXML file cannot be loaded.
     */
    public static void openLevelSelection(Stage stage, AudioManager audio, GameSettings settings, HighScoreManager highScores) throws Exception {
        URL location = SceneLoader.class.getClassLoader().getResource("levelSelection.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        LevelSelectionController controller = fxmlLoader.getController();
        // Pass all three dependencies
        controller.initModel(audio, settings, highScores);

        Scene currentScene = stage.getScene();
        controller.setOnBackCallback(() -> stage.setScene(currentScene));
        controller.setStage(stage);

        stage.setScene(new Scene(root, 500, 590));
    }

    /**
     * Opens the Controls/Instructions screen.
     *
     * @param stage The primary stage.
     * @param audio The AudioManager instance (for button sounds).
     * @throws Exception If the FXML file cannot be loaded.
     */
    public static void openControls(Stage stage, AudioManager audio) throws Exception {
        URL location = SceneLoader.class.getClassLoader().getResource("controlsPanel.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        ControlsController controller = fxmlLoader.getController();
        controller.initModel(audio);

        // Save current scene to return to
        Scene currentScene = stage.getScene();
        controller.setOnCloseCallback(() -> stage.setScene(currentScene));
        controller.setStage(stage);

        stage.setScene(new Scene(root, 470, 600));
    }


    /**
     * Opens the High Scores screen.
     *
     * @param stage   The primary stage.
     * @param manager The HighScoreManager to retrieve scores from.
     * @param audio   The AudioManager instance.
     * @throws Exception If the FXML file cannot be loaded.
     */
    public static void openHighScores(Stage stage, HighScoreManager manager, AudioManager audio) throws Exception {
        URL location = SceneLoader.class.getClassLoader().getResource("highScores.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        HighScoreController controller = fxmlLoader.getController();
        controller.initModel(manager, audio);

        Scene currentScene = stage.getScene();
        controller.setOnCloseCallback(() -> stage.setScene(currentScene));
        controller.setStage(stage);

        stage.setScene(new Scene(root, 440, 510));
    }
}