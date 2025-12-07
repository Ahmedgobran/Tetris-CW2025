package com.comp2042.model.bricks;

import java.util.List;

/**
 * Represents the blueprint for a Tetris game piece.
 * <p>
 * Implementations of this interface define the specific shape configurations
 * (rotations) for a Tetromino (e.g., I-Shape, T-Shape).
 * </p>
 */
public interface Brick {

    /**
     * Retrieves the list of rotation states for this brick.
     * <p>
     * Each state is represented by a 4x4 integer matrix, where non-zero values
     * indicate the presence of a block and its color code.
     * </p>
     *
     * @return A list of 2D integer arrays representing the brick's possible shapes.
     */
    List<int[][]> getShapeMatrix();
}