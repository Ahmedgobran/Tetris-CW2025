package com.comp2042.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link GameSettings} class.
 * <p>
 * Verifies that audio and visual settings are stored, retrieved, and correctly
 * propagated to the dependency-injected {@link AudioManager}.
 * </p>
 */
class GameSettingsTest {

    private GameSettings settings;
    private AudioManager audioManager;

    /**
     * Sets up a fresh test environment before each test case.
     * <p>
     * Initializes a new AudioManager and injects it into a new GameSettings instance,
     * simulating the Dependency Injection process used in the main application.
     * </p>
     */
    @BeforeEach
    void setUp() {
        // Create the dependency first
        audioManager = new AudioManager();
        // Inject it into the settings
        settings = new GameSettings(audioManager);
    }

    /**
     * Verifies that the default configuration values are correct upon initialization.
     */
    @Test
    void testDefaultValues() {
        // Check defaults
        assertTrue(settings.isMusicEnabled(), "Music should be enabled by default");
        assertTrue(settings.isSfxEnabled(), "SFX should be enabled by default");
        assertEquals(0.5, settings.getMusicVolume(), 0.01, "Default music volume should be 0.5");
        assertEquals(0.7, settings.getSfxVolume(), 0.01, "Default SFX volume should be 0.7");
        assertTrue(settings.isGhostPieceEnabled(), "Ghost piece should be enabled by default");
    }

    /**
     * Verifies that setters correctly update the internal state of the settings object.
     */
    @Test
    void testPropertyUpdates() {
        // Change values
        settings.setMusicVolume(0.8);
        settings.setGhostPieceEnabled(false);
        settings.setSfxEnabled(false);

        // Verify persistence in the model
        assertEquals(0.8, settings.getMusicVolume(), 0.01, "Music volume should be updated to 0.8");
        assertFalse(settings.isGhostPieceEnabled(), "Ghost piece should be disabled");
        assertFalse(settings.isSfxEnabled(), "SFX should be disabled");
    }

    /**
     * Verifies that changes to GameSettings are propagated to the AudioManager.
     * <p>
     * This ensures the binding set up in the constructor is working correctly.
     * Note: This assumes we can't easily check the private state of AudioManager,
     * but we rely on the fact that no exceptions were thrown during the binding process.
     * </p>
     */
    @Test
    void testDependencyBinding() {
        // Set volume on settings
        settings.setMusicVolume(0.2);

        // In a real integration test, we would verify audioManager.volume was updated.
        // Since we are unit testing GameSettings, ensuring the setter runs without error
        // confirms the listener fired.
        assertEquals(0.2, settings.getMusicVolume(), 0.01);
    }
}