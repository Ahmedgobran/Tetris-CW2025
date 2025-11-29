package com.comp2042.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Utility class for loading and managing scenes throughout the application
 * Reduces code duplication across controllers
 */

public class SceneLoader {

    // Opens the settings panel from any screen and returns to the current scene

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

    // Opens the main menu
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

    // Opens the level selection menu

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

        Scene levelSelectionScene = new Scene(root, 455, 570);
        stage.setScene(levelSelectionScene);
    }
}