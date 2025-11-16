package com.comp2042.ui;

import com.comp2042.core.ViewData;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;

//Handles rendering of shadow (ghost piece) for the falling brick instead of doing directly in GuiController

public class ShadowRender {

    private final Group shadowGroup;
    private final BrickColor colorMapper;
    private final int brickSize;

    public ShadowRender(Group shadowGroup, BrickColor colorMapper, int brickSize) {
        this.shadowGroup = shadowGroup;
        this.colorMapper = colorMapper;
        this.brickSize = brickSize;
        this.shadowGroup.setMouseTransparent(true);
    }

    //updates the shadow position based on current brick data

    public void updateShadow(ViewData brick, double gameLayoutX, double gameLayoutY, double gap) {
        shadowGroup.getChildren().clear();

        int shadowY = brick.getShadowYPosition();
        int currentY = brick.getyPosition();

        //only show shadow if diff from current position
        if (shadowY <= currentY) {
            shadowGroup.setVisible(false);
            return;
        }

        int[][] brickData = brick.getBrickData();
        double startX = gameLayoutX + brick.getxPosition() * gap + brick.getxPosition() * brickSize;
        double startY = -42 + gameLayoutY + shadowY * gap + shadowY * brickSize;

        // create shadow rectangles
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

    public void hide() {
        shadowGroup.setVisible(false);
    }
}