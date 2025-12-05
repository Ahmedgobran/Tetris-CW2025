package com.comp2042.model;

import com.comp2042.controller.GuiController;
import com.comp2042.model.board.AbstractBoard;
import com.comp2042.model.board.Board;
import com.comp2042.model.event.InputEventListener;
import com.comp2042.model.event.MoveEvent;
import com.comp2042.model.state.ClearRow;
import com.comp2042.model.state.DownData;
import com.comp2042.model.state.ViewData;
import com.comp2042.util.HighScoreManager;

/**
 * Abstract parent class for all Game Controllers.
 * Handles common game logic like movement, rotation, and view updates.
 * Specific game rules (Scoring, Gravity, Game Over conditions) are left to subclasses.
 */
public abstract class AbstractGameController implements InputEventListener {

    protected final Board board;
    protected final GuiController viewGuiController;

    public AbstractGameController(GuiController c, Board board) {
        this.viewGuiController = c;
        this.board = board;

        // Initialize common game setup
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
    }

    // Functionality (Identical in both modes -GameController and GameControllerChallenge)

    @Override
    public int[][] getBoard() {
        return board.getBoardMatrix();
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        refreshView();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        refreshView();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        refreshView();
        return board.getViewData();
    }

    @Override
    public ViewData onHoldEvent() {
        if (board instanceof AbstractBoard ab) {
            ab.holdBrick();
        }
        refreshView();
        return board.getViewData();
    }

    @Override
    public void createNewGame() {
        board.newGame();
        refreshView();
    }

    /**
     * Helper to force a UI refresh.
     * Useful because Challenge mode needs constant refreshing for invisible blocks.
     */
    protected void refreshView() {
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }

    /**
     * Shared logic for what happens when a brick lands.
     * Subclasses can override this if they need specific Game Over behavior (like Challenge Mode).
     */
    protected DownData processBrickLanding() {
        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();

        if (clearRow.getLinesRemoved() > 0) {
            // Use abstract method for scoring, or default to standard
            board.getScore().add(calculateScore(clearRow));
        }

        if (board.createNewBrick()) {
            handleGameOver();
        }

        refreshView();
        return new DownData(clearRow, board.getViewData());
    }

    // --- Protected Hooks for Child Classes ---

    protected void handleGameOver() {
        HighScoreManager.getInstance().addScore(board.getScore().scoreProperty().get());
        viewGuiController.gameOver();
    }

    protected int calculateScore(ClearRow clearRow) {
        return clearRow.scoreBonus();
    }
}