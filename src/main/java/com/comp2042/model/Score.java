package com.comp2042.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Manages the player's score during the game.
 * <p>
 * This class wraps an {@link IntegerProperty} to allow for real-time data binding
 * with the UI labels in the game view.
 * </p>
 */
public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);

    /**
     * Retrieves the observable score property.
     *
     * @return The IntegerProperty representing the current score.
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Adds the specified amount to the current score.
     *
     * @param i The number of points to add.
     */
    public void add(int i){
        score.setValue(score.getValue() + i);
    }

    /**
     * Resets the score to zero.
     */
    public void reset() {
        score.setValue(0);
    }
}