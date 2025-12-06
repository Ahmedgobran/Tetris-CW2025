package com.comp2042.logic.bricks;

import com.comp2042.model.BrickType;
import com.comp2042.util.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the "O" shape Tetromino (Yellow/Square).
 * <p>
 * Defined as a 4x4 matrix with 1 rotation state.
 * Uses {@link BrickType#O} for its ID.
 * </p>
 */
final class OBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    public OBrick() {
        int id = BrickType.O.getID(); // Replace Magic Number "4"

        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, id, id, 0},
                {0, id, id, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}