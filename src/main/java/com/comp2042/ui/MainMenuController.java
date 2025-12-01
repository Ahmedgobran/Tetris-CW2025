package com.comp2042.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private void onPlayClicked() {
        AudioManager.getInstance().playPlayPress();
        try {
            SceneLoader.openLevelSelection(stage);
        } catch (Exception e) {
            System.err.println("Error opening level selection: " + e.getMessage());
            e.printStackTrace();
        }
    }

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

    @FXML
    private void onHighScoresClicked() {
        // TODO: Implement later
        System.out.println("do later");
    }

    @FXML
    private void onExitClicked() {
        AudioManager.getInstance().playButtonPress();
        AudioManager.getInstance().stopMusic();
        // Close the application
        stage.close();
        System.exit(0);
    }

}