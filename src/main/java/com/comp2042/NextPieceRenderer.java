package com.comp2042;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class NextPieceRenderer{

    private static final int GRID_SIZE = 4;
    private final Rectangle[][] panel = new Rectangle[GRID_SIZE][GRID_SIZE];
    private final BrickColor colorMapper;
    private final int brickArcSize;

    public NextPieceRenderer(GridPane nextPiecePanel, BrickColor colorMapper, int brickSize, int brickArcSize) {
        this.colorMapper = colorMapper;
        this.brickArcSize = brickArcSize;

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Rectangle r = new Rectangle(brickSize, brickSize);
                r.setFill(Color.TRANSPARENT);
                r.setArcHeight(brickArcSize);
                r.setArcWidth(brickArcSize);
                panel[i][j] = r;
                nextPiecePanel.add(r, j, i);
            }
        }
    }

    public void update(int[][] nextBrickData) {
        // Clear all rectangles
        for (Rectangle[] row : panel) {
            for (Rectangle rect : row) {
                rect.setFill(Color.TRANSPARENT);
            }
        }

        // Fill in the next piece
        for (int i = 0; i < nextBrickData.length && i < GRID_SIZE; i++) {
            for (int j = 0; j < nextBrickData[i].length && j < GRID_SIZE; j++) {
                Rectangle rect = panel[i][j];
                rect.setFill(colorMapper.getFillColor(nextBrickData[i][j]));
                rect.setArcHeight(brickArcSize);
                rect.setArcWidth(brickArcSize);
            }
        }
    }
}