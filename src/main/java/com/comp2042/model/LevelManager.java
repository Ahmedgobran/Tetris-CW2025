package com.comp2042.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class LevelManager {

    private static final int POINTS_PER_LEVEL = 1000; // decides how many points u need to proceed to next level
    private static final double INITIAL_DELAY = 400.0;
    private static final double SPEED_MULTIPLIER = 0.85; // controls speed multiplier per level

    private final IntegerProperty level = new SimpleIntegerProperty(1);

    // bind directly to the score property
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
    // calculate speed based on the current level
    public double getCurrentDelay() {
        return INITIAL_DELAY * Math.pow(SPEED_MULTIPLIER, level.get() - 1);
    }

    public IntegerProperty levelProperty() {
        return level;
    }
}