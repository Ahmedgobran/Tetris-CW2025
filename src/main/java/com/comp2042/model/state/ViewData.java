package com.comp2042.model.state;

import com.comp2042.util.MatrixOperations;

public record ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int shadowYPosition) {

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData) {
        this(brickData, xPosition, yPosition, nextBrickData, yPosition);
    }

    @Override
    public int[][] brickData() {
        return MatrixOperations.copy(brickData);
    }

    @Override
    public int[][] nextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }
}
