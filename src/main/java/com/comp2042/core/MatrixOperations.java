package com.comp2042.core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class MatrixOperations {


    //We don't want to instantiate this utility class
    private MatrixOperations(){

    }

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

    //creates a deep copy of a 2D array
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

    //removes row/line when blocks fills it up
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
        return new ClearRow(clearedRows.size(), tmp, scoreBonus);
    }

    //creates the list for the deep copy of 2D arrays
    public static List<int[][]> deepCopyList(List<int[][]> list){
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }

}
