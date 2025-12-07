package com.comp2042.model.bricks;

import com.comp2042.util.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the "J" shape Tetromino (Blue).
 * <p>
 * Defined as a 4x4 matrix with 4 rotation states.
 * Uses {@link BrickType#J} for its ID.
 * </p>
 */
final class JBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    public JBrick() {
        int id = BrickType.J.getID(); // Replace Magic Number "2"

        brickMatrix.add(new int[][]{
                {id, 0, 0, 0},
                {id, id, id, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, id, id, 0},
                {0, id, 0, 0},
                {0, id, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {id, id, id, 0},
                {0, 0, id, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, id, 0, 0},
                {0, id, 0, 0},
                {id, id, 0, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}