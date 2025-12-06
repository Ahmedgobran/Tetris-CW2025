package com.comp2042.model.board;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import com.comp2042.model.BrickRotator;
import com.comp2042.model.Score;
import com.comp2042.model.state.ClearRow;
import com.comp2042.model.state.NextShapeInfo;
import com.comp2042.model.state.ViewData;
import com.comp2042.util.MatrixOperations;

import java.awt.Point;

/**
 * An abstract implementation of the {@link Board} interface, providing the core logic for
 * brick movement, collision detection, and rotation.
 * <p>
 * This class uses the <strong>Template Method Design Pattern</strong> for board updates.
 * The {@link #mergeBrickToBackground()} and {@link #clearRows()} methods define the standard
 * algorithm for updating the board, while {@link #onAfterMerge()} and {@link #onAfterClear()}
 * serve as "hooks" that subclasses can override to inject custom behavior (e.g., visual effects).
 * </p>
 */
public abstract class AbstractBoard implements Board {

    protected final int width;
    protected final int height;
    protected final BrickGenerator brickGenerator;
    protected final BrickRotator brickRotator;
    protected final Score score;
    private Brick heldBrick = null;
    private boolean canHold = true;

    // This represents the "Real" game state (collision checks happen here)
    protected int[][] boardMatrix;
    protected Point currentOffset;

    /**
     * Constructs a new Board with the specified dimensions.
     *
     * @param height The number of rows in the board grid.
     * @param width  The number of columns in the board grid.
     */
    public AbstractBoard(int height, int width) {
        this.width = width;
        this.height = height;
        this.boardMatrix = new int[height][width];
        this.brickGenerator = new RandomBrickGenerator();
        this.brickRotator = new BrickRotator();
        this.score = new Score();
    }

    // --- Template Methods (Refactored) ---

    /**
     * Locks the current active brick into the static board matrix.
     * <p>
     * <strong>Template Method:</strong> This method performs the standard merge operation
     * and then calls the hook {@link #onAfterMerge()} for any subclass-specific post-processing.
     * </p>
     */
    @Override
    public void mergeBrickToBackground() {
        // Common Logic: Merge into the logical board
        boardMatrix = MatrixOperations.merge(boardMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
        // Hook for subclasses
        onAfterMerge();
    }

    /**
     * Scans the board for full rows, clears them, and shifts remaining blocks down.
     * <p>
     * <strong>Template Method:</strong> This method performs the standard row clearing check
     * and then calls the hook {@link #onAfterClear()} for any subclass-specific post-processing.
     * </p>
     *
     * @return A {@link ClearRow} object containing details about the cleared lines and updated board state.
     */
    @Override
    public ClearRow clearRows() {
        // Common Logic: Check and remove rows
        ClearRow clearRow = MatrixOperations.checkRemoving(boardMatrix);
        boardMatrix = clearRow.newMatrix();
        // Hook for subclasses
        onAfterClear();
        return clearRow;
    }

    // --- Hooks ---

    /**
     * Hook method called immediately after a brick is merged into the background.
     * <p>
     * Subclasses can override this to perform additional actions (e.g., updating a secondary
     * render board or triggering effects). The default implementation does nothing.
     * </p>
     */
    protected void onAfterMerge() { }

    /**
     * Hook method called immediately after rows are cleared.
     * <p>
     * Subclasses can override this to perform additional actions (e.g., syncing a secondary
     * render board). The default implementation does nothing.
     * </p>
     */
    protected void onAfterClear() { }

    // --- Shared Movement Logic ---

    /**
     * Attempts to move the current brick down by one row.
     * @return true if successful, false if blocked.
     */
    @Override
    public boolean moveBrickDown() {
        return move(0, 1);
    }

    /**
     * Attempts to move the current brick left by one column.
     * @return true if successful, false if blocked.
     */
    @Override
    public boolean moveBrickLeft() {
        return move(-1, 0);
    }

    /**
     * Attempts to move the current brick right by one column.
     * @return true if successful, false if blocked.
     */
    @Override
    public boolean moveBrickRight() {
        return move(1, 0);
    }

    /**
     * Helper method to move the brick by a specific offset.
     *
     * @param x The x-offset (horizontal).
     * @param y The y-offset (vertical).
     * @return true if the new position is valid (no collision), false otherwise.
     */
    protected boolean move(int x, int y) {
        Point p = new Point(currentOffset);
        p.translate(x, y);
        boolean conflict = MatrixOperations.intersect(boardMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Rotates the current brick to the left (counter-clockwise).
     * @return true if rotation is valid, false if blocked.
     */
    @Override
    public boolean rotateLeftBrick() {
        NextShapeInfo nextShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(boardMatrix, nextShape.shape(), (int) currentOffset.getX(), (int) currentOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.position());
            return true;
        }
    }

    /**
     * Spawns a new brick from the generator and places it at the top center.
     * Also resets the hold ability for the turn.
     *
     * @return true if spawn is successful, false if the spawn area is blocked (Game Over).
     */
    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point((width / 2) - 2, 0);
        canHold = true; // Reset hold permission
        return MatrixOperations.intersect(boardMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    /**
     * Swaps the current active brick with the held brick.
     * <p>
     * This action is allowed only once per turn (until a piece locks).
     * If no brick is held, the current brick is stored and a new one spawns.
     * </p>
     */
    public void holdBrick() {
        if (!canHold) return;

        Brick currentBrick = brickRotator.getBrick(); // You might need to add getBrick() to BrickRotator class!

        if (heldBrick == null) {
            // First time holding: Store current, spawn next
            heldBrick = currentBrick;
            createNewBrick(); // This will pull the next brick from generator
        } else {
            // Swap: Store current, restore held
            Brick temp = heldBrick;
            heldBrick = currentBrick;
            brickRotator.setBrick(temp); // This resets rotation to 0
            currentOffset = new Point((width / 2) - 2, 0); // Reset position
        }

        canHold = false; // Disable holding until next piece spawns
    }

    /**
     * Instantly drops the brick to the lowest possible position.
     * @return The number of rows dropped.
     */
    @Override
    public int hardDrop() {
        int rowsDropped = 0;
        while (moveBrickDown()) {
            rowsDropped++;
        }
        return rowsDropped;
    }

    /**
     * Calculates the shadow (ghost piece) Y position by simulating a drop.
     * @return The Y-coordinate where the brick would land.
     */
    @Override
    public int getShadowYPosition() {
        int shadowY = (int) currentOffset.getY();
        int[][] currentShape = brickRotator.getCurrentShape();
        int currentX = (int) currentOffset.getX();
        int[][] currentMatrix = MatrixOperations.copy(boardMatrix);

        while (true) {
            int testY = shadowY + 1;
            if (MatrixOperations.intersect(currentMatrix, currentShape, currentX, testY)) {
                break;
            }
            shadowY = testY;
        }
        return shadowY;
    }

    @Override
    public Score getScore() {
        return score;
    }

    @Override
    public ViewData getViewData() {
        int[][] heldMatrix = (heldBrick != null) ? heldBrick.getShapeMatrix().getFirst() : null;

        return new ViewData(
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY(),
                brickGenerator.getNextBrick().getShapeMatrix().getFirst(),
                getShadowYPosition(),
                heldMatrix // Pass the held brick
        );
    }

    // Abstract methods to be implemented by subclasses
    @Override
    public abstract int[][] getBoardMatrix();

    @Override
    public abstract void newGame();
}