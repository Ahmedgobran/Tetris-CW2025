package com.comp2042.model;

import com.comp2042.logic.bricks.IBrick;
import com.comp2042.logic.bricks.TBrick;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BrickRotatorTest {

    private BrickRotator rotator;

    @BeforeEach
    void setUp() {
        rotator = new BrickRotator();
    }

    @Test
    void testRotationCycle_IBrick() {
        // I Brick has 2 states
        rotator.setBrick(new IBrick());

        // Init state is 0 Next should be 1
        NextShapeInfo next = rotator.getNextShape();
        assertEquals(1, next.getPosition(), "I-Brick should rotate to position 1");

        // set current to 1 next should cycle back to 0
        rotator.setCurrentShape(1);
        next = rotator.getNextShape();
        assertEquals(0, next.getPosition(), "I-Brick should cycle back to 0");
    }

    @Test
    void testRotationCycle_TBrick() {
        // T Brick has 4 states
        rotator.setBrick(new TBrick());

        // o:1
        assertEquals(1, rotator.getNextShape().getPosition());

        //1:2
        rotator.setCurrentShape(1);
        assertEquals(2, rotator.getNextShape().getPosition());

        // 3:0
        rotator.setCurrentShape(3);
        assertEquals(0, rotator.getNextShape().getPosition());
    }

    @Test
    void testGetCurrentShape_ReturnsMatrix() {
        rotator.setBrick(new TBrick());
        int[][] shape = rotator.getCurrentShape();

        assertNotNull(shape, "Current shape matrix should not be null");
        assertEquals(4, shape.length, "Shape matrix should be 4x4");
    }
}