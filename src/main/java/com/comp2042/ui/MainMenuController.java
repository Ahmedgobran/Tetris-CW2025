package com.comp2042.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private ScrollPane controlsScrollPane;
    @FXML
    private Button settingsButton;

    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Hide controls panel initially
        if (controlsScrollPane != null) {
            controlsScrollPane.setVisible(false);
            controlsScrollPane.setManaged(false);
        }

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

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void onPlayClicked(ActionEvent event) {
        AudioManager.getInstance().playPlayPress();
        try {
            openLevelSelection();
        } catch (Exception e) {
            System.err.println("Error opening level selection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onControlsClicked(ActionEvent event) {
        AudioManager.getInstance().playButtonPress();
        if (controlsScrollPane != null) {
            boolean isVisible = controlsScrollPane.isVisible();
            controlsScrollPane.setVisible(!isVisible);
            controlsScrollPane.setManaged(!isVisible);
            // Adjust window size if controls panel is collapsed/expanded
            if (!isVisible) {
                stage.setHeight(670);
            } else {
                stage.setHeight(550);
            }
        }
    }

    @FXML
    private void onSettingsClicked(ActionEvent event) {
        AudioManager.getInstance().playButtonPress();
        try {
            openSettings();
        } catch (Exception e) {
            System.err.println("Error opening settings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onHighScoresClicked(ActionEvent event) {
        // TODO: Implement later
        System.out.println("do later");
    }

    @FXML
    private void onExitClicked(ActionEvent event) {
        AudioManager.getInstance().playButtonPress();
        AudioManager.getInstance().stopMusic();
        // Close the application
        stage.close();
        System.exit(0);
    }

    private void openLevelSelection() throws Exception {
        URL location = getClass().getClassLoader().getResource("levelSelection.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();
        LevelSelectionController levelSelectionController = fxmlLoader.getController();

        // Save current scene to return to
        Scene currentScene = stage.getScene();

        // Set callback to return to menu
        levelSelectionController.setOnBackCallback(() -> {
            stage.setScene(currentScene);
        });

        levelSelectionController.setStage(stage);

        Scene levelSelectionScene = new Scene(root, 455, 540);
        stage.setScene(levelSelectionScene);
    }

    private void openSettings() throws Exception {
        URL location = getClass().getClassLoader().getResource("settingsPanel.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        SettingsController settingsController = fxmlLoader.getController();

        // Save current scene to return to
        Scene currentScene = stage.getScene();

        // Set callback to return to menu
        settingsController.setOnCloseCallback(() -> {
            stage.setScene(currentScene);
        });

        Scene settingsScene = new Scene(root, 440, 510); // adjust size of settings window size
        stage.setScene(settingsScene);
    }
}