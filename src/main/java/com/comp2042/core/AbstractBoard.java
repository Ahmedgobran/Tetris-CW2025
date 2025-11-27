package com.comp2042.core;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import java.awt.Point;

public abstract class AbstractBoard implements Board {

    protected final int width;
    protected final int height;
    protected final BrickGenerator brickGenerator;
    protected final BrickRotator brickRotator;
    protected final Score score;

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
        boolean conflict = MatrixOperations.intersect(boardMatrix, nextShape.getShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }

    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point((width / 2) - 2, 0);
        return MatrixOperations.intersect(boardMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
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
        return new ViewData(brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY(), brickGenerator.getNextBrick().getShapeMatrix().get(0), getShadowYPosition());
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