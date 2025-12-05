package com.comp2042.model;

import com.comp2042.model.state.ViewData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Tests the security of the game state records,
 * confirming that arrays are copied before being exposed to prevent accidental modification. */

class DataImmutabilityTest {

    @Test
    void testViewDataReturnsCopy() {
        // Setup original data:
        int[][] originalBrick = {{1}};
        ViewData data = new ViewData(originalBrick, 0, 0, new int[0][0], 0);

        // Get the array from the record:
        int[][] retrieved = data.brickData();

        // Modify the retrieved array
        retrieved[0][0] = 99;

        // Assert the record logic protected the original data:
        // if this fails it means record is returning the raw array which is bad
        // if it passes it means its correctly returning a copy which is good
        assertNotEquals(99, data.brickData()[0][0], "Record should return a safe copy, not the internal array");
    }
}