package com.comp2042.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages the persistence and retrieval of high scores using the Singleton Design Pattern.
 * <p>
 * Handles reading from and writing to a local text file ('highscores.txt').
 * Ensures the score list is sorted in descending order and capped at a maximum size.
 * </p>
 */
public class HighScoreManager {

    private static final String FILE_NAME = "highscores.txt";
    private static HighScoreManager instance;
    private List<Integer> scores;

    private HighScoreManager() {
        scores = new ArrayList<>();
        loadScores();
    }

    /**
     * Retrieves the singleton instance of the HighScoreManager.
     * @return The singleton instance.
     */
    public static HighScoreManager getInstance() {
        if (instance == null) {
            instance = new HighScoreManager();
        }
        return instance;
    }

    /**
     * Adds a new score to the list.
     * <p>
     * The list is re-sorted in descending order, and if it exceeds 10 entries,
     * the lowest score is removed. The list is then saved to the file.
     * </p>
     *
     * @param score The score value to add.
     */
    public void addScore(int score) {
        scores.add(score);
        // Sort descending (High to Low)
        scores.sort(Collections.reverseOrder());

        // Keep only top 10
        if (scores.size() > 10) {
            scores.subList(10, scores.size()).clear();
        }
        saveScores();
    }

    /**
     * Retrieves the current list of high scores.
     * @return A list of integers representing the top scores.
     */
    public List<Integer> getScores() {
        return scores;
    }

    private void loadScores() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            scores = reader.lines()
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            scores.sort(Collections.reverseOrder());
        } catch (IOException | NumberFormatException e) {
            System.err.println("Could not load scores: " + e.getMessage());
            scores = new ArrayList<>();
        }
    }

    private void saveScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int score : scores) {
                writer.write(score + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}