package com.comp2042.controller;

import com.comp2042.util.AudioManager;
import com.comp2042.util.GameSettings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controls the Settings screen interactions.
 * <p>
 * Manages the configuration of Audio (Music/SFX volume and toggles) and
 * Gameplay options (Ghost Piece toggle). Updates are applied in real-time for preview,
 * but only saved permanently if the user clicks "Save".
 * </p>
 */
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

    /**
     * Initializes the settings UI with the current values from {@link GameSettings}.
     * Sets up listeners for sliders and checkboxes to provide real-time feedback.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GameSettings settings = GameSettings.getInstance();

        // Initialize UI controls with saved values
        musicEnabledCheckbox.setSelected(settings.isMusicEnabled());
        musicVolumeSlider.setValue(settings.getMusicVolume());
        updateMusicVolumeLabel();

        sfxEnabledCheckbox.setSelected(settings.isSfxEnabled());
        sfxVolumeSlider.setValue(settings.getSfxVolume());
        updateSfxVolumeLabel();

        // Initialize game controls from saved settings
        ghostPieceCheckbox.setSelected(settings.isGhostPieceEnabled());

        // listeners for real-time preview (doesn't save yet)
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

        // Set initial disable state
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

    /**
     * Sets the primary stage for this controller.
     *
     * @param stage The application window.
     */

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets the callback to execute when the settings screen is closed.
     *
     * @param callback The action to run (usually returning to the previous menu).
     */

    public void setOnCloseCallback(Runnable callback) {
        this.onCloseCallback = callback;
    }

    /**
     * Saves the current UI state to the GameSettings singleton and closes the screen.
     */
    @FXML
    private void onSaveClicked() {
        saveSettings();
        AudioManager.getInstance().playButtonPress();
        closeSettings();
    }

    /**
     * Discards changes and reverts GameSettings to their previous state before closing.
     */
    @FXML
    private void onBackClicked() {
        // Revert any preview changes to original saved values
        GameSettings settings = GameSettings.getInstance();
        AudioManager.getInstance().playButtonPress();

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