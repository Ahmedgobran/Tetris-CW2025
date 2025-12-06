package com.comp2042.model;

/**
 * Represents the possible lifecycle states of the game.
 * <p>
 * Used by the Controller to manage the game loop, input handling, and UI transitions.
 * </p>
 */
public enum GameStatus {
    /** The game is currently active and running. */
    PLAYING,

    /** The game logic is suspended (e.g., Pause Menu is open). */
    PAUSED,

    /** The game has ended (player lost). */
    GAME_OVER
}