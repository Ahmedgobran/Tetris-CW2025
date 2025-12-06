package com.comp2042.view.renderers;

import com.comp2042.view.BrickColor;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Handles the rendering of the static game board (the background grid).
 * <p>
 * This class maintains a grid of Rectangle objects representing the board cells
 * and updates their colors based on the logical board state.
 * </p>
 */
public class BoardRender {

    private static final int VISIBLE_ROWS_OFFSET = 2;
    private final Rectangle[][] matrix;
    private final BrickColor colorMapper;
    private final int brickArcSize;

    /**
     * Initializes the board renderer and populates the grid with empty rectangles.
     *
     * @param panel        The GridPane to populate.
     * @param board        The initial board data (used for dimensions).
     * @param colorMapper  The color mapping utility.
     * @param brickSize    The size of each block in pixels.
     * @param brickArcSize The corner radius for the blocks.
     */
    public BoardRender(GridPane panel, int[][] board, BrickColor colorMapper, int brickSize, int brickArcSize) {
        this.colorMapper = colorMapper;
        this.brickArcSize = brickArcSize;
        matrix = new Rectangle[board.length][board[0].length];

        for (int i = VISIBLE_ROWS_OFFSET; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Rectangle r = new Rectangle(brickSize, brickSize);
                r.setFill(Color.TRANSPARENT);
                matrix[i][j] = r;
                panel.add(r, j, i - VISIBLE_ROWS_OFFSET);
            }
        }
    }

    /**
     * Refreshes the board visual to match the logical state.
     *
     * @param board The 2D array containing the current colors of the board blocks.
     */
    public void refresh(int[][] board) {
        for (int i = VISIBLE_ROWS_OFFSET; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Rectangle rect = matrix[i][j];
                rect.setFill(colorMapper.getFillColor(board[i][j]));
                rect.setArcHeight(brickArcSize);
                rect.setArcWidth(brickArcSize);
            }
        }
    }
}