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

    public AbstractBoard(int height, int width) {
        this.width = width;
        this.height = height;
        this.boardMatrix = new int[height][width];
        this.brickGenerator = new RandomBrickGenerator();
        this.brickRotator = new BrickRotator();
        this.score = new Score();
    }

    // shared logic between the board classes (Movement & Collision)

    @Override
    public boolean moveBrickDown() {
        return move(0, 1);
    }

    @Override
    public boolean moveBrickLeft() {
        return move(-1, 0);
    }

    @Override
    public boolean moveBrickRight() {
        return move(1, 0);
    }

    // Helper method to reduce duplication in move methods
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

    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point((width / 2) - 2, 0);
        canHold = true; // Reset hold permission
        return MatrixOperations.intersect(boardMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    // hold logic
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

    @Override
    public int hardDrop() {
        int rowsDropped = 0;
        while (moveBrickDown()) {
            rowsDropped++;
        }
        return rowsDropped;
    }

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

    // abstract methods (To be implemented by subclasses)
    // These methods differ between Simpleboard.java and invisibleblocksboard.java
    @Override
    public abstract int[][] getBoardMatrix();

    @Override
    public abstract void mergeBrickToBackground();

    @Override
    public abstract ClearRow clearRows();

    @Override
    public abstract void newGame();
}