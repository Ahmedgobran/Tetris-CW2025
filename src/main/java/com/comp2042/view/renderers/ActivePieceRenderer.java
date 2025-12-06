package com.comp2042.view.renderers;

import com.comp2042.model.state.ViewData;
import com.comp2042.view.BrickColor;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

/**
 * Responsible for rendering the currently falling "Active" brick.
 * <p>
 * Unlike the static board, this renderer moves a set of existing rectangles
 * around the screen to improve performance, rather than recreating them every frame.
 * </p>
 */
public class ActivePieceRenderer {

    private static final double VERTICAL_LAYOUT_OFFSET = -42.0;
    private final GridPane brickPanel;
    private final BrickColor colorMapper;
    private final int brickSize;
    private Rectangle[][] rectangles;

    /**
     * Constructs the renderer.
     *
     * @param brickPanel  The JavaFX container where the brick will be drawn.
     * @param colorMapper The utility to convert brick IDs to JavaFX colors.
     * @param brickSize   The size of each block in pixels.
     */
    public ActivePieceRenderer(GridPane brickPanel, BrickColor colorMapper, int brickSize) {
        this.brickPanel = brickPanel;
        this.colorMapper = colorMapper;
        this.brickSize = brickSize;
    }

    /**
     * Initializes the pool of Rectangle objects used to visualize the brick.
     *
     * @param brickData The 2D array structure of the new brick.
     */
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

    /**
     * Updates the position and color of the active brick on the screen.
     *
     * @param brick The current state of the brick (coordinates and shape).
     * @param gameX The absolute X position of the game board container.
     * @param gameY The absolute Y position of the game board container.
     */
    public void update(ViewData brick, double gameX, double gameY) {
        // Move the whole grid panel to match the logic position
        brickPanel.setLayoutX(gameX + brick.xPosition() * brickPanel.getVgap() + brick.xPosition() * brickSize);
        brickPanel.setLayoutY(VERTICAL_LAYOUT_OFFSET + gameY + brick.yPosition() * brickPanel.getHgap() + brick.yPosition() * brickSize);

        // Update individual rectangle colors
        for (int i = 0; i < brick.brickData().length; i++) {
            for (int j = 0; j < brick.brickData()[i].length; j++) {
                int colorCode = brick.brickData()[i][j];
                rectangles[i][j].setFill(colorMapper.getFillColor(colorCode));
            }
        }
    }
}