package com.comp2042.logic.bricks;

import com.comp2042.util.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the "Z" shape Tetromino (Red/Burlywood).
 * <p>
 * Defined as a 4x4 matrix with 2 rotation states.
 * </p>
 */
final class ZBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs a new Z-Brick and initializes its rotation states.
     * The shape uses color code 7.
     */
    public ZBrick() {
        brickMatrix.add(new int[][]{
                {7, 7, 0, 0},
                {0, 7, 7, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
                });
        brickMatrix.add(new int[][]{
                {0, 0, 7, 0},
                {0, 7, 7, 0},
                {0, 7, 0, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}