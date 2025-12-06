package com.comp2042.logic.bricks;

import com.comp2042.util.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the "I" shape Tetromino (Cyan).
 * <p>
 * Defined as a 4x4 matrix with 2 rotation states (Horizontal and Vertical).
 * This is the only piece capable of clearing 4 lines at once ("Tetris").
 * </p>
 */
public final class IBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs a new I-Brick and initializes its rotation states.
     * The shape uses color code 1 (Cyan).
     */
    public IBrick() {
        // Horizontal State
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        // Vertical State
        brickMatrix.add(new int[][]{
                {0, 0, 1, 0},
                {0, 0, 1, 0},
                {0, 0, 1, 0},
                {0, 0, 1, 0}
        });
    }

    /**
     * Retrieves the list of rotation states for this brick.
     *
     * @return A list of 4x4 integer arrays representing the I-Brick's shapes.
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}