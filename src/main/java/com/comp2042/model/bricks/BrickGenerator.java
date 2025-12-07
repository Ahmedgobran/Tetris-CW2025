package com.comp2042.model.bricks;

/**
 * Interface for generating Brick objects for the game.
 * <p>
 * Allows for different generation strategies, such as random generation (Standard Game)
 * or deterministic generation (Testing/Tutorials).
 * </p>
 */
public interface BrickGenerator {

    /**
     * Retrieves the next brick in the queue and generates a replacement for the future.
     *
     * @return The Brick object to be used as the active piece.
     */
    Brick getBrick();

    /**
     * Peeks at the upcoming brick without removing it from the queue.
     * Used to display the "Next Piece" preview in the UI.
     *
     * @return The Brick object scheduled to appear next.
     */
    Brick getNextBrick();
}