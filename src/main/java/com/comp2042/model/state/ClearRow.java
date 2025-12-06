package com.comp2042.model.state;

import com.comp2042.util.MatrixOperations;

import java.util.List;

/**
 * A record representing the result of a row-clearing operation on the board.
 * <p>
 * Contains the updated board matrix, the score bonus earned from the clear,
 * and the indices of the rows that were removed.
 * </p>
 *
 * @param newMatrix         The updated board grid after rows are removed and blocks shifted.
 * @param scoreBonus        The calculated score points for this specific clear event.
 * @param clearedRowIndices The list of row indices (0-based) that were cleared.
 */
public record ClearRow(int[][] newMatrix, int scoreBonus, List<Integer> clearedRowIndices) {

    /**
     * Helper method to count how many lines were removed.
     *
     * @return The number of cleared lines (0 if none).
     */
    public int getLinesRemoved() {
        return clearedRowIndices == null ? 0 : clearedRowIndices.size();
    }

    /**
     * Retrieves the new board matrix.
     * <p>
     * Returns a defensive copy to prevent external modification of the game state.
     * </p>
     *
     * @return A deep copy of the 2D integer array.
     */
    @Override
    public int[][] newMatrix() {
        return MatrixOperations.copy(newMatrix);
    }
}