package com.comp2042.model;

import com.comp2042.controller.GuiController;
import com.comp2042.model.board.TetrisBoard;
import com.comp2042.model.event.EventSource;
import com.comp2042.model.event.MoveEvent;
import com.comp2042.model.state.DownData;
import com.comp2042.model.state.ViewData;
import com.comp2042.util.HighScoreManager;
import com.comp2042.model.state.ClearRow;


/**
 * Controls the logic for the "Normal Mode" game.
 * <p>
 * This controller extends {@link AbstractGameController} to implement standard scoring rules
 * and integrates with the {@link LevelManager} to increase difficulty over time.
 * </p>
 */
public class NormalModeController extends AbstractGameController {

    private final LevelManager levelManager;
    /**
     * Initializes the Normal Mode controller.
     * Sets up the {@link LevelManager} and binds it to the GUI for speed updates.
     *
     * @param c The GUI Controller.
     */
    public NormalModeController(GuiController c, HighScoreManager highScoreManager) {
        // Pass highScoreManager to parent
        super(c, new TetrisBoard(25 ,11), highScoreManager);
        this.levelManager = new LevelManager();
        this.levelManager.bindScore(board.getScore().scoreProperty());
        c.bindLevel(levelManager);
    }

    /**
     * Handles the brick moving down.
     * Supports "Instant Lock" if the brick hits the bottom shadow position.
     *
     * @param event The move event containing the source (User or Timer).
     * @return DownData containing the result of the move.
     */
    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();

        // instant lock
        if (canMove) {
            ViewData currentView = board.getViewData();
            if (currentView.yPosition() == currentView.shadowYPosition()) {
                return processBrickLanding();
            }
        }
        if (!canMove) {
            return processBrickLanding();
        } else {
            // Standard scoring for soft drop
            if (event.eventSource() == EventSource.USER) {
                board.getScore().add(levelManager.getCurrentLevel());
            }
            return new DownData(null, board.getViewData());
        }
    }

    /**
     * Handles the Hard Drop event.
     * Adds extra points (2x rows dropped) for a hard drop in Normal Mode.
     *
     * @return DownData containing the landing result.
     */
    @Override
    public DownData onHardDropEvent() {
        int rowsDropped = board.hardDrop();
        if (rowsDropped > 0) {
            board.getScore().add(rowsDropped * 2); // Normal mode Score
        }
        return processBrickLanding();
    }

    /**
     * Calculates the score for cleared rows with a level-based multiplier.
     * <p>
     * Overrides the default scoring logic to reward players for surviving at higher speeds.
     * The formula used is: {@code Base_Score * Current_Level}.
     * For example, clearing a line (100 pts) at Level 5 awards 500 points.
     * </p>
     *
     * @param clearRow The result of the row clearing operation containing lines removed.
     * @return The total score to be added to the player's current score.
     */
    @Override
    protected int calculateScore(ClearRow clearRow) {
        // Get the standard score (e.g., 100, 300, 1200)
        int baseScore = super.calculateScore(clearRow);
        // Get the multiplier (Level 1 = 1x, Level 5 = 5x)
        int currentLevel = levelManager.levelProperty().get();
        return baseScore * currentLevel;
    }
}