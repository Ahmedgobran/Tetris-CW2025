package com.comp2042.model.state;

import com.comp2042.util.MatrixOperations;

import java.util.List;

public record ClearRow(int[][] newMatrix, int scoreBonus, List<Integer> clearedRowIndices) {

    public int getLinesRemoved() {
        return clearedRowIndices == null ? 0 : clearedRowIndices.size();
    }

    @Override
    public int[][] newMatrix() {
        return MatrixOperations.copy(newMatrix);
    }
}
