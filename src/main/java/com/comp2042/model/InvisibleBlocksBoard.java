package com.comp2042.model;

import com.comp2042.util.MatrixOperations;

public class InvisibleBlocksBoard extends AbstractBoard {

    private int[][] renderBoard; // The "fake" board shown to the user

    private boolean revealActive = false;
    private long revealStartTime = 0;
    private static final long REVEAL_DURATION = 4000; // 4 seconds
    private static final long REVEAL_INTERVAL = 10000; // 10 seconds
    private long nextRevealTime;
    private volatile boolean gameActive = true;
    private int lastCountdown = 0;

    public InvisibleBlocksBoard(int height, int width) {
        super(height, width);
        this.renderBoard = new int[height][width];
        this.nextRevealTime = System.currentTimeMillis() + REVEAL_INTERVAL;
    }

    @Override
    public int[][] getBoardMatrix() {
        updateRevealState();
        return renderBoard;
    }

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

    @Override
    public ClearRow clearRows() {
        // Clear rows on the logical board
        ClearRow clearRow = MatrixOperations.checkRemoving(boardMatrix);
        boardMatrix = clearRow.getNewMatrix();

        // Sync the render board
        if (!revealActive) {
            hideLockedBlocks();
        } else {
            copyBoard(boardMatrix, renderBoard);
        }
        return clearRow;
    }
// Resets the board and game state
    @Override
    public void newGame() {
        boardMatrix = new int[height][width];
        renderBoard = new int[height][width];
        score.reset();
        revealActive = false;
        nextRevealTime = System.currentTimeMillis() + REVEAL_INTERVAL;
        createNewBrick();
    }

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
        for (int i = 0; i < renderBoard.length; i++) {
            for (int j = 0; j < renderBoard[i].length; j++) {
                renderBoard[i][j] = 0;
            }
        }
    }
    // calculates the number of seconds left until the next block appears
    public String getCountdown() {
        // Only show countdown during reveal
        if (!revealActive) {
            lastCountdown = 0;
            return null;
        }
        long timeLeft = (revealStartTime + REVEAL_DURATION) - System.currentTimeMillis();
        int secondsLeft = (int) Math.ceil(timeLeft / 1000.0);

        // If within the 3-second window (3, 2, or 1)
        if (secondsLeft <= 3 && secondsLeft > 0) {
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