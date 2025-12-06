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
 * This class handles the shared mechanics (e.g., gravity, moving left/right) while
 * subclasses (like {@link TetrisBoard} and {@link InvisibleBlocksBoard}) are responsible for
 * managing the board matrix and specific game rules (e.g., merging blocks, clearing rows).
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

    // shared logic between the board classes (Movement & Collision)

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
        int[][] heldMatrix = (heldBrick != null) ? heldBrick.getShapeMatrix().get(0) : null;

        return new ViewData(
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY(),
                brickGenerator.getNextBrick().getShapeMatrix().get(0),
                getShadowYPosition(),
                heldMatrix // Pass the held brick
        );
    }

    // Abstract methods to be implemented by subclasses
    @Override
    public abstract int[][] getBoardMatrix();

    @Override
    public abstract void mergeBrickToBackground();

    @Override
    public abstract ClearRow clearRows();

    @Override
    public abstract void newGame();
}