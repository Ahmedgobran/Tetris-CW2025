package com.comp2042.view.renderers;

import com.comp2042.view.BrickColor;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class NextPieceRenderer {

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

        // Calculate offsets to center the piece
        int[] offsets = calculateCenterOffset(nextBrickData);
        int offsetRow = offsets[0];
        int offsetCol = offsets[1];

        // Fill in the next piece with centering offset
        for (int i = 0; i < nextBrickData.length && i < GRID_SIZE; i++) {
            for (int j = 0; j < nextBrickData[i].length && j < GRID_SIZE; j++) {
                if (nextBrickData[i][j] != 0) {
                    int displayRow = i + offsetRow;
                    int displayCol = j + offsetCol;

                    // Check bounds before drawing
                    if (displayRow >= 0 && displayRow < GRID_SIZE &&
                            displayCol >= 0 && displayCol < GRID_SIZE) {
                        Rectangle rect = panel[displayRow][displayCol];
                        rect.setFill(colorMapper.getFillColor(nextBrickData[i][j]));
                        rect.setArcHeight(brickArcSize);
                        rect.setArcWidth(brickArcSize);
                    }
                }
            }
        }
    }
    private int[] calculateCenterOffset(int[][] brickData) {
        int minRow = GRID_SIZE, maxRow = -1;
        int minCol = GRID_SIZE, maxCol = -1;

        // Find the bounding box of the actual brick (non-zero cells)
        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                if (brickData[i][j] != 0) {
                    minRow = Math.min(minRow, i);
                    maxRow = Math.max(maxRow, i);
                    minCol = Math.min(minCol, j);
                    maxCol = Math.max(maxCol, j);
                }
            }
        }

        // If no cells found, no offset needed
        if (maxRow == -1) {
            return new int[]{0, 0};
        }

        // Calculate brick dimensions
        int brickHeight = maxRow - minRow + 1;
        int brickWidth = maxCol - minCol + 1;

        // Calculate offset to center in GRID_SIZE x GRID_SIZE panel
        int offsetRow = (GRID_SIZE - brickHeight) / 2 - minRow;
        int offsetCol = (GRID_SIZE - brickWidth) / 2 - minCol;

        return new int[]{offsetRow, offsetCol};
    }
}