package com.comp2042.logic.bricks;

import com.comp2042.util.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the "S" shape Tetromino (Green).
 * <p>
 * Defined as a 4x4 matrix with 2 rotation states.
 * </p>
 */
final class SBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs a new S-Brick and initializes its rotation states.
     * The shape uses color code 5 (Green).
     */
    public SBrick() {
        brickMatrix.add(new int[][]{
                {0, 5, 5, 0},
                {5, 5, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 5, 0, 0},
                {0, 5, 5, 0},
                {0, 0, 5, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}
