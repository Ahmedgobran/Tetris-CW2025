package com.comp2042.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HighScoreManager {

    private static final String FILE_NAME = "highscores.txt";
    private static HighScoreManager instance;
    private List<Integer> scores;

    private HighScoreManager() {
        scores = new ArrayList<>();
        loadScores();
    }

    public static HighScoreManager getInstance() {
        if (instance == null) {
            instance = new HighScoreManager();
        }
        return instance;
    }

    public void addScore(int score) {
        scores.add(score);
        // Sort descending (High to Low)
        Collections.sort(scores, Collections.reverseOrder());

        // Keep only top 10
        if (scores.size() > 10) {
            scores.subList(10, scores.size()).clear();
        }
        saveScores();
    }

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
            Collections.sort(scores, Collections.reverseOrder());
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