package com.comp2042.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

// manages all audio for the game (music and sound effects)

public class AudioManager {

    private static AudioManager instance;

    private MediaPlayer musicPlayer;
    private MediaPlayer sfxPlayer;

    private double musicVolume = 0.5;  // 0.0 to 1.0
    private double sfxVolume = 0.7;    // 0.0 to 1.0
    private boolean musicEnabled = true;
    private boolean sfxEnabled = true;

    // Private constructor
    private AudioManager() {
    }


    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    // Initialize and play background music
    public void playMusic(String resourcePath) {
        try {
            URL resource = getClass().getResource(resourcePath);
            if (resource == null) {
                System.err.println("Music file not found: " + resourcePath);
                return;
            }

            // Stop existing music
            stopMusic();

            Media media = new Media(resource.toExternalForm());
            musicPlayer = new MediaPlayer(media);
            musicPlayer.setVolume(musicVolume);
            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop forever

            if (musicEnabled) {
                musicPlayer.play();
            }
        } catch (Exception e) {
            System.err.println("Error playing music: " + e.getMessage());
        }
    }

    //play a sound effect (doesn't loop)
    public void playSFX(String resourcePath) {
        if (!sfxEnabled) return;

        try {
            URL resource = getClass().getResource(resourcePath);
            if (resource == null) {
                System.err.println("SFX file not found: " + resourcePath);
                return;
            }

            Media media = new Media(resource.toExternalForm());
            sfxPlayer = new MediaPlayer(media);
            sfxPlayer.setVolume(sfxVolume);
            sfxPlayer.play();

            // Clean up after playing
            sfxPlayer.setOnEndOfMedia(() -> sfxPlayer.dispose());
        } catch (Exception e) {
            System.err.println("Error playing SFX: " + e.getMessage());
        }
    }

    //Stop background music
    public void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.dispose();
            musicPlayer = null;
        }
    }


    // Volume Controls

    public void setMusicVolume(double volume) {
        this.musicVolume = Math.max(0.0, Math.min(1.0, volume));
        if (musicPlayer != null) {
            musicPlayer.setVolume(musicVolume);
        }
    }


    public void setSfxVolume(double volume) {
        this.sfxVolume = Math.max(0.0, Math.min(1.0, volume));
    }


    //Enable/Disable

    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        if (musicPlayer != null) {
            if (enabled) {
                musicPlayer.play();
            } else {
                musicPlayer.pause();
            }
        }
    }

    public void setSfxEnabled(boolean enabled) {
        this.sfxEnabled = enabled;
    }

    public void playButtonPress() {
        playSFX("/sfx/button-press.mp3");
    }

    public void playPlayPress() {
        playSFX("/sfx/play-press.mp3");
    }
}