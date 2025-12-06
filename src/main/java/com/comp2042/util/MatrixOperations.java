package com.comp2042.util;

import com.comp2042.model.state.ClearRow;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class providing static methods for 2D matrix manipulation.
 * <p>
 * Contains core algorithms for collision detection, boundary checks,
 * grid merging, deep copying, and row clearing logic.
 * </p>
 */
public class MatrixOperations {


    //We don't want to instantiate this utility class
    private MatrixOperations(){

    }

    /**
     * Checks if a brick collides with existing blocks or board boundaries.
     *
     * @param matrix The game board grid.
     * @param brick  The brick shape matrix.
     * @param x      The target x-coordinate.
     * @param y      The target y-coordinate.
     * @return true if a collision is detected; false otherwise.
     */
    public static boolean intersect(final int[][] matrix, final int[][] brick, int x, int y) {
        for (int row = 0; row < brick.length; row++) {
            for (int column = 0; column < brick[row].length; column++) {
                int targetX = x + column;
                int targetY = y + row;
                if (brick[row][column] != 0 && (checkOutOfBound(matrix, targetX, targetY) || matrix[targetY][targetX] != 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    //simplified method & fixed potential error of bricks goign above board
    private static boolean checkOutOfBound(int[][] matrix, int targetX, int targetY) {
        //fixed: Added targetY < 0 check
        return targetX < 0 || targetY < 0 || targetY >= matrix.length || targetX >= matrix[targetY].length;
    }

    /**
     * Creates a deep copy of a 2D integer array.
     *
     * @param original The array to copy.
     * @return A new array with identical contents.
     */
    public static int[][] copy(int[][] original) {
        int[][] myInt = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            int[] aMatrix = original[i];
            int aLength = aMatrix.length;
            myInt[i] = new int[aLength];
            System.arraycopy(aMatrix, 0, myInt[i], 0, aLength);
        }
        return myInt;
    }

    /**
     * Merges a brick's shape into the board matrix at the specified position.
     * Returns a new matrix with the merged data (immutability).
     *
     * @param filledFields The current board state.
     * @param brick        The brick to merge.
     * @param x            The x-coordinate.
     * @param y            The y-coordinate.
     * @return A new 2D array representing the board after the merge.
     */
    public static int[][] merge(int[][] filledFields, int[][] brick, int x, int y) {
        int[][] copy = copy(filledFields);
        for (int row = 0; row < brick.length; row++) {
            for (int column = 0; column < brick[row].length; column++) {
                int targetX = x + column;
                int targetY = y + row;
                if (brick[row][column] != 0) {
                    copy[targetY][targetX] = brick[row][column];
                }
            }
        }
        return copy;
    }

    /**
     * Scans the matrix for full rows, removes them, and adds new empty rows at the top.
     * Calculates the score based on number of lines cleared.
     *
     * @param matrix The board matrix to check.
     * @return A {@link ClearRow} object containing the new matrix and stats on cleared lines.
     */
    public static ClearRow checkRemoving(final int[][] matrix) {
        int[][] tmp = new int[matrix.length][matrix[0].length];
        Deque<int[]> newRows = new ArrayDeque<>();
        List<Integer> clearedRows = new ArrayList<>();

        //identify the rows to clear
        for (int i = 0; i < matrix.length; i++) {
            int[] tmpRow = new int[matrix[i].length];
            boolean rowToClear = true;
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    rowToClear = false;
                }
                tmpRow[j] = matrix[i][j];
            }
            if (rowToClear) {
                clearedRows.add(i);
            } else {
                newRows.add(tmpRow);
            }
        }
        //Properly handle row shifting (fixed)
        int targetRow = matrix.length - 1;

        // added while loop fill from bottom with non-cleared rows
        while (!newRows.isEmpty()) {
            tmp[targetRow--] = newRows.pollLast();
        }
        //fill top rows with empty rows (zeros)
        for (int i = 0; i <= targetRow; i++) {
            tmp[i] = new int[matrix[0].length];
        }
        int scoreBonus = 50 * clearedRows.size() * clearedRows.size();
        return new ClearRow(tmp, scoreBonus,clearedRows);
    }

    /**
     * Creates a deep copy of a List of 2D arrays.
     * Used for copying Brick definition lists.
     *
     * @param list The list to copy.
     * @return A new list containing deep copies of the arrays.
     */
    public static List<int[][]> deepCopyList(List<int[][]> list){
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }
}