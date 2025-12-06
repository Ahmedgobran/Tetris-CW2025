package com.comp2042.model.board;

/**
 * A standard implementation of the Tetris board.
 * <p>
 * This class uses the standard behavior defined in {@link AbstractBoard}.
 * Since standard Tetris rules for merging and clearing rows do not require extra
 * processing (like hiding blocks), this class relies on the default hook implementations.
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
     * Resets the board to an empty state and starts a new game.
     */
    @Override
    public void newGame() {
        boardMatrix = new int[height][width];
        score.reset();
        createNewBrick();
    }
}