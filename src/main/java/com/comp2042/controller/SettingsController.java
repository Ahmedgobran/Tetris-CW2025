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
 * Controls the interactions for the Settings screen.
 * <p>
 * This controller manages the configuration of Audio (Music/SFX volume and toggles) and
 * Gameplay options (Ghost Piece toggle). It supports "Real-time Preview," meaning that
 * dragging sliders applies the volume change immediately for feedback.
 * </p>
 * <p>
 * Changes are only permanently saved to the {@link GameSettings} model if the user
 * clicks "Save". Clicking "Back" reverts the audio state to its previous values.
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
    private GameSettings settings;
    private AudioManager audioManager;

    /**
     * Standard FXML initialization hook.
     * <p>
     * Logic is deferred to {@link #initModel(GameSettings, AudioManager)} because
     * this controller requires external dependencies to function.
     * </p>
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Init logic moved to initModel because we need dependencies first
    }

    /**
     * Initializes the controller with the required model and service dependencies.
     * <p>
     * Populates the UI elements with the current values from {@link GameSettings} and
     * sets up listeners for user interaction.
     * </p>
     *
     * @param settings     The game settings model to read from and write to.
     * @param audioManager The audio service to apply real-time volume previews.
     */
    public void initModel(GameSettings settings, AudioManager audioManager) {
        this.settings = settings;
        this.audioManager = audioManager;

        // Initialize UI with values from injected settings
        musicEnabledCheckbox.setSelected(settings.isMusicEnabled());
        musicVolumeSlider.setValue(settings.getMusicVolume());
        updateMusicVolumeLabel();

        sfxEnabledCheckbox.setSelected(settings.isSfxEnabled());
        sfxVolumeSlider.setValue(settings.getSfxVolume());
        updateSfxVolumeLabel();

        // Initialize game controls from saved settings
        ghostPieceCheckbox.setSelected(settings.isGhostPieceEnabled());

        setupListeners();
    }

    /**
     * Configures listeners on UI controls to provide real-time feedback.
     * <p>
     * Updates text labels and calls the AudioManager immediately when sliders move,
     * allowing the user to hear the volume change before saving.
     * </p>
     */
    private void setupListeners() {
        musicVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateMusicVolumeLabel();
            // Preview using injected audio manager
            audioManager.setMusicVolume(newVal.doubleValue());
        });

        sfxVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateSfxVolumeLabel();
            audioManager.setSfxVolume(newVal.doubleValue());
        });

        musicEnabledCheckbox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            musicVolumeSlider.setDisable(!newVal);
            audioManager.setMusicEnabled(newVal);
        });

        sfxEnabledCheckbox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            sfxVolumeSlider.setDisable(!newVal);
            audioManager.setSfxEnabled(newVal);
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
     * Sets the primary stage for this scene.
     * @param stage The application window.
     */
    public void setStage(Stage stage) { this.stage = stage; }

    /**
     * Registers a callback to execute when the settings screen is closed.
     * @param callback The action to run (usually returning to the previous menu).
     */
    public void setOnCloseCallback(Runnable callback) { this.onCloseCallback = callback; }

    /**
     * Handles the "Save" button click.
     * <p>
     * Persists the current state of the UI elements into the {@link GameSettings} model
     * and closes the screen.
     * </p>
     */
    @FXML
    private void onSaveClicked() {
        // Save to GameSettings
        settings.setMusicEnabled(musicEnabledCheckbox.isSelected());
        settings.setMusicVolume(musicVolumeSlider.getValue());
        settings.setSfxEnabled(sfxEnabledCheckbox.isSelected());
        settings.setSfxVolume(sfxVolumeSlider.getValue());
        settings.setGhostPieceEnabled(ghostPieceCheckbox.isSelected());

        audioManager.playButtonPress();
        closeSettings();
    }

    /**
     * Handles the "Back" button click.
     * <p>
     * Discards any changes made in the UI and reverts the {@link AudioManager} to the
     * values currently stored in the {@link GameSettings} model.
     * </p>
     */
    @FXML
    private void onBackClicked() {
        audioManager.playButtonPress();
        // Revert to saved settings
        audioManager.setMusicVolume(settings.getMusicVolume());
        audioManager.setSfxVolume(settings.getSfxVolume());
        audioManager.setMusicEnabled(settings.isMusicEnabled());
        audioManager.setSfxEnabled(settings.isSfxEnabled());
        closeSettings();
    }

    private void closeSettings() {
        if (onCloseCallback != null) onCloseCallback.run();
        else if (stage != null) stage.close();
    }
}