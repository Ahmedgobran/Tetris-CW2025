package com.comp2042.core;

public class InvisibleBlocksBoard extends AbstractBoard {

    private int[][] renderBoard; // The "fake" board shown to the user

    private boolean revealActive = false;
    private long revealStartTime = 0;
    private static final long REVEAL_DURATION = 2000;
    private static final long REVEAL_INTERVAL = 7000;
    private long nextRevealTime;
    private volatile boolean gameActive = true;

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

    private void hideLockedBlocks() {
        for (int i = 0; i < renderBoard.length; i++) {
            for (int j = 0; j < renderBoard[i].length; j++) {
                renderBoard[i][j] = 0;
            }
        }
    }
}