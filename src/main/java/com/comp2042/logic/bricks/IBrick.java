package com.comp2042.logic.bricks;

import com.comp2042.model.BrickType;
import com.comp2042.util.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the "I" shape Tetromino (Cyan).
 * <p>
 * Defined as a 4x4 matrix with 2 rotation states (Horizontal and Vertical).
 * Uses {@link BrickType#I} for its ID.
 * </p>
 */
public final class IBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    public IBrick() {
        int id = BrickType.I.getID(); // Replace Magic Number "1"

        // Horizontal State
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {id, id, id, id},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        // Vertical State
        brickMatrix.add(new int[][]{
                {0, 0, id, 0},
                {0, 0, id, 0},
                {0, 0, id, 0},
                {0, 0, id, 0}
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