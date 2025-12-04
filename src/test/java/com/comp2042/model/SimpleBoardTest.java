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
    void testNewGame_ResetsScore() {
        board.getScore().add(100);
        board.newGame();
        assertEquals(0, board.getScore().scoreProperty().get(), "Score should reset to 0 on new game");
    }

    @Test
    void testMoveBrickDown_ReturnsTrue_WhenPathIsClear() {
        // A new brick spawns at top, moving down should be valid
        assertTrue(board.moveBrickDown(), "Should be able to move down into empty space");
    }

    @Test
    void testMoveBrickLeft_ReturnsTrue_WhenPathIsClear() {
        // Assuming spawn is centered, moving left should be valid
        assertTrue(board.moveBrickLeft(), "Should be able to move left");
    }

    @Test
    void testBoardMatrix_IsNotNull() {
        assertNotNull(board.getBoardMatrix(), "Board matrix should be initialized");
        assertEquals(11, board.getBoardMatrix()[0].length, "Board width should be 11");
        assertEquals(25, board.getBoardMatrix().length, "Board height should be 25");
    }
}