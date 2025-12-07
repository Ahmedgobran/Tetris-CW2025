package com.comp2042.model.bricks;


import com.comp2042.model.bricks.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Verifies the structural integrity of brick definitions ensuring each shape
 * has the correct matrix dimensions and rotation states
 *  (replaced this with BrickRotatorTest since only rotated 2 blocks) */

class BrickTest {

    @Test
    void testIBrick_HasTwoRotations() {
        // The I-Piece usually flips between Horizontal and Vertical (2 states)
        Brick brick = new IBrick();
        assertNotNull(brick.getShapeMatrix(), "Brick shape matrix should not be null");
        assertEquals(2, brick.getShapeMatrix().size(), "I-Brick should have 2 rotation states");
    }

    @Test
    void testTBrick_HasFourRotations() {
        // The T-Piece can point Up, Down, Left, Right (4 states)
        Brick brick = new TBrick();
        assertEquals(4, brick.getShapeMatrix().size(), "T-Brick should have 4 rotation states");
    }

    @Test
    void testOBrick_IsStable() {
        // The Square (O-Piece) doesn't effectively rotate (1 state logic)
        Brick brick = new OBrick();
        assertFalse(brick.getShapeMatrix().isEmpty(), "O-Brick must have at least one state");
        // Verify it is a 2x2 block (in a 4x4 matrix usually) or checking specific bits
        int[][] shape = brick.getShapeMatrix().getFirst();
        assertNotEquals(0, shape.length, "Shape matrix should have rows");
    }

    @Test
    void testBrickMatrixSize() {
        // All your bricks seem to use a 4x4 grid standard
        Brick brick = new LBrick();
        int[][] matrix = brick.getShapeMatrix().getFirst();

        assertEquals(4, matrix.length, "Brick matrix height should be 4");
        assertEquals(4, matrix[0].length, "Brick matrix width should be 4");
    }
}