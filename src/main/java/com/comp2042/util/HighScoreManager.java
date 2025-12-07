package com.comp2042.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages the persistence and retrieval of high scores.
 * <p>
 * This class handles reading from and writing to a local text file.
 * It ensures the score list is always sorted in descending order and capped at a maximum size (Top 10).
 * </p>
 */
public class HighScoreManager {

    // Default file name for the main application
    private static final String DEFAULT_FILE_NAME = "highscores.txt";

    private final String fileName;
    private List<Integer> scores;

    /**
     * Default constructor for the main application.
     * Uses "highscores.txt".
     */
    public HighScoreManager() {
        this(DEFAULT_FILE_NAME);
    }

    /**
     * Constructor for testing or custom files.
     * * @param fileName The name of the file to store scores in.
     */
    public HighScoreManager(String fileName) {
        this.fileName = fileName;
        this.scores = new ArrayList<>();
        loadScores();
    }

    /**
     * Adds a new score to the list and persists the updated list to the file.
     * <p>
     * The list is re-sorted in descending order (highest score first). If the list
     * exceeds 10 entries after the addition, the lowest score is removed to maintain
     * the "Top 10" limit.
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
     *
     * @return A list of integers representing the top scores, sorted descending.
     */
    public List<Integer> getScores() {
        return scores;
    }

    private void loadScores() {
        File file = new File(fileName);
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int score : scores) {
                writer.write(score + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}