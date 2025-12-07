package com.comp2042.controller.gamemode;

import com.comp2042.controller.GameViewController;
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
 * This class serves as the controller in the MVC architecture, mediating user input from the
 * {@link GameViewController} and game logic in the {@link Board}. It handles common actions like
 * movement and view refreshing, while delegating specific scoring and game-over rules to subclasses.
 * </p>
 * <p>
 * It utilizes Dependency Injection to manage the {@link HighScoreManager}, avoiding global state access.
 * </p>
 */
public abstract class AbstractGameController implements InputEventListener {

    protected final Board board;
    protected final GameViewController viewGameViewController;
    protected final HighScoreManager highScoreManager;

    /**
     * Initializes the game controller with necessary dependencies.
     * <p>
     * Sets up the initial game state, registers this controller as the input listener for the GUI,
     * and binds the score property to the UI labels.
     * </p>
     *
     * @param c                 The GUI Controller responsible for rendering.
     * @param board             The specific Board model (e.g., TetrisBoard or InvisibleBlocksBoard).
     * @param highScoreManager  The injected service for saving high scores.
     */
    public AbstractGameController(GameViewController c, Board board, HighScoreManager highScoreManager) {
        this.viewGameViewController = c;
        this.board = board;
        this.highScoreManager = highScoreManager;

        // Initialize common game setup
        board.createNewBrick();
        viewGameViewController.setEventListener(this);
        viewGameViewController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGameViewController.bindScore(board.getScore().scoreProperty());
    }

    // Functionality (Identical in both modes -NormalModeController and ChallengeModeController)

    /**
     * Retrieves the current configuration of the game board.
     *
     * @return A 2D integer array representing the grid of blocks.
     */
    @Override
    public int[][] getBoard() {
        return board.getBoardMatrix();
    }

    /**
     * Handles the request to move the active brick to the left.
     *
     * @param event The move event details.
     * @return A {@link ViewData} object representing the updated state of the board.
     */
    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        refreshView();
        return board.getViewData();
    }

    /**
     * Handles the request to move the active brick to the right.
     *
     * @param event The move event details.
     * @return A {@link ViewData} object representing the updated state of the board.
     */
    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        refreshView();
        return board.getViewData();
    }

    /**
     * Handles the request to rotate the active brick.
     *
     * @param event The move event details.
     * @return A {@link ViewData} object representing the updated state of the board.
     */
    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        refreshView();
        return board.getViewData();
    }

    /**
     * Handles the request to hold (swap) the current brick.
     *
     * @return A {@link ViewData} object containing the updated board state and held brick info.
     */
    @Override
    public ViewData onHoldEvent() {
        // If the holdBrick method was pulled up to AbstractBoard, we can cast freely
        if (board instanceof com.comp2042.model.board.AbstractBoard ab) {
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

    // --- Helper Methods ---

    /**
     * Helper method to force a UI refresh of the background grid.
     */
    protected void refreshView() {
        viewGameViewController.refreshGameBackground(board.getBoardMatrix());
    }

    /**
     * Orchestrates the logic sequence when a brick lands on the stack.
     * <p>
     * This method performs the following steps:
     * </p>
     * <ol>
     * <li>Merges the active brick into the background grid.</li>
     * <li>Checks for and clears any full rows.</li>
     * <li>Calculates and awards score points (via {@link #calculateScore(ClearRow)}).</li>
     * <li>Checks if a new brick can be spawned; triggers Game Over if not.</li>
     * </ol>
     *
     * @return A {@link DownData} object containing the result of the landing event (cleared rows, new score).
     */
    protected DownData processBrickLanding() {
        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();

        if (clearRow.getLinesRemoved() > 0) {
            board.getScore().add(calculateScore(clearRow));
        }

        if (board.createNewBrick()) {
            handleGameOver();
        }

        refreshView();
        return new DownData(clearRow, board.getViewData());
    }

    /**
     * Handles the Game Over state.
     * <p>
     * Saves the current score using the injected {@link HighScoreManager} and
     * triggers the UI to display the Game Over screen.
     * </p>
     */
    protected void handleGameOver() {
        highScoreManager.addScore(board.getScore().scoreProperty().get());
        viewGameViewController.gameOver();
    }

    /**
     * Calculates the score to be awarded based on cleared rows.
     * <p>
     * This is a hook method that subclasses can override to implement different
     * scoring rules (e.g., score multipliers in Challenge Mode).
     * </p>
     *
     * @param clearRow The result of the row clearing operation.
     * @return The score bonus to add.
     */
    protected int calculateScore(ClearRow clearRow) {
        return clearRow.scoreBonus();
    }
}