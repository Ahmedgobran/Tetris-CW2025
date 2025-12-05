package com.comp2042.view.renderers;

import com.comp2042.model.state.ViewData;
import com.comp2042.view.BrickColor;
import com.comp2042.view.LineClearAnimation;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class GameRenderer {

    private final BoardRender boardRenderer;
    private final ActivePieceRenderer activePieceRenderer;
    private final NextPieceRenderer nextPieceRenderer;
    private final ShadowRender shadowRender;
    private final LineClearAnimation lineClearAnimation;
    private final NextPieceRenderer holdPieceRenderer;

    // Layout references needed for positioning
    private final GridPane gamePanel;
    private final GridPane brickPanel;

    private static final int BRICK_SIZE = 20;
    private static final int BRICK_ARC_SIZE = 9;

    public GameRenderer(GridPane gamePanel, GridPane brickPanel, GridPane nextPiecePanel, GridPane holdPiecePanel, int boardWidth) {
        this.gamePanel = gamePanel;
        this.brickPanel = brickPanel;

        BrickColor colorMapper = new BrickColor();

        // Initialize all sub-renderers
        this.boardRenderer = new BoardRender(gamePanel, new int[25][boardWidth], colorMapper, BRICK_SIZE, BRICK_ARC_SIZE); // Temp empty board init
        this.activePieceRenderer = new ActivePieceRenderer(brickPanel, colorMapper, BRICK_SIZE);
        this.nextPieceRenderer = new NextPieceRenderer(nextPiecePanel, colorMapper, BRICK_SIZE, BRICK_ARC_SIZE);
        this.lineClearAnimation = new LineClearAnimation(gamePanel, colorMapper, BRICK_SIZE, BRICK_ARC_SIZE, boardWidth);
        // Initialize Hold Renderer (using same logic as Next Piece)
        this.holdPieceRenderer = new NextPieceRenderer(holdPiecePanel, colorMapper, BRICK_SIZE, BRICK_ARC_SIZE);

        // Setup Shadow Group
        Group shadowGroup = new Group();
        if (brickPanel.getParent() instanceof Pane parent) {
            parent.getChildren().add(shadowGroup);
            shadowGroup.toBack();
        }
        this.shadowRender = new ShadowRender(shadowGroup, colorMapper, BRICK_SIZE);
    }

    public void initActivePiece(ViewData brick) {
        activePieceRenderer.initRectangles(brick.brickData());
        render(brick, true);
    }

    public void render(ViewData brick, boolean ghostEnabled) {
        // Update Active Piece
        activePieceRenderer.update(brick, gamePanel.getLayoutX(), gamePanel.getLayoutY());

        // Update Shadow
        if (ghostEnabled) {
            shadowRender.updateShadow(brick, gamePanel.getLayoutX(), gamePanel.getLayoutY(), brickPanel.getVgap());
        } else {
            shadowRender.hide();
        }
        // Update Next Piece
        nextPieceRenderer.update(brick.nextBrickData());
        // Update Hold Piece
        if (brick.heldBrickData() != null) {
            holdPieceRenderer.update(brick.heldBrickData());
        }
    }

    public void refreshBackground(int[][] board) {
        boardRenderer.refresh(board);
    }

    public LineClearAnimation getLineClearAnimation() {
        return lineClearAnimation;
    }
}