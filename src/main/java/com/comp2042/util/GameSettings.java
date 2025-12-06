package com.comp2042.util;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Singleton class that stores global game configuration and user preferences.
 * <p>
 * Manages settings such as volume levels and visual toggles. Binds changes
 * directly to the {@link AudioManager} to apply them in real-time.
 * </p>
 */
public class GameSettings {

    private static GameSettings instance;

    // Audio settings
    private final DoubleProperty musicVolume = new SimpleDoubleProperty(0.5);
    private final DoubleProperty sfxVolume = new SimpleDoubleProperty(0.7);
    private final BooleanProperty musicEnabled = new SimpleBooleanProperty(true);
    private final BooleanProperty sfxEnabled = new SimpleBooleanProperty(true);

    // Visual settings
    private final BooleanProperty ghostPieceEnabled = new SimpleBooleanProperty(true);


    private GameSettings() {
        // Bind audio settings to AudioManager
        musicVolume.addListener((obs, oldVal, newVal) -> AudioManager.getInstance().setMusicVolume(newVal.doubleValue()));

        sfxVolume.addListener((obs, oldVal, newVal) -> AudioManager.getInstance().setSfxVolume(newVal.doubleValue()));

        musicEnabled.addListener((obs, oldVal, newVal) -> AudioManager.getInstance().setMusicEnabled(newVal));

        sfxEnabled.addListener((obs, oldVal, newVal) -> AudioManager.getInstance().setSfxEnabled(newVal));
    }

    /**
     * Retrieves the singleton instance of GameSettings.
     * @return The global settings instance.
     */
    public static GameSettings getInstance() {
        if (instance == null) {
            instance = new GameSettings();
        }
        return instance;
    }

    // Audio Properties

    /**
     * Gets the current music volume preference.
     * @return Value between 0.0 and 1.0.
     */
    public double getMusicVolume() {
        return musicVolume.get();
    }

    /**
     * Sets the music volume preference.
     * @param value Value between 0.0 and 1.0.
     */
    public void setMusicVolume(double value) {
        musicVolume.set(value);
    }

    /**
     * Gets the current SFX volume preference.
     * @return Value between 0.0 and 1.0.
     */
    public double getSfxVolume() {
        return sfxVolume.get();
    }

    /**
     * Sets the SFX volume preference.
     * @param value Value between 0.0 and 1.0.
     */
    public void setSfxVolume(double value) {
        sfxVolume.set(value);
    }

    /**
     * Checks if music is enabled.
     * @return true if music is on.
     */
    public boolean isMusicEnabled() {
        return musicEnabled.get();
    }

    /**
     * Toggles music on or off.
     * @param value true to enable.
     */
    public void setMusicEnabled(boolean value) {
        musicEnabled.set(value);
    }

    /**
     * Checks if sound effects are enabled.
     * @return true if SFX is on.
     */
    public boolean isSfxEnabled() {
        return sfxEnabled.get();
    }

    /**
     * Toggles sound effects on or off.
     * @param value true to enable.
     */
    public void setSfxEnabled(boolean value) {
        sfxEnabled.set(value);
    }

    // Visual Properties

    /**
     * Checks if the Ghost Piece (shadow) should be rendered.
     * @return true if enabled.
     */
    public boolean isGhostPieceEnabled() {
        return ghostPieceEnabled.get();
    }

    /**
     * Toggles the Ghost Piece rendering.
     * @param value true to enable.
     */
    public void setGhostPieceEnabled(boolean value) {
        ghostPieceEnabled.set(value);
    }
}