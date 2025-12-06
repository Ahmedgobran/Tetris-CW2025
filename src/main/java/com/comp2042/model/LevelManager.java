package com.comp2042.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Manages the game progression system, including level calculation and dynamic speed adjustments.
 * <p>
 * This class monitors the player's score and automatically increases the difficulty (Level)
 * at set thresholds. It also calculates the appropriate game loop speed for the current level.
 * </p>
 */
public class LevelManager {

    private static final int POINTS_PER_LEVEL = 1000; // decides how many points u need to proceed to next level
    private static final double INITIAL_DELAY = 400.0;
    private static final double SPEED_MULTIPLIER = 0.85; // controls speed multiplier per level

    private final IntegerProperty level = new SimpleIntegerProperty(1);

    /**
     * Connects the level manager to the game score.
     * <p>
     * Adds a listener to the score property that recalculates the level whenever the score changes.
     * The level increases for every 1000 points earned (e.g., 0-999 is Level 1, 1000-1999 is Level 2).
     * </p>
     *
     * @param scoreProperty The observable integer property representing the current game score.
     */
    public void bindScore(IntegerProperty scoreProperty) {
        scoreProperty.addListener((obs, oldVal, newVal) -> {
            int currentScore = newVal.intValue();

            // Logic: 0-999 = Lvl 1, 1000-1999 = Lvl 2, etc.
            int calculatedLevel = (currentScore / POINTS_PER_LEVEL) + 1;
            // only update if we level up
            if (calculatedLevel != level.get()) {
                level.set(calculatedLevel);
            }
        });
    }

    /**
     * Calculates the game loop delay (falling speed) for the current level.
     * <p>
     * The speed increases (delay decreases) by 15% for each level gained.
     * Formula: {@code Initial_Delay * (0.85 ^ (Level - 1))}
     * </p>
     *
     * @return The delay in milliseconds between game ticks.
     */
    public double getCurrentDelay() {
        return INITIAL_DELAY * Math.pow(SPEED_MULTIPLIER, level.get() - 1);
    }

    /**
     * Retrieves the observable property for the current level.
     * <p>
     * This can be bound to UI labels to display the level in real-time.
     * </p>
     *
     * @return The IntegerProperty representing the current level.
     */
    public IntegerProperty levelProperty() {
        return level;
    }
}