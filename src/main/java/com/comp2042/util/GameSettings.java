package com.comp2042.util;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Manages game configuration and user preferences.
 * <p>
 * This class stores settings such as volume levels and visual toggles.
 * It uses JavaFX properties to allow for real-time binding, ensuring that changes
 * (e.g., volume slider adjustment) are immediately applied to the {@link AudioManager}.
 * </p>
 * <p>
 * <strong>Note:</strong> Refactored from a Singleton to a standard class to support Dependency Injection.
 * </p>
 */
public class GameSettings {

    // Audio settings
    private final DoubleProperty musicVolume = new SimpleDoubleProperty(0.5);
    private final DoubleProperty sfxVolume = new SimpleDoubleProperty(0.7);
    private final BooleanProperty musicEnabled = new SimpleBooleanProperty(true);
    private final BooleanProperty sfxEnabled = new SimpleBooleanProperty(true);

    // Visual settings
    private final BooleanProperty ghostPieceEnabled = new SimpleBooleanProperty(true);

    /**
     * Constructs a new GameSettings instance and binds it to the provided Audio Manager.
     * <p>
     * Sets up listeners on the audio properties so that any changes to volume or mute state
     * are automatically propagated to the AudioManager.
     * </p>
     *
     * @param audioManager The audio manager that will respond to setting changes.
     */
    public GameSettings(AudioManager audioManager) {
        // Direct dependency injection binding
        musicVolume.addListener((obs, oldVal, newVal) -> audioManager.setMusicVolume(newVal.doubleValue()));
        sfxVolume.addListener((obs, oldVal, newVal) -> audioManager.setSfxVolume(newVal.doubleValue()));
        musicEnabled.addListener((obs, oldVal, newVal) -> audioManager.setMusicEnabled(newVal));
        sfxEnabled.addListener((obs, oldVal, newVal) -> audioManager.setSfxEnabled(newVal));
    }

    // --- Audio Properties ---

    /**
     * Retrieves the current music volume preference.
     *
     * @return A value between 0.0 (mute) and 1.0 (max volume).
     */
    public double getMusicVolume() {
        return musicVolume.get();
    }

    /**
     * Sets the music volume preference.
     *
     * @param value A value between 0.0 and 1.0.
     */
    public void setMusicVolume(double value) {
        musicVolume.set(value);
    }

    /**
     * Retrieves the current sound effects (SFX) volume preference.
     *
     * @return A value between 0.0 (mute) and 1.0 (max volume).
     */
    public double getSfxVolume() {
        return sfxVolume.get();
    }

    /**
     * Sets the sound effects (SFX) volume preference.
     *
     * @param value A value between 0.0 and 1.0.
     */
    public void setSfxVolume(double value) {
        sfxVolume.set(value);
    }

    /**
     * Checks if background music is currently enabled.
     *
     * @return true if music is allowed to play, false otherwise.
     */
    public boolean isMusicEnabled() {
        return musicEnabled.get();
    }

    /**
     * Toggles background music on or off.
     *
     * @param value true to enable music, false to mute it.
     */
    public void setMusicEnabled(boolean value) {
        musicEnabled.set(value);
    }

    /**
     * Checks if sound effects are currently enabled.
     *
     * @return true if SFX are allowed to play, false otherwise.
     */
    public boolean isSfxEnabled() {
        return sfxEnabled.get();
    }

    /**
     * Toggles sound effects on or off.
     *
     * @param value true to enable SFX, false to mute them.
     */
    public void setSfxEnabled(boolean value) {
        sfxEnabled.set(value);
    }

    // --- Visual Properties ---

    /**
     * Checks if the Ghost Piece (shadow) should be rendered on the board.
     *
     * @return true if the ghost piece is enabled, false otherwise.
     */
    public boolean isGhostPieceEnabled() {
        return ghostPieceEnabled.get();
    }

    /**
     * Toggles the rendering of the Ghost Piece.
     *
     * @param value true to show the ghost piece, false to hide it.
     */
    public void setGhostPieceEnabled(boolean value) {
        ghostPieceEnabled.set(value);
    }
}