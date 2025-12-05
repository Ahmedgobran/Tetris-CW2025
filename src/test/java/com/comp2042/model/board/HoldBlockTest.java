package com.comp2042.model.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HoldBlockTest {

    private TetrisBoard board;

    @BeforeEach
    void setUp() {
        // Use the standard constructor (Random generation is fine for these tests)
        board = new TetrisBoard(20, 10);
        board.createNewBrick();
    }

    @Test
    void testInitialHold_MovesActiveToHold() {
        // 1. Capture the current active brick
        int[][] initialActive = board.getViewData().brickData();

        // Pre-condition: Hold should be empty
        assertNull(board.getViewData().heldBrickData(), "Held brick should be empty initially");

        // 2. Action: Press Hold
        board.holdBrick();

        // 3. Verify the held brick is what was previously active
        int[][] heldAfter = board.getViewData().heldBrickData();

        assertNotNull(heldAfter, "Held brick should now exist");
        assertArrayEquals(initialActive, heldAfter, "The active brick should have moved to the hold slot");
    }

    @Test
    void testSwap_SwapsActiveAndHeld() {
        // 1. Perform first hold to fill the slot
        board.holdBrick();

        // Force a new turn to reset the 'canHold' permission
        board.hardDrop();
        board.mergeBrickToBackground();
        board.createNewBrick();

        // 2. Capture state before the second swap
        int[][] heldBefore = board.getViewData().heldBrickData();
        int[][] activeBefore = board.getViewData().brickData();

        // 3. Action: Swap again
        board.holdBrick();

        // 4. Verify Swap
        int[][] activeAfter = board.getViewData().brickData();
        int[][] heldAfter = board.getViewData().heldBrickData();

        // The old 'Held' brick should now be 'Active'
        assertArrayEquals(heldBefore, activeAfter, "The previously held brick should become active");

        // The old 'Active' brick should now be 'Held'
        assertArrayEquals(activeBefore, heldAfter, "The previously active brick should be held");
    }

    @Test
    void testPreventDoubleHold() {
        // 1. Hold once -> Should work
        board.holdBrick();
        int[][] firstHoldData = board.getViewData().heldBrickData();

        // 2. Try to Hold again immediately -> Should FAIL (do nothing)
        board.holdBrick();

        // Verify the held piece did not change (swap didn't happen)
        int[][] secondHoldData = board.getViewData().heldBrickData();

        assertArrayEquals(firstHoldData, secondHoldData, "Should not allow holding twice in one turn");
    }

    @Test
    void testHoldRefreshesAfterLock() {
        // 1. Hold once (Uses up permission)
        board.holdBrick();

        // 2. Lock the current piece (Ends turn)
        board.hardDrop();
        board.mergeBrickToBackground();
        board.createNewBrick(); // Spawns new, Resets permission

        // 3. Try to Hold again -> Should work now
        int[][] activeBefore = board.getViewData().brickData();

        board.holdBrick();

        int[][] heldAfter = board.getViewData().heldBrickData();
        assertArrayEquals(activeBefore, heldAfter, "Should allow holding again after a piece locks");
    }
}