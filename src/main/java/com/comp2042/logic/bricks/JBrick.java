package com.comp2042.logic.bricks;

import com.comp2042.util.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the "J" shape Tetromino (Blue).
 * <p>
 * Defined as a 4x4 matrix with 4 rotation states.
 * The shape resembles a reverse 'L' letter.
 * </p>
 */
final class JBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs a new J-Brick and initializes its rotation states.
     * The shape uses color code 2 (Blue).
     */
    public JBrick() {
        brickMatrix.add(new int[][]{
                {2, 0, 0, 0},
                {2, 2, 2, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 2, 2, 0},
                {0, 2, 0, 0},
                {0, 2, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {2, 2, 2, 0},
                {0, 0, 2, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 2, 0, 0},
                {0, 2, 0, 0},
                {2, 2, 0, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}
