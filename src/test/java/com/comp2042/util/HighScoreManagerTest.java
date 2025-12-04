package com.comp2042.util;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class HighScoreManagerTest {

    @Test
    void testScoresAreSorted() {
        HighScoreManager manager = HighScoreManager.getInstance();

        // add score in random order
        manager.addScore(100);
        manager.addScore(500);
        manager.addScore(50);

        List<Integer> scores = manager.getScores();

        // check descending Order
        assertTrue(scores.get(0) >= scores.get(1), "First score should be highest");
    }

    @Test
    void testScoreLimit() {
        // if we add 15 scores, it should only keep 10 bcoz thats the max
        HighScoreManager manager = HighScoreManager.getInstance();
        for(int i=0; i<15; i++) {
            manager.addScore(i * 10);
        }

        assertTrue(manager.getScores().size() <= 10, "Should store max 10 scores");
    }
}