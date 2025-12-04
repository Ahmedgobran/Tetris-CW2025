package com.comp2042.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** tests the Score model functionality ensuring points are added correctly and the score resets to zero */

class ScoreTest {

    @Test //Test increasing the score
    void testAdd_IncreasesScore() {
        Score score = new Score();
        score.add(10);
        assertEquals(10, score.scoreProperty().get());
        score.add(5);
        assertEquals(15, score.scoreProperty().get());
    }

    @Test //test resetting the score to 0
    void testReset_SetsScoreToZero() {
        Score score = new Score();
        score.add(50);
        score.reset();
        assertEquals(0, score.scoreProperty().get());
    }
}