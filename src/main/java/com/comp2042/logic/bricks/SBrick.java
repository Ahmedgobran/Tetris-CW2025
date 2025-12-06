package com.comp2042.logic.bricks;

import com.comp2042.model.BrickType;
import com.comp2042.util.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the "S" shape Tetromino (Green).
 * <p>
 * Defined as a 4x4 matrix with 2 rotation states.
 * Uses {@link BrickType#S} for its ID.
 * </p>
 */
final class SBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    public SBrick() {
        int id = BrickType.S.getID(); // Replace Magic Number "5"

        brickMatrix.add(new int[][]{
                {0, id, id, 0},
                {id, id, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, id, 0, 0},
                {0, id, id, 0},
                {0, 0, id, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}