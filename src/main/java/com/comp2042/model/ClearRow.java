package com.comp2042.model;

import com.comp2042.util.MatrixOperations;

import java.util.List;

public final class ClearRow {

    private final int[][] newMatrix;
    private final int scoreBonus;
    private final List<Integer> clearedRowIndices;

    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus, List<Integer> clearedRows) {
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
        this.clearedRowIndices = clearedRows;

    }
    public int getLinesRemoved() {
        return clearedRowIndices == null ? 0 : clearedRowIndices.size();
    }

    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    public int getScoreBonus() {
        return scoreBonus;
    }

    public List<Integer> getClearedRowIndices() {return clearedRowIndices;}
}
