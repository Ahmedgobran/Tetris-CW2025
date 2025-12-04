package com.comp2042.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** tests the unique mechanics of challenge mode
 *  like ensuring blocks become invisible on the render board after locking */

class InvisibleBoardTest {

    private InvisibleBlocksBoard board;

    @BeforeEach
    void setUp() {
        // Create the invisible board (Same dimensions as my SimpleBoard)
        board = new InvisibleBlocksBoard(25, 11);
        board.createNewBrick(); // Spawn the first brick
    }

    @Test
    void testNewGame_ResetsScore() {
        board.getScore().add(500);
        board.newGame();
        assertEquals(0, board.getScore().scoreProperty().get(), "Score should reset to 0 on new game");
    }

    @Test
    void testInvisibility_RenderBoardIsEmptyAfterLocking() {
        // Force the current brick to drop to the bottom
        board.hardDrop();

        // Lock it into the board
        board.mergeBrickToBackground();

        // Get the "Visible" board (what the player sees)
        int[][] visibleBoard = board.getBoardMatrix();

        // Verify it is completely empty (all zeros)
        // Even if the brick just landed the InvisibleBoard should hide it
        boolean isVisible = false;
        for (int[] row : visibleBoard) {
            for (int cell : row) {
                if (cell != 0) {
                    isVisible = true;
                    break;
                }
            }
        }

        assertFalse(isVisible, "The board should appear empty (invisible) to the player");
    }

    @Test
    void testLogicalBoard_ActuallyHasData() {
        // this test proves the brick exists in memory even if invisible

        board.hardDrop();
        board.mergeBrickToBackground();

        // this is the "real" memory of the board.
        int[][] logicalData = board.boardMatrix;

        boolean hasData = false;
        for (int[] row : logicalData) {
            for (int cell : row) {
                if (cell != 0) {
                    hasData = true;
                    break;
                }
            }
        }

        assertTrue(hasData, "The internal logic should still record the locked brick");
    }
}