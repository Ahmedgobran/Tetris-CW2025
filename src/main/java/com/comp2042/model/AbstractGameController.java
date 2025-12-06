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
 * Abstract base class for all Game Controllers, implementing the Template Method Design Pattern.
 * <p>
 * This class centralizes the shared game loop logic (movement, rotation, view refreshing) while
 * delegating specific rules (like scoring multipliers and game over conditions) to its subclasses.
 * It acts as the bridge between the UI Controller and the Board Model.
 * </p>
 */
public abstract class AbstractGameController implements InputEventListener {

    protected final Board board;
    protected final GuiController viewGuiController;

    /**
     * Initializes the game controller with a specific UI and Board implementation.
     *
     * @param c     The GUI Controller responsible for rendering the game.
     * @param board The specific Board model (e.g., TetrisBoard or InvisibleBlocksBoard).
     */

    public AbstractGameController(GuiController c, Board board) {
        this.viewGuiController = c;
        this.board = board;

        // Initialize common game setup
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
    }

    // Functionality (Identical in both modes -NormalModeController and ChallengeModeController)

    /**
     * Retrieves the current board grid.
     *
     * @return A 2D array representing the board state.
     */
    @Override
    public int[][] getBoard() {
        return board.getBoardMatrix();
    }

    /**
     * Handles the logic when the user attempts to move the brick to the left.
     *
     * @param event The move event context.
     * @return The updated view data after the move attempt.
     */
    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        refreshView();
        return board.getViewData();
    }

    /**
     * Handles the logic when the user attempts to move the brick to the right.
     *
     * @param event The move event context.
     * @return The updated view data after the move attempt.
     */
    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        refreshView();
        return board.getViewData();
    }

    /**
     * Handles the logic when the user attempts to rotate the brick.
     *
     * @param event The move event context.
     * @return The updated view data after the rotation attempt.
     */
    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        refreshView();
        return board.getViewData();
    }

    /**
     * Handles the logic when the user attempts to hold (swap) the current brick.
     *
     * @return The updated view data containing the held brick and new active brick.
     */
    @Override
    public ViewData onHoldEvent() {
        if (board instanceof AbstractBoard ab) {
            ab.holdBrick();
        }
        refreshView();
        return board.getViewData();
    }

    /**
     * Resets the game state to start a new session.
     */
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
     * Handles the logic sequence when a brick lands on the stack.
     * <p>
     * This is a Template Method that defines the strict order of operations:
     * 1. Merge brick to background.
     * 2. Clear full rows.
     * 3. Calculate Score (Hook method overridden by subclasses).
     * 4. Check for Game Over.
     * </p>
     *
     * @return DownData containing the result of the landing event (cleared rows, new score).
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

    /**
     * Handles the Game Over state.
     * Saves the current score to the high score manager and triggers the UI game over screen.
     */
    protected void handleGameOver() {
        HighScoreManager.getInstance().addScore(board.getScore().scoreProperty().get());
        viewGuiController.gameOver();
    }

    /**
     * Calculates the score to be awarded based on cleared rows.
     * Subclasses can override this to implement different scoring rules (e.g., multipliers).
     *
     * @param clearRow The result of the row clearing operation.
     * @return The score bonus to add.
     */
    protected int calculateScore(ClearRow clearRow) {
        return clearRow.scoreBonus();
    }
}