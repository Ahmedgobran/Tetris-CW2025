package com.comp2042.model.state;

import com.comp2042.util.MatrixOperations;

/**
 * An immutable snapshot of the game's visual state.
 * <p>
 * This record acts as a Data Transfer Object (DTO) to pass the game state from the
 * Model to the View. It contains all necessary information to render a single frame,
 * including the active brick, its position, the next brick, the shadow, and the held brick.
 * </p>
 *
 * @param brickData       The matrix of the currently falling active brick.
 * @param xPosition       The current X-coordinate (column) of the active brick.
 * @param yPosition       The current Y-coordinate (row) of the active brick.
 * @param nextBrickData   The matrix of the upcoming brick (for the Next Piece preview).
 * @param shadowYPosition The projected Y-coordinate where the brick would land (Ghost Piece).
 * @param heldBrickData   The matrix of the currently held brick (can be null).
 */
public record ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int shadowYPosition, int[][] heldBrickData) {

    /**
     * Alternative constructor for compatibility with older tests/code (sets held brick to null).
     *
     * @param brickData     The active brick matrix.
     * @param xPosition     The X coordinate.
     * @param yPosition     The Y coordinate.
     * @param nextBrickData The next brick matrix.
     * @param shadowYPosition The shadow Y coordinate.
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int shadowYPosition) {
        this(brickData, xPosition, yPosition, nextBrickData, shadowYPosition, null);
    }

    /**
     * Alternative constructor for legacy code (without shadow or hold).
     *
     * @param brickData     The active brick matrix.
     * @param xPosition     The X coordinate.
     * @param yPosition     The Y coordinate.
     * @param nextBrickData The next brick matrix.
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData) {
        this(brickData, xPosition, yPosition, nextBrickData, yPosition, null);
    }

    /**
     * Retrieves the active brick matrix.
     * @return A defensive copy of the brick data.
     */
    @Override
    public int[][] brickData() {
        return MatrixOperations.copy(brickData);
    }

    /**
     * Retrieves the next brick matrix.
     * @return A defensive copy of the next brick data.
     */
    @Override
    public int[][] nextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    /**
     * Retrieves the held brick matrix.
     * @return A defensive copy of the held brick data, or null if no brick is held.
     */
    public int[][] heldBrickData() {
        return heldBrickData != null ? MatrixOperations.copy(heldBrickData) : null;
    }
}