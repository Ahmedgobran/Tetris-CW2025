package com.comp2042.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SimpleBoardTest {

    private SimpleBoard board;

    @BeforeEach
    void setUp() {
        // Create a standard board
        board = new SimpleBoard(25, 11);
        board.createNewBrick(); // Spawn the first brick
    }

    @Test
    void testScoreResetOnNewGame() {
        board.getScore().add(500);
        board.newGame();
        assertEquals(0, board.getScore().scoreProperty().get(), "Score should reset to 0 on new game");
    }

    @Test
    void testMoveBrickDown_ReturnsTrue_WhenPathIsClear() {
        // A new brick spawns at top, moving down should be valid
        assertTrue(board.moveBrickDown(), "Should be able to move down into empty space");
    }


    @Test
    void testMoveRight_BlockedByWall() {
        // attempt to move right 15 times (board is only 10 wide)
        for (int i = 0; i < 15; i++) {
            board.moveBrickRight();
        }

        int xAtWall = board.getViewData().getxPosition();

        // try one more move
        boolean moved = board.moveBrickRight();
        int xAfter = board.getViewData().getxPosition();

        assertFalse(moved, "Should return false when hitting the wall");
        assertEquals(xAtWall, xAfter, "X position should not increase past the wall");
    }

    @Test
    void testMoveLeft_BlockedByWall() {
        // attempt to move left 15 times
        for (int i = 0; i < 15; i++) {
            board.moveBrickLeft();
        }

        int xAtWall = board.getViewData().getxPosition();

        // Try one more move
        boolean moved = board.moveBrickLeft();
        int xAfter = board.getViewData().getxPosition();

        assertFalse(moved, "Should return false when hitting the wall");
        assertEquals(xAtWall, xAfter, "X position should not decrease past the wall");
    }

    @Test
    void testRotate_Success() {
        boolean rotated = board.rotateLeftBrick();
        assertTrue(rotated, "Should be able to rotate in open space");
    }

    @Test
    void testMergeBrick_UpdatesLogicalMatrix() {
        board.hardDrop();
        board.mergeBrickToBackground();

        // Verify the internal matrix actually saved the blocks
        int[][] matrix = board.getBoardMatrix();
        boolean hasBlocks = false;

        for (int[] row : matrix) {
            for (int cell : row) {
                if (cell != 0) {
                    hasBlocks = true;
                    break;
                }
            }
        }
        assertTrue(hasBlocks, "Merging should permanently write the brick to the board matrix");
    }

    @Test
    void testGameOver_CollisionOnSpawn() {
        // manually fill the top row to force Game Over
        int[][] matrix = board.boardMatrix;

        for (int x = 0; x < matrix[0].length; x++) {
            matrix[0][x] = 1;
        }

        // Try to spawn: should fail (Collision)
        boolean collisionDetected = board.createNewBrick();
        assertTrue(collisionDetected, "Should detect collision (Game Over) if spawn area is blocked");
    }
}