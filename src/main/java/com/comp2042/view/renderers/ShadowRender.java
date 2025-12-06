package com.comp2042.view.renderers;

import com.comp2042.model.state.ViewData;
import com.comp2042.view.BrickColor;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;

/**
 * Handles the rendering of the "Ghost Piece" (Shadow).
 * <p>
 * The shadow shows where the active brick would land if dropped instantly.
 * This renderer updates the shadow's position based on calculations from the Model.
 * </p>
 */
public class ShadowRender {
    private static final double VERTICAL_LAYOUT_OFFSET = -42.0;

    private final Group shadowGroup;
    private final BrickColor colorMapper;
    private final int brickSize;

    /**
     * Initializes the shadow renderer.
     *
     * @param shadowGroup The JavaFX Group node to hold the shadow rectangles.
     * @param colorMapper The utility for determining shadow colors (transparent/dimmed).
     * @param brickSize   The size of each block in pixels.
     */
    public ShadowRender(Group shadowGroup, BrickColor colorMapper, int brickSize) {
        this.shadowGroup = shadowGroup;
        this.colorMapper = colorMapper;
        this.brickSize = brickSize;
        this.shadowGroup.setMouseTransparent(true);
    }

    /**
     * Updates the position and shape of the shadow.
     *
     * @param brick       The current view data containing the shadow's projected Y position.
     * @param gameLayoutX The X offset of the game board.
     * @param gameLayoutY The Y offset of the game board.
     * @param gap         The gap between grid cells.
     */
    public void updateShadow(ViewData brick, double gameLayoutX, double gameLayoutY, double gap) {
        shadowGroup.getChildren().clear();

        int shadowY = brick.shadowYPosition();
        int currentY = brick.yPosition();

        // Only show shadow if different from current position
        if (shadowY <= currentY) {
            shadowGroup.setVisible(false);
            return;
        }

        int[][] brickData = brick.brickData();
        double startX = gameLayoutX + brick.xPosition() * gap + brick.xPosition() * brickSize;
        double startY =  VERTICAL_LAYOUT_OFFSET + gameLayoutY + shadowY * gap + shadowY * brickSize;

        // Create shadow rectangles
        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                if (brickData[i][j] != 0) {
                    Rectangle rect = new Rectangle(brickSize, brickSize);
                    rect.setFill(colorMapper.getShadowColor(brickData[i][j]));
                    rect.setArcHeight(9);
                    rect.setArcWidth(9);
                    rect.setX(startX + j * (brickSize + gap));
                    rect.setY(startY + i * (brickSize + gap));
                    shadowGroup.getChildren().add(rect);
                }
            }
        }
        shadowGroup.setVisible(true);
    }

    /**
     * Hides the shadow from view.
     */
    public void hide() {
        shadowGroup.setVisible(false);
    }
}