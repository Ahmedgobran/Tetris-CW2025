package com.comp2042.model.bricks;

import com.comp2042.util.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the "Z" shape Tetromino (Red/Burlywood).
 * <p>
 * Defined as a 4x4 matrix with 2 rotation states.
 * Uses {@link BrickType#Z} for its ID.
 * </p>
 */
final class ZBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    public ZBrick() {
        int id = BrickType.Z.getID(); // Replace Magic Number "7"

        brickMatrix.add(new int[][]{
                {id, id, 0, 0},
                {0, id, id, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 0, id, 0},
                {0, id, id, 0},
                {0, id, 0, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}