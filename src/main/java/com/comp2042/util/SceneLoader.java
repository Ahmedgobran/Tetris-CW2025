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
 * </p>
 */
public class SceneLoader {

    /**
     * Opens the Settings Panel, saving the current scene state to allow returning.
     *
     * @param stage The primary stage of the application.
     * @throws Exception If the FXML file cannot be loaded.
     */
    public static void openSettings(Stage stage) throws Exception {
        URL location = SceneLoader.class.getClassLoader().getResource("settingsPanel.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        SettingsController settingsController = fxmlLoader.getController();

        // Save current scene to return to
        Scene currentScene = stage.getScene();

        // Set callback to return to previous scene
        settingsController.setOnCloseCallback(() -> stage.setScene(currentScene));

        Scene settingsScene = new Scene(root, 440, 510);
        stage.setScene(settingsScene);
    }

    /**
     * Transitions the application to the Main Menu screen.
     *
     * @param stage The primary stage.
     * @throws Exception If the FXML file cannot be loaded.
     */
    public static void openMainMenu(Stage stage) throws Exception {
        URL location = SceneLoader.class.getClassLoader().getResource("mainMenu.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        MainMenuController mainMenuController = fxmlLoader.getController();
        mainMenuController.setStage(stage);

        stage.setTitle("TetrisJFX");
        Scene mainMenuScene = new Scene(root, 500, 600);
        stage.setScene(mainMenuScene);
    }

    /**
     * Opens the Level Selection screen.
     *
     * @param stage The primary stage.
     * @throws Exception If the FXML file cannot be loaded.
     */
    public static void openLevelSelection(Stage stage) throws Exception {
        URL location = SceneLoader.class.getClassLoader().getResource("levelSelection.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();
        LevelSelectionController levelSelectionController = fxmlLoader.getController();

        // Save current scene to return to
        Scene currentScene = stage.getScene();

        // Set callback to return to menu
        levelSelectionController.setOnBackCallback(() -> stage.setScene(currentScene));

        levelSelectionController.setStage(stage);

        Scene levelSelectionScene = new Scene(root, 500, 590);
        stage.setScene(levelSelectionScene);
    }

    /**
     * Opens the Controls/Instructions screen.
     *
     * @param stage The primary stage.
     * @throws Exception If the FXML file cannot be loaded.
     */
    public static void openControls(Stage stage) throws Exception {
        URL location = SceneLoader.class.getClassLoader().getResource("controlsPanel.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        ControlsController controlsController = fxmlLoader.getController();

        // Save current scene to return to
        Scene currentScene = stage.getScene();

        // Set callback to return to previous scene
        controlsController.setOnCloseCallback(() -> stage.setScene(currentScene));
        controlsController.setStage(stage);

        Scene controlsScene = new Scene(root, 470, 600);
        stage.setScene(controlsScene);
    }

    /**
     * Opens the High Scores screen.
     *
     * @param stage The primary stage.
     * @throws Exception If the FXML file cannot be loaded.
     */
    public static void openHighScores(Stage stage) throws Exception {
        URL location = SceneLoader.class.getClassLoader().getResource("highScores.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        HighScoreController controller = fxmlLoader.getController();
        Scene currentScene = stage.getScene();
        controller.setOnCloseCallback(() -> stage.setScene(currentScene));
        controller.setStage(stage);

        Scene scene = new Scene(root, 440, 510);
        stage.setScene(scene);
    }
}