package com.comp2042.view;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the visual effects for clearing lines on the game board.
 * <p>
 * This class creates temporary "flash" rectangles over the cleared rows and
 * orchestrates a fade-in/fade-out animation sequence before the board updates.
 * </p>
 */
public class LineClearAnimation {

    private final GridPane gamePanel;
    private final BrickColor colorMapper;
    private final int brickSize;
    private final int brickArcSize;
    private final int boardWidth;
    private static final int VISIBLE_ROWS_OFFSET = 2;

    /**
     * Initializes the animation handler with board layout details.
     *
     * @param gamePanel    The GridPane where the animation overlay will be added.
     * @param colorMapper  The utility for handling colors (not directly used for the flash, but part of render context).
     * @param brickSize    The size of blocks in pixels.
     * @param brickArcSize The corner radius for blocks.
     * @param boardWidth   The number of columns in the board.
     */
    public LineClearAnimation(GridPane gamePanel, BrickColor colorMapper, int brickSize, int brickArcSize, int boardWidth) {
        this.gamePanel = gamePanel;
        this.colorMapper = colorMapper;
        this.brickSize = brickSize;
        this.brickArcSize = brickArcSize;
        this.boardWidth = boardWidth;
    }

    /**
     * Plays a flash animation on the specified rows.
     *
     * @param clearedRows The list of row indices (logical) that were cleared.
     * @param onFinished  A callback Runnable to execute when the animation completes (usually to refresh the board).
     */
    public void animateClearedRows(List<Integer> clearedRows, Runnable onFinished) {
        if (clearedRows == null || clearedRows.isEmpty()) {
            if (onFinished != null) {
                onFinished.run();
            }
            return;
        }

        List<Rectangle> flashRectangles = new ArrayList<>();

        // Create white flash rectangles for each cleared row
        for (int rowIndex : clearedRows) {
            int displayRow = rowIndex - VISIBLE_ROWS_OFFSET;
            if (displayRow < 0) continue; // Skip hidden rows

            for (int col = 0; col < boardWidth; col++) { // num of  columns
                Rectangle flash = new Rectangle(brickSize, brickSize);
                flash.setFill(Color.GHOSTWHITE);
                flash.setArcHeight(brickArcSize);
                flash.setArcWidth(brickArcSize);
                flash.setOpacity(0.0);

                gamePanel.add(flash, col, displayRow);
                flashRectangles.add(flash);
            }
        }

        // Create flash animation (fade in then fade out)
        ParallelTransition flashIn = new ParallelTransition();
        ParallelTransition flashOut = new ParallelTransition();

        for (Rectangle rect : flashRectangles) {
            // Flash in (0 -> 0.8 opacity)
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), rect);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(0.8);
            flashIn.getChildren().add(fadeIn);

            // Flash out (0.8 -> 0 opacity)
            FadeTransition fadeOut = new FadeTransition(Duration.millis(500), rect);
            fadeOut.setFromValue(0.8);
            fadeOut.setToValue(0.0);
            flashOut.getChildren().add(fadeOut);
        }

        // Sequence: Flash in -> pause -> Flash out -> cleanup
        PauseTransition pause = new PauseTransition(Duration.millis(200));

        SequentialTransition sequence = new SequentialTransition(
                flashIn,
                pause,
                flashOut
        );

        sequence.setOnFinished(event -> {
            // Remove flash rectangles
            gamePanel.getChildren().removeAll(flashRectangles);

            // Call the callback to update the board
            if (onFinished != null) {
                onFinished.run();
            }
        });

        sequence.play();
    }
}