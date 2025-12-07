package com.comp2042.model.bricks;

import com.comp2042.model.state.NextShapeInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BrickRotatorTest {

    private BrickRotator rotator;
    private Brick testBrick;

    @BeforeEach
    void setUp() {
        rotator = new BrickRotator();
        testBrick = new LBrick(); // LBrick has 4 rotation states
        rotator.setBrick(testBrick);
    }

    @Test
    void testInitialState() {
        // Should start at rotation index 0
        int[][] current = rotator.getCurrentShape();
        assertArrayEquals(testBrick.getShapeMatrix().get(0), current, "Should start at rotation 0");
    }

    @Test
    void testNextShape_CyclesCorrectly() {
        // Current is 0. Next should be 1.
        NextShapeInfo next = rotator.getNextShape();
        assertEquals(1, next.position(), "Next rotation index should be 1");

        // Update to 1
        rotator.setCurrentShape(next.position());

        // Next should be 2
        assertEquals(2, rotator.getNextShape().position());
    }

    @Test
    void testRotationWrapAround() {
        // LBrick has 4 states (0, 1, 2, 3).
        // Set current to the last state (3)
        rotator.setCurrentShape(3);

        // Next should wrap back to 0
        NextShapeInfo next = rotator.getNextShape();
        assertEquals(0, next.position(), "Rotation should wrap from last index back to 0");
    }

    @Test
    void testResetBrick_ResetsIndex() {
        rotator.setCurrentShape(2);
        rotator.setBrick(new LBrick()); // Set new brick

        int[][] current = rotator.getCurrentShape();
        // Should be back at index 0 of the new brick
        assertNotNull(current);
        // We verify via behavior: calling getNextShape should return 1, not 3
        assertEquals(1, rotator.getNextShape().position(), "Setting a new brick should reset rotation index to 0");
    }
}