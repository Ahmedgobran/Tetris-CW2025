package com.comp2042.model.board;

import com.comp2042.model.state.ClearRow;
import com.comp2042.util.MatrixOperations;

/**
 * A standard implementation of the Tetris board.
 * <p>
 * This class uses standard rules for matrix manipulation: bricks are permanently merged
 * into the grid, and completed rows are cleared immediately.
 * </p>
 */
public class TetrisBoard extends AbstractBoard {

    /**
     * Creates a standard board.
     *
     * @param height Board height in blocks.
     * @param width  Board width in blocks.
     */
    public TetrisBoard(int height, int width) {
        super(height, width);
    }

    /**
     * Returns the current state of the board grid.
     * @return The 2D integer array representing the board.
     */
    @Override
    public int[][] getBoardMatrix() {
        return boardMatrix;
    }

    /**
     * Merges the current active brick into the static background grid.
     */
    @Override
    public void mergeBrickToBackground() {
        boardMatrix = MatrixOperations.merge(boardMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    /**
     * Checks for and removes any full rows, shifting the board down.
     * @return A {@link ClearRow} object containing the new board state and number of cleared lines.
     */
    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(boardMatrix);
        boardMatrix = clearRow.newMatrix();
        return clearRow;
    }

    /**
     * Resets the board to an empty state and starts a new game.
     */
    @Override
    public void newGame() {
        boardMatrix = new int[height][width];
        score.reset();
        createNewBrick();
    }
}