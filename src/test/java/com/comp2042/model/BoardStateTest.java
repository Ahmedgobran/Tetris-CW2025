package com.comp2042.model;

import com.comp2042.model.board.TetrisBoard;
import com.comp2042.model.state.ViewData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Validates core game state: transitions, score accumulation, game resets, initial board setup */

class BoardStateTest {

    private TetrisBoard board;

    @BeforeEach
    void setUp() {
        board = new TetrisBoard(20, 10);
        board.createNewBrick();
    }

    @Test
    void testBoardInitialization() {
        // verify the board was created with the correct size
        int[][] matrix = board.getBoardMatrix();

        assertNotNull(matrix, "Board matrix should be initialized");
        assertEquals(20, matrix.length, "Board height should match constructor");
        assertEquals(10, matrix[0].length, "Board width should match constructor");
    }

    @Test
    void testScoreResetOnNewGame() {
        // Simulate playing and getting points
        board.getScore().add(500);
        assertEquals(500, board.getScore().scoreProperty().get(), "Score should update");

        // Restart the game
        board.newGame();

        // Verify score is back to zero
        assertEquals(0, board.getScore().scoreProperty().get(), "Score should reset to 0 after newGame()");
    }

    @Test
    void testHardDrop_ReturnsValidRows() {
        // Even with a random brick, hard drop should return a number >= 0
        int dropped = board.hardDrop();
        assertTrue(dropped >= 0, "Hard drop should return non-negative row count");

        // After hard drop, the brick should be at the bottom
        // We can check if it moved by checking the Y position
        ViewData view = board.getViewData();
        // Since it dropped, Y should be > 0
        assertTrue(view.yPosition() > 0, "Brick should have moved down after hard drop");
    }
}