package com.comp2042.util;

import com.comp2042.model.state.ClearRow;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/** tests methods for collision detection, boundary checks, and matrix manipulation */

class MatrixOperationsTest {

    @Test
    void testIntersect_ReturnsTrue_WhenBrickCollidesWithExistingBlock() {
        int[][] board = {
                {0, 0, 0},
                {0, 1, 0}, // Block exists at (1,1)
                {0, 0, 0}
        };
        int[][] brick = {{1}}; // 1x1 brick

        // try to place brick at (1,1), it should collide
        assertTrue(MatrixOperations.intersect(board, brick, 1, 1), "Should detect collision");
    }

    @Test
    void testIntersect_ReturnsTrue_WhenBrickIsOutOfBounds() {
        int[][] board = new int[10][10];
        int[][] brick = {{1}};

        // try to place brick at (-1, 0), out of bounds
        assertTrue(MatrixOperations.intersect(board, brick, -1, 0), "Should detect out of bounds Left");
        // try to place brick at (10, 0), out of bounds
        assertTrue(MatrixOperations.intersect(board, brick, 10, 0), "should detect out of bounds Right");
    }

    @Test
    void testIntersect_ReturnsFalse_WhenSpaceIsEmpty() {
        int[][] board = new int[5][5]; // Empty board
        int[][] brick = {{1}};

        // proper placemnt
        assertFalse(MatrixOperations.intersect(board, brick, 2, 2), "Should allow placement in empty space");
    }

    @Test
    void testCheckRemoving_ClearsFullRow() {
        // setup a 3x3 board where the bottom row is full
        int[][] board = {
                {0, 0, 0},
                {0, 1, 0},
                {1, 1, 1} //
        };

        ClearRow result = MatrixOperations.checkRemoving(board);

        // Check if logic detected 1 line
        assertEquals(1, result.getLinesRemoved(), "Should detect 1 cleared line");

        // Check if the board processed correctly (bottom row becomes empty or shifted)
        // The algorithm pushes empty lines to top, but here we just check if its cleared logic
        assertNotNull(result.newMatrix(), "Should return a new matrix");
    }

    @Test
    void testMerge_AddsBrickToBoard() {
        int[][] board = new int[3][3];
        int[][] brick = {{1}};

        int[][] newBoard = MatrixOperations.merge(board, brick, 1, 1);

        assertEquals(1, newBoard[1][1], "Board should now contain the brick value");
        assertEquals(0, newBoard[0][0], "Other cells should remain empty");
    }

    @Test
    void testClearRows_MultipleLines() {
        // Setup: Fill bottom 2 rows
        int[][] board = {
                {0, 0, 0},
                {1, 1, 1},  // Full row
                {1, 1, 1}   // Full row
        };

        ClearRow result = MatrixOperations.checkRemoving(board);
        assertEquals(2, result.getLinesRemoved(), "Should clear both full rows");
    }

    @Test
    void testClearRows_NoFullRows() {
        int[][] board = {
                {0, 0, 0},
                {1, 0, 1},  // Incomplete
                {1, 1, 0}   // Incomplete
        };

        ClearRow result = MatrixOperations.checkRemoving(board);
        assertEquals(0, result.getLinesRemoved(), "Should not clear incomplete rows");
    }

    @Test
    void testScoreBonus_ForMultipleLines() {
        // Fill 4 rows
        int[][] board = new int[10][10];
        for (int row = 6; row < 10; row++) {
            Arrays.fill(board[row], 1);
        }

        ClearRow result = MatrixOperations.checkRemoving(board);
        assertTrue(result.scoreBonus() > 0, "Should award points for clearing 4 lines");
        assertEquals(4, result.getLinesRemoved(), "Should clear all 4 rows");
    }
}