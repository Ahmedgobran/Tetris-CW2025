package com.comp2042.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

/**
 * Singleton manager for all audio playback in the application.
 * <p>
 * Handles loading and playing of background music (looped) and sound effects (one-shot).
 * Centralizes volume control and mute settings for the entire game.
 * </p>
 */
public class AudioManager {

    private MediaPlayer musicPlayer;
    private MediaPlayer sfxPlayer;

    private double musicVolume = 0.5;  // 0.0 to 1.0
    private double sfxVolume = 0.7;    // 0.0 to 1.0
    private boolean musicEnabled = true;
    private boolean sfxEnabled = true;

    public AudioManager() {
    }

    /**
     * Plays a background music track in a loop.
     * Stops any currently playing music before starting the new track.
     *
     * @param resourcePath The relative path to the music resource (e.g., "/music/track.mp3").
     */
    public void playMusic(String resourcePath) {
        try {
            URL resource = getClass().getResource(resourcePath);
            if (resource == null) return;

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

    /**
     * Plays a short sound effect (SFX) once.
     *
     * @param resourcePath The relative path to the SFX resource.
     */
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

    /**
     * Stops and disposes of the currently playing background music.
     */
    public void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.dispose();
            musicPlayer = null;
        }
    }


    // Volume Controls

    /**
     * Sets the volume for background music.
     * @param volume A value between 0.0 (mute) and 1.0 (max).
     */
    public void setMusicVolume(double volume) {
        this.musicVolume = Math.max(0.0, Math.min(1.0, volume));
        if (musicPlayer != null) {
            musicPlayer.setVolume(musicVolume);
        }
    }

    /**
     * Sets the volume for sound effects.
     * @param volume A value between 0.0 (mute) and 1.0 (max).
     */
    public void setSfxVolume(double volume) {
        this.sfxVolume = Math.max(0.0, Math.min(1.0, volume));
    }

    // Enable/Disable

    /**
     * Enables or disables background music playback.
     * @param enabled true to play music, false to mute.
     */
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

    /**
     * Enables or disables sound effects.
     * @param enabled true to allow SFX, false to mute.
     */
    public void setSfxEnabled(boolean enabled) {
        this.sfxEnabled = enabled;
    }

    /**
     * Helper method to play the standard button press sound.
     */
    public void playButtonPress() {
        playSFX("/sfx/button-press.mp3");
    }

    /**
     * Helper method to play the 'Play' button start sound.
     */
    public void playPlayPress() {
        playSFX("/sfx/play-press.mp3");
    }
}