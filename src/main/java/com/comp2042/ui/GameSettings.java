package com.comp2042.ui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

// Stores all game settings

public class GameSettings {

    private static GameSettings instance;

    // Audio settings
    private final DoubleProperty musicVolume = new SimpleDoubleProperty(0.5);
    private final DoubleProperty sfxVolume = new SimpleDoubleProperty(0.7);
    private final BooleanProperty musicEnabled = new SimpleBooleanProperty(true);
    private final BooleanProperty sfxEnabled = new SimpleBooleanProperty(true);

    // Visual settings
    private final BooleanProperty ghostPieceEnabled = new SimpleBooleanProperty(true);

    // Game settings
    private final DoubleProperty gameSpeed = new SimpleDoubleProperty(400);  // milliseconds per tick

    private GameSettings() {
        // Bind audio settings to AudioManager
        musicVolume.addListener((obs, oldVal, newVal) -> {
            AudioManager.getInstance().setMusicVolume(newVal.doubleValue());
        });

        sfxVolume.addListener((obs, oldVal, newVal) -> {
            AudioManager.getInstance().setSfxVolume(newVal.doubleValue());
        });

        musicEnabled.addListener((obs, oldVal, newVal) -> {
            AudioManager.getInstance().setMusicEnabled(newVal);
        });

        sfxEnabled.addListener((obs, oldVal, newVal) -> {
            AudioManager.getInstance().setSfxEnabled(newVal);
        });
    }


    public static GameSettings getInstance() {
        if (instance == null) {
            instance = new GameSettings();
        }
        return instance;
    }

    // Audio Properties

    public DoubleProperty musicVolumeProperty() {
        return musicVolume;
    }

    public double getMusicVolume() {
        return musicVolume.get();
    }

    public void setMusicVolume(double value) {
        musicVolume.set(value);
    }

    public DoubleProperty sfxVolumeProperty() {
        return sfxVolume;
    }

    public double getSfxVolume() {
        return sfxVolume.get();
    }

    public void setSfxVolume(double value) {
        sfxVolume.set(value);
    }

    public BooleanProperty musicEnabledProperty() {
        return musicEnabled;
    }

    public boolean isMusicEnabled() {
        return musicEnabled.get();
    }

    public void setMusicEnabled(boolean value) {
        musicEnabled.set(value);
    }

    public BooleanProperty sfxEnabledProperty() {
        return sfxEnabled;
    }

    public boolean isSfxEnabled() {
        return sfxEnabled.get();
    }

    public void setSfxEnabled(boolean value) {
        sfxEnabled.set(value);
    }

    // Visual Properties

    public BooleanProperty ghostPieceEnabledProperty() {
        return ghostPieceEnabled;
    }

    public boolean isGhostPieceEnabled() {
        return ghostPieceEnabled.get();
    }

    public void setGhostPieceEnabled(boolean value) {
        ghostPieceEnabled.set(value);
    }

    // game Properties

    public DoubleProperty gameSpeedProperty() {
        return gameSpeed;
    }

    public double getGameSpeed() {
        return gameSpeed.get();
    }

    public void setGameSpeed(double value) {
        gameSpeed.set(value);
    }
}