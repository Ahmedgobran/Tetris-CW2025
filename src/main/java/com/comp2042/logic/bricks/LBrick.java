package com.comp2042.logic.bricks;

import com.comp2042.util.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the "L" shape Tetromino (Orange).
 * <p>
 * Defined as a 4x4 matrix with 4 rotation states.
 * The shape resembles the letter 'L'.
 * </p>
 */
final class LBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs a new L-Brick and initializes its rotation states.
     * The shape uses color code 3 (Orange).
     */
    public LBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 3, 0},
                {3, 3, 3, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 3, 0, 0},
                {0, 3, 0, 0},
                {0, 3, 3, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {3, 3, 3, 0},
                {3, 0, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {3, 3, 0, 0},
                {0, 3, 0, 0},
                {0, 3, 0, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}