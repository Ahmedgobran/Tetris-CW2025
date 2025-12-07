package com.comp2042.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link HighScoreManager} class.
 * <p>
 * Verifies that scores are sorted correctly in descending order and that the list
 * size is strictly capped at the top 10 scores.
 * </p>
 */
class HighScoreManagerTest {

    private static final String TEST_FILE = "test_scores.txt";
    private HighScoreManager manager;

    /**
     * Sets up a fresh manager instance before each test using a temporary file.
     * This isolates the test from your real game data.
     */
    @BeforeEach
    void setUp() {
        // Ensure we start with no file
        new File(TEST_FILE).delete();

        // Inject the test filename
        manager = new HighScoreManager(TEST_FILE);
    }

    /**
     * Cleans up the temporary file after each test finishes.
     */
    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    @Test
    void testScoresAreSorted() {
        // Add scores in random order
        manager.addScore(100);
        manager.addScore(500);
        manager.addScore(50);

        List<Integer> scores = manager.getScores();

        // Verify size (sanity check)
        assertTrue(scores.size() >= 3, "Should have at least the 3 added scores");

        // Check descending Order (Index 0 should be 500, Index 1 should be 100)
        assertEquals(500, scores.get(0), "First score should be the highest (500)");
        assertEquals(100, scores.get(1), "Second score should be the middle (100)");
        assertEquals(50, scores.get(2), "Third score should be the lowest (50)");
    }

    @Test
    void testScoreLimit() {
        // Add 15 scores (more than the limit of 10)
        for (int i = 0; i < 15; i++) {
            manager.addScore(i * 10);
        }

        // Verify cap
        assertTrue(manager.getScores().size() <= 10, "Should store max 10 scores");

        // Verify we kept the highest ones (since we added 0 to 140, max is 140)
        int topScore = manager.getScores().get(0);
        assertEquals(140, topScore, "Top score should be the highest value added");
    }
}