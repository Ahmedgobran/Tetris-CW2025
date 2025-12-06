package com.comp2042.model.board;

import java.util.Arrays;

/**
 * A specialized Board implementation for the Challenge Mode.
 * <p>
 * In this mode, blocks on the board become invisible (hidden) shortly after they are locked.
 * This class overrides the {@link #onAfterMerge()} and {@link #onAfterClear()} hooks
 * from {@link AbstractBoard} to synchronize the invisible "Render Board" with the logical board.
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
     * Updates the render board after a brick has been merged.
     * <p>
     * This ensures that if the board is currently in "invisible mode", the newly locked
     * blocks are immediately hidden from the player.
     * </p>
     */
    @Override
    protected void onAfterMerge() {
        updateRenderBoard();
    }

    /**
     * Updates the render board after rows have been cleared.
     * <p>
     * This synchronizes the render board with the logical board state to ensure
     * proper positioning of the remaining blocks (even if they are invisible).
     * </p>
     */
    @Override
    protected void onAfterClear() {
        updateRenderBoard();
    }

    // Helper to sync state
    private void updateRenderBoard() {
        if (!revealActive) {
            hideLockedBlocks();
        } else {
            copyBoard(boardMatrix, renderBoard);
        }
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
        // gameActive logic handled by controller state but method kept for interface compatibility
    }

    /* Checks the system time to toggle the visibility of the blocks. */
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

    /* Clears the render board by setting all cells to 0 making the locked blocks invisible. */
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