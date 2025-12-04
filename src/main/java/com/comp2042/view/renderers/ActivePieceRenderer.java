package com.comp2042.view.renderers;

import com.comp2042.model.ViewData;
import com.comp2042.view.BrickColor;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

public class ActivePieceRenderer {

    private final GridPane brickPanel;
    private final BrickColor colorMapper;
    private final int brickSize;
    private Rectangle[][] rectangles;

    public ActivePieceRenderer(GridPane brickPanel, BrickColor colorMapper, int brickSize) {
        this.brickPanel = brickPanel;
        this.colorMapper = colorMapper;
        this.brickSize = brickSize;
    }

    // Initialize the rectangles (moved from initGameView)
    public void initRectangles(int[][] brickData) {
        rectangles = new Rectangle[brickData.length][brickData[0].length];
        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                Rectangle rectangle = new Rectangle(brickSize, brickSize);
                rectangle.setArcHeight(9);
                rectangle.setArcWidth(9);
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
    }

    // Update positions and colors (moved from refreshBrick)
    public void update(ViewData brick, double gameX, double gameY) {
        // Move the whole grid panel
        brickPanel.setLayoutX(gameX + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * brickSize);
        brickPanel.setLayoutY(-42 + gameY + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * brickSize);
        // Update individual rectangle colors
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                int colorCode = brick.getBrickData()[i][j];
                rectangles[i][j].setFill(colorMapper.getFillColor(colorCode));
            }
        }
    }
}