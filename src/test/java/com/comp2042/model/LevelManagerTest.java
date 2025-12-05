package com.comp2042.model;

import javafx.beans.property.SimpleIntegerProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Verifies the dynamic difficulty system
 *  ensuring levels increase at correct score thresholds and game speed calculates correctly */

class LevelManagerTest {

    private LevelManager levelManager;
    private SimpleIntegerProperty scoreProperty;

    @BeforeEach
    void setUp() {
        levelManager = new LevelManager();
        scoreProperty = new SimpleIntegerProperty(0);
        // Bind the manager to our test score property
        levelManager.bindScore(scoreProperty);
    }

    @Test
    void testInitialState() {
        assertEquals(1, levelManager.levelProperty().get(), "Game should start at Level 1");
        assertEquals(400.0, levelManager.getCurrentDelay(), 0.01, "Initial delay should be 400ms");
    }

    @Test
    void testLevelUpAtThreshold() {
        // Score = 900 -> Still Level 1
        scoreProperty.set(900);
        assertEquals(1, levelManager.levelProperty().get());

        // Score = 1000 -> Level 2
        scoreProperty.set(1000);
        assertEquals(2, levelManager.levelProperty().get(), "Should level up at 1000 points");

        // Check Speed Increase (Level 2 should be faster than Level 1)
        // 400 * 0.85 = 340
        assertEquals(340.0, levelManager.getCurrentDelay(), 0.01, "Speed should increase (delay decreases) at Level 2");
    }

    @Test
    void testMultipleLevelUps() {
        // Score = 5500 -> Level 6
        scoreProperty.set(5500);
        assertEquals(6, levelManager.levelProperty().get(), "Should be at Level 6 for 5500 points");
    }

    @Test
    void testRestartReset() {
        // Go to Level 5
        scoreProperty.set(4000);
        assertEquals(5, levelManager.levelProperty().get());
        // Reset Score to 0 (New Game)
        scoreProperty.set(0);
        assertEquals(1, levelManager.levelProperty().get(), "Level should reset to 1 when score resets");
    }
}