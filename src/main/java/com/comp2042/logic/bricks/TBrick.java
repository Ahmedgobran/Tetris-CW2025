package com.comp2042.logic.bricks;

import com.comp2042.model.BrickType;
import com.comp2042.util.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the "T" shape Tetromino (Purple).
 * <p>
 * Defined as a 4x4 matrix with 4 rotation states.
 * Uses {@link BrickType#T} for its ID.
 * </p>
 */
public final class TBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    public TBrick() {
        int id = BrickType.T.getID(); // Replace Magic Number "6"

        brickMatrix.add(new int[][]{
                {0, id, 0, 0},
                {id, id, id, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, id, 0, 0},
                {0, id, id, 0},
                {0, id, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {id, id, id, 0},
                {0, id, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, id, 0, 0},
                {id, id, 0, 0},
                {0, id, 0, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}