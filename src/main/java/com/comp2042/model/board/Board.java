package com.comp2042.model.board;

import com.comp2042.model.Score;
import com.comp2042.model.state.ClearRow;
import com.comp2042.model.state.ViewData;

/**
 * Defines the contract for the game board logic.
 * <p>
 * This interface specifies the core mechanics for controlling the game grid,
 * manipulating the active brick (movement, rotation), managing game state (score, new game),
 * and handling collision detection and row clearing.
 * </p>
 */
public interface Board {

    /**
     * Attempts to move the current active brick downwards by one row.
     *
     * @return true if the move was successful; false if blocked by the floor or another block.
     */
    boolean moveBrickDown();

    /**
     * Attempts to move the current active brick one column to the left.
     *
     * @return true if the move was successful; false if blocked by the wall or another block.
     */
    boolean moveBrickLeft();

    /**
     * Attempts to move the current active brick one column to the right.
     *
     * @return true if the move was successful; false if blocked by the wall or another block.
     */
    boolean moveBrickRight();

    /**
     * Rotates the current active brick in a counter-clockwise direction.
     *
     * @return true if the rotation was valid; false if the new position causes a collision.
     */
    boolean rotateLeftBrick();

    /**
     * Spawns a new active brick at the top of the board.
     *
     * @return true if the brick was spawned successfully; false if the spawn position is blocked (Game Over).
     */
    boolean createNewBrick();

    /**
     * Retrieves the internal grid representation of the board.
     *
     * @return A 2D integer array representing the board, where non-zero values indicate occupied blocks.
     */
    int[][] getBoardMatrix();

    /**
     * Creates a snapshot of the current view state for rendering purposes.
     *
     * @return A {@link ViewData} object containing data about the board, active brick, and next brick.
     */
    ViewData getViewData();

    /**
     * Locks the current active brick into the static board matrix.
     * This is typically called when the brick lands and can no longer move.
     */
    void mergeBrickToBackground();

    /**
     * Scans the board for full rows, clears them, and shifts remaining blocks down.
     *
     * @return A {@link ClearRow} object containing details about the cleared lines and updated board state.
     */
    ClearRow clearRows();

    /**
     * Retrieves the score manager associated with this board.
     *
     * @return The {@link Score} object tracking the player's points.
     */
    Score getScore();

    /**
     * Resets the board state to start a new game.
     * Clears the grid and resets the score.
     */
    void newGame();

    /**
     * Calculates the Y-coordinate where the current brick would land if dropped instantly.
     * Used to render the "Ghost Piece" or shadow.
     *
     * @return The Y-coordinate index of the projected landing position.
     */
    int getShadowYPosition();

    /**
     * Instantly drops the current brick to the lowest possible valid position.
     *
     * @return The number of rows the brick fell.
     */
    int hardDrop();
}