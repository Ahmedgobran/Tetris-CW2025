package com.comp2042.model;

import com.comp2042.controller.GuiController;
import com.comp2042.model.board.TetrisBoard;
import com.comp2042.model.event.EventSource;
import com.comp2042.model.event.MoveEvent;
import com.comp2042.model.state.DownData;
import com.comp2042.model.state.ViewData;

/**
 * Controls the logic for the "Normal Mode" game.
 * <p>
 * This controller extends {@link AbstractGameController} to implement standard scoring rules
 * and integrates with the {@link LevelManager} to increase difficulty over time.
 * </p>
 */
public class NormalModeController extends AbstractGameController {

    /**
     * Initializes the Normal Mode controller.
     * Sets up the {@link LevelManager} and binds it to the GUI for speed updates.
     *
     * @param c The GUI Controller.
     */
    public NormalModeController(GuiController c) {
        // Pass the specific board type to the parent
        super(c, new TetrisBoard(25, 11));
        // Initialize Level Logic
        LevelManager levelManager = new LevelManager();
        // Bind it to the score
        levelManager.bindScore(board.getScore().scoreProperty());
        // bind it to the gui
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
                board.getScore().add(1); // Normal mode Score
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
}