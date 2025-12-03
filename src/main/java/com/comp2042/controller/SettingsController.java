package com.comp2042.controller;

import com.comp2042.util.AudioManager;
import com.comp2042.util.GameSettings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML private CheckBox musicEnabledCheckbox;
    @FXML private Slider musicVolumeSlider;
    @FXML private Label musicVolumeLabel;

    @FXML private CheckBox sfxEnabledCheckbox;
    @FXML private Slider sfxVolumeSlider;
    @FXML private Label sfxVolumeLabel;

    @FXML private CheckBox ghostPieceCheckbox;

    private Stage stage;
    private Runnable onCloseCallback;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GameSettings settings = GameSettings.getInstance();

        // Initialize audio controls from saved settings
        musicEnabledCheckbox.setSelected(settings.isMusicEnabled());
        musicVolumeSlider.setValue(settings.getMusicVolume());
        updateMusicVolumeLabel();

        sfxEnabledCheckbox.setSelected(settings.isSfxEnabled());
        sfxVolumeSlider.setValue(settings.getSfxVolume());
        updateSfxVolumeLabel();

        // Initialize game controls from saved settings
        ghostPieceCheckbox.setSelected(settings.isGhostPieceEnabled());

        // Add listeners for real-time preview (doesn't save yet)
        musicVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateMusicVolumeLabel();
            // Preview volume change immediately
            AudioManager.getInstance().setMusicVolume(newVal.doubleValue());
        });

        sfxVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateSfxVolumeLabel();
            // Preview volume change immediately
            AudioManager.getInstance().setSfxVolume(newVal.doubleValue());
        });

        musicEnabledCheckbox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            musicVolumeSlider.setDisable(!newVal);
            // Preview enable/disable immediately
            AudioManager.getInstance().setMusicEnabled(newVal);
        });

        sfxEnabledCheckbox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            sfxVolumeSlider.setDisable(!newVal);
            // Preview enable/disable immediately
            AudioManager.getInstance().setSfxEnabled(newVal);
        });

        // Disable sliders if audio is disabled
        musicVolumeSlider.setDisable(!musicEnabledCheckbox.isSelected());
        sfxVolumeSlider.setDisable(!sfxEnabledCheckbox.isSelected());
    }

    private void updateMusicVolumeLabel() {
        int percent = (int) (musicVolumeSlider.getValue() * 100);
        musicVolumeLabel.setText(percent + "%");
    }

    private void updateSfxVolumeLabel() {
        int percent = (int) (sfxVolumeSlider.getValue() * 100);
        sfxVolumeLabel.setText(percent + "%");
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setOnCloseCallback(Runnable callback) {
        this.onCloseCallback = callback;
    }

    @FXML
    private void onSaveClicked(ActionEvent event) {
        saveSettings();
        closeSettings();
    }

    @FXML
    private void onBackClicked(ActionEvent event) {
        // Revert any preview changes to original saved values
        GameSettings settings = GameSettings.getInstance();

        // Revert audio settings to what was saved
        AudioManager.getInstance().setMusicVolume(settings.getMusicVolume());
        AudioManager.getInstance().setSfxVolume(settings.getSfxVolume());
        AudioManager.getInstance().setMusicEnabled(settings.isMusicEnabled());
        AudioManager.getInstance().setSfxEnabled(settings.isSfxEnabled());

        closeSettings();
    }

    private void saveSettings() {
        GameSettings settings = GameSettings.getInstance();

        // Save audio settings permanently
        settings.setMusicEnabled(musicEnabledCheckbox.isSelected());
        settings.setMusicVolume(musicVolumeSlider.getValue());
        settings.setSfxEnabled(sfxEnabledCheckbox.isSelected());
        settings.setSfxVolume(sfxVolumeSlider.getValue());

        // Save game settings
        settings.setGhostPieceEnabled(ghostPieceCheckbox.isSelected());

        System.out.println("Settings saved!");

    }

    private void closeSettings() {
        if (onCloseCallback != null) {
            onCloseCallback.run();
        } else if (stage != null) {
            stage.close();
        }
    }
}