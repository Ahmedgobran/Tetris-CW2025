package com.comp2042.model.state;

import com.comp2042.util.MatrixOperations;

public record NextShapeInfo(int[][] shape, int position) {

    @Override
    public int[][] shape() {
        return MatrixOperations.copy(shape);
    }
}
