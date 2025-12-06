package com.comp2042.model.board;

import com.comp2042.model.state.ClearRow;
import com.comp2042.util.MatrixOperations;

import java.util.Arrays;

/**
 * A specialized Board implementation for the Challenge Mode.
 * <p>
 * In this mode, blocks on the board become invisible (hidden) shortly after they are locked.
 * The board maintains two states: a 'logical' matrix for collision detection and a 'render'
 * matrix for display. The render matrix is periodically cleared or revealed based on a timer.
 * </p>
 */
public class InvisibleBlocksBoard extends AbstractBoard {

    private int[][] renderBoard; // The "fake" board shown to the user

    private boolean revealActive = false;
    private long revealStartTime = 0;
    private static final long REVEAL_DURATION = 4000; // 4 seconds
    private static final long REVEAL_INTERVAL = 10000; // 10 seconds
    private static final int COUNTDOWN_SECONDS = 3;
    private long nextRevealTime;
    private volatile boolean gameActive = true;
    private int lastCountdown = 0;

    /**
     * Constructs a new InvisibleBlocksBoard with the specified dimensions.
     *
     * @param height The number of rows in the board.
     * @param width  The number of columns in the board.
     */
    public InvisibleBlocksBoard(int height, int width) {
        super(height, width);
        this.renderBoard = new int[height][width];
        this.nextRevealTime = System.currentTimeMillis() + REVEAL_INTERVAL;
    }

    /**
     * Retrieves the board matrix to be displayed to the user.
     * <p>
     * This method checks the internal timer to decide whether to return the actual blocks
     * (revealed) or an empty grid (invisible).
     * </p>
     *
     * @return A 2D integer array representing the visible state of the board.
     */
    @Override
    public int[][] getBoardMatrix() {
        updateRevealState();
        return renderBoard;
    }

    /**
     * Locks the current active brick into the logical board and updates the render board.
     * <p>
     * If the board is currently in "invisible mode", the newly locked blocks will be hidden.
     * </p>
     */
    @Override
    public void mergeBrickToBackground() {
        //  Merge into the Logical board (superclass field 'boardMatrix')
        boardMatrix = MatrixOperations.merge(boardMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());

        // Update the Render board
        if (!revealActive) {
            hideLockedBlocks();
        } else {
            copyBoard(boardMatrix, renderBoard);
        }
    }

    /**
     * Clears completed rows from the logical board and synchronizes the render board.
     *
     * @return A {@link ClearRow} object containing the updated logical matrix and score info.
     */
    @Override
    public ClearRow clearRows() {
        // Clear rows on the logical board
        ClearRow clearRow = MatrixOperations.checkRemoving(boardMatrix);
        boardMatrix = clearRow.newMatrix();

        // Sync the render board
        if (!revealActive) {
            hideLockedBlocks();
        } else {
            copyBoard(boardMatrix, renderBoard);
        }
        return clearRow;
    }

    /**
     * Resets the board state, score, and visibility timers for a new game.
     */
    @Override
    public void newGame() {
        boardMatrix = new int[height][width];
        renderBoard = new int[height][width];
        score.reset();
        revealActive = false;
        nextRevealTime = System.currentTimeMillis() + REVEAL_INTERVAL;
        createNewBrick();
    }

    /**
     * Stops the game logic processing.
     */
    public void stopGame() {
        gameActive = false;
    }

    /* Checks the system time to toggle the visibility of the blocks
    if reveal interval passed blocks become visible and if reveal duration ends it hides the blocks again */
    private synchronized void updateRevealState() {
        long currentTime = System.currentTimeMillis();
        if (!revealActive && currentTime >= nextRevealTime) {
            revealActive = true;
            revealStartTime = currentTime;
            copyBoard(boardMatrix, renderBoard);
            nextRevealTime = currentTime + REVEAL_INTERVAL;
        } else if (revealActive && (currentTime - revealStartTime >= REVEAL_DURATION)) {
            hideLockedBlocks();
            revealActive = false;
        }
    }

    private void copyBoard(int[][] source, int[][] destination) {
        for (int i = 0; i < source.length; i++) {
            System.arraycopy(source[i], 0, destination[i], 0, source[i].length);
        }
    }

    /* Clears the render board by setting all cells to 0 making the locked blocks invisible
    to the player while preserving them in the logical board */
    private void hideLockedBlocks() {
        for (int[] ints : renderBoard) {
            Arrays.fill(ints, 0);
        }
    }

    /**
     * Calculates the remaining time for the reveal phase to display a countdown.
     *
     * @return A String representing the seconds left (3, 2, 1) if applicable, or null.
     */
    public String getCountdown() {
        // Only show countdown during reveal
        if (!revealActive) {
            lastCountdown = 0;
            return null;
        }
        long timeLeft = (revealStartTime + REVEAL_DURATION) - System.currentTimeMillis();
        int secondsLeft = (int) Math.ceil(timeLeft / 1000.0);

        // If within the 3-second window (3, 2, or 1)
        if (secondsLeft <= COUNTDOWN_SECONDS && secondsLeft > 0) {
            // Only return if the number changed
            if (secondsLeft != lastCountdown) {
                lastCountdown = secondsLeft;
                return String.valueOf(secondsLeft);
            }
        } else {
            lastCountdown = 0;
        }
        return null;
    }
}