package com.comp2042.view.renderers;

import com.comp2042.model.state.ViewData;
import com.comp2042.view.BrickColor;
import com.comp2042.view.LineClearAnimation;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * Acts as a Facade for the View layer, orchestrating all rendering sub-systems.
 * <p>
 * This class encapsulates individual renderers (Board, Active Piece, Shadow, Next Piece, Hold Piece)
 * providing a single simplified interface for the Controller to draw the entire game state.
 * This implements the Facade Design Pattern to reduce coupling between the Controller and View details.
 * </p>
 */
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

    /**
     * Initializes the renderer with the necessary layout containers.
     *
     * @param gamePanel       The main grid for the static board.
     * @param brickPanel      The pane overlay for the falling active brick.
     * @param nextPiecePanel  The pane for displaying the next upcoming brick.
     * @param holdPiecePanel  The pane for displaying the currently held brick.
     * @param boardWidth      The width of the game board in columns.
     */
    public GameRenderer(GridPane gamePanel, GridPane brickPanel, GridPane nextPiecePanel, GridPane holdPiecePanel, int boardWidth) {
        this.gamePanel = gamePanel;
        this.brickPanel = brickPanel;

        BrickColor colorMapper = new BrickColor();

        // Initialize all sub-renderers
        this.boardRenderer = new BoardRender(gamePanel, new int[25][boardWidth], colorMapper, BRICK_SIZE, BRICK_ARC_SIZE);
        this.activePieceRenderer = new ActivePieceRenderer(brickPanel, colorMapper, BRICK_SIZE);
        this.nextPieceRenderer = new NextPieceRenderer(nextPiecePanel, colorMapper, BRICK_SIZE, BRICK_ARC_SIZE);
        this.lineClearAnimation = new LineClearAnimation(gamePanel, colorMapper, BRICK_SIZE, BRICK_ARC_SIZE, boardWidth);

        // Initialize Hold Renderer (reusing NextPieceRenderer logic as it fits perfectly)
        this.holdPieceRenderer = new NextPieceRenderer(holdPiecePanel, colorMapper, BRICK_SIZE, BRICK_ARC_SIZE);

        // Setup Shadow Group (Ghost Piece layer)
        Group shadowGroup = new Group();
        if (brickPanel.getParent() instanceof Pane parent) {
            parent.getChildren().add(shadowGroup);
            shadowGroup.toBack();
        }
        this.shadowRender = new ShadowRender(shadowGroup, colorMapper, BRICK_SIZE);
    }

    /**
     * Initializes the graphical objects for a new active brick.
     * Should be called whenever a new brick spawns.
     *
     * @param brick The view data containing the new brick's structure.
     */
    public void initActivePiece(ViewData brick) {
        activePieceRenderer.initRectangles(brick.brickData());
        render(brick, true);
    }

    /**
     * Updates all visual components based on the current game state.
     * This is the main render loop method.
     *
     * @param brick         The current snapshot of game data (active piece, positions, next/hold info).
     * @param ghostEnabled  Whether to render the shadow (Ghost Piece).
     */
    public void render(ViewData brick, boolean ghostEnabled) {
        // Update Active Piece position
        activePieceRenderer.update(brick, gamePanel.getLayoutX(), gamePanel.getLayoutY());

        // Update Shadow position and visibility
        if (ghostEnabled) {
            shadowRender.updateShadow(brick, gamePanel.getLayoutX(), gamePanel.getLayoutY(), brickPanel.getVgap());
        } else {
            shadowRender.hide();
        }

        // Update Next Piece preview
        nextPieceRenderer.update(brick.nextBrickData());

        // Update Hold Piece view
        if (brick.heldBrickData() != null) {
            holdPieceRenderer.update(brick.heldBrickData());
        }
    }

    /**
     * Forces a full redraw of the static background grid (locked blocks).
     *
     * @param board The 2D array representing the board state.
     */
    public void refreshBackground(int[][] board) {
        boardRenderer.refresh(board);
    }

    /**
     * Retrieves the animation handler for line clears.
     *
     * @return The LineClearAnimation instance.
     */
    public LineClearAnimation getLineClearAnimation() {
        return lineClearAnimation;
    }
}