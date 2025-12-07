package com.comp2042.model.bricks;

import com.comp2042.util.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the "L" shape Tetromino (Orange).
 * <p>
 * Defined as a 4x4 matrix with 4 rotation states.
 * Uses {@link BrickType#L} for its ID.
 * </p>
 */
final class LBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    public LBrick() {
        int id = BrickType.L.getID(); // Replace Magic Number "3"

        brickMatrix.add(new int[][]{
                {0, 0, id, 0},
                {id, id, id, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, id, 0, 0},
                {0, id, 0, 0},
                {0, id, id, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {id, id, id, 0},
                {id, 0, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {id, id, 0, 0},
                {0, id, 0, 0},
                {0, id, 0, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}