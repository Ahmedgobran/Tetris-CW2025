package com.comp2042.model.state;

import com.comp2042.model.bricks.BrickRotator;
import com.comp2042.util.MatrixOperations;

/**
 * A record holding the state of a brick after a rotation calculation.
 * <p>
 * Used by the {@link BrickRotator} to return the calculated
 * matrix shape and the new rotation index.
 * </p>
 *
 * @param shape    The 2D matrix representing the rotated brick shape.
 * @param position The new rotation index (0-3).
 */
public record NextShapeInfo(int[][] shape, int position) {

    /**
     * Retrieves the shape matrix.
     * <p>
     * Returns a defensive copy to ensure the rotation logic remains immutable.
     * </p>
     *
     * @return A deep copy of the shape matrix.
     */
    @Override
    public int[][] shape() {
        return MatrixOperations.copy(shape);
    }
}