package com.comp2042.model.state;

import com.comp2042.util.MatrixOperations;

// Added int[][] heldBrickData
public record ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int shadowYPosition, int[][] heldBrickData) {

    // Update the constructor chain
    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int[][] heldBrickData) {
        this(brickData, xPosition, yPosition, nextBrickData, yPosition, heldBrickData);
    }

    @Override
    public int[][] brickData() {
        return MatrixOperations.copy(brickData);
    }

    @Override
    public int[][] nextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    public int[][] heldBrickData() {
        return heldBrickData != null ? MatrixOperations.copy(heldBrickData) : null;
    }
}