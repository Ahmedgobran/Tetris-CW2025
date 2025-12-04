package com.comp2042.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Validates the ghost Piece algorithm to make sure the shadow prediction properly matches where brick will land */

class ShadowLogicTest {

    private SimpleBoard board;

    @BeforeEach
    void setUp() {
        board = new SimpleBoard(25, 11);
        board.createNewBrick();
    }

    @Test
    void testShadowPredictionIsAccurate() {
        // get the predicted shadow y position
        int predictedShadowY = board.getShadowYPosition();

        // force the actual drop
        board.hardDrop();

        // get the actual landing y position
        int actualLandingY = board.getViewData().getyPosition();

        // they should match perfectly
        assertEquals(predictedShadowY, actualLandingY, "The piece should land exactly where the shadow predicted");
    }

    @Test
    void testShadowIsAlwaysBelowOrSameAsCurrent() {
        // The shadow should never be floating ABOVE the current piece
        int currentY = board.getViewData().getyPosition();
        int shadowY = board.getShadowYPosition();

        assertTrue(shadowY >= currentY, "Shadow Y should be >= Current Y");
    }

    @Test
    void testShadowUpdatesAfterMove() {
        // get init shadow
        int initialShadow = board.getShadowYPosition();

        // Move the piece (Left or Right)
        // ee try moving right: if it hits a wall we try left to ensure it moved
        boolean moved = board.moveBrickRight();
        if (!moved) {
            board.moveBrickLeft();
        }

        // calculate shadow again
        int newShadow = board.getShadowYPosition();

        // Force drop again to verify accuracy at the new position
        board.hardDrop();
        int actualY = board.getViewData().getyPosition();

        assertEquals(newShadow, actualY, "Shadow should remain accurate after lateral movement");
    }
}