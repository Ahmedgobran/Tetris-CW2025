package com.comp2042.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** tests the singleton pattern implementation and ensures audio/visual settings are stored and retrieved correctly */

class GameSettingsTest {

    @Test
    void testInstance() {
        GameSettings instance1 = GameSettings.getInstance();
        GameSettings instance2 = GameSettings.getInstance();

        assertSame(instance1, instance2, "GameSettings should return the same instance");
    }

    @Test
    void testDefaultValues() {
        GameSettings settings = GameSettings.getInstance();

        // check that everything is enabled by default
        assertTrue(settings.isMusicEnabled(), "music should be enabled by default");
        assertTrue(settings.isSfxEnabled(), "sfx should be enabled by default");
        assertEquals(0.5, settings.getMusicVolume(), 0.01, "Default music volume should be 0.5");
    }

    @Test
    void testPropertyUpdates() {
        GameSettings settings = GameSettings.getInstance();

        // Change value
        settings.setMusicVolume(0.8);
        settings.setGhostPieceEnabled(false);

        // Verify persistence
        assertEquals(0.8, settings.getMusicVolume());
        assertFalse(settings.isGhostPieceEnabled());
    }
}