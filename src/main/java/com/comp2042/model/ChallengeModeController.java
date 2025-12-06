package com.comp2042.model;

import com.comp2042.controller.GuiController;
import com.comp2042.model.board.InvisibleBlocksBoard;
import com.comp2042.model.event.EventSource;
import com.comp2042.model.event.MoveEvent;
import com.comp2042.model.state.ClearRow;
import com.comp2042.model.state.DownData;
import com.comp2042.model.state.ViewData;


/**
 * Controls the logic for the "Challenge Mode" game.
 * <p>
 * This controller implements specific rules for the Challenge Mode:
 * <ul>
 * <li>Uses an {@link InvisibleBlocksBoard} where blocks disappear.</li>
 * <li>Implements a double-score multiplier for all actions.</li>
 * <li>Handles the countdown timer notifications for block reveals.</li>
 * </ul>
 * </p>
 */
public class ChallengeModeController extends AbstractGameController {

    /**
     * Initializes the Challenge Mode controller.
     * Sets up the {@link InvisibleBlocksBoard}.
     *
     * @param c The GUI Controller responsible for rendering.
     */
    public ChallengeModeController(GuiController c) {
        super(c, new InvisibleBlocksBoard(25, 11));
    }

    /**
     * Handles the downward movement logic.
     * Checks for the invisible board countdown and updates the UI notification if needed.
     *
     * @param event The move event.
     * @return DownData containing the result of the move.
     */
    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();

        // Check countdown for invisible blocks
        if (board instanceof InvisibleBlocksBoard invisibleBoard) {
            String countdown = invisibleBoard.getCountdown();
            if (countdown != null) {
                viewGuiController.showNotification(countdown);
            }
        }

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
            if (event.eventSource() == EventSource.USER) {
                board.getScore().add(2); //  Double Score
            }
            refreshView(); //  Force refresh for invisible effect
            return new DownData(null, board.getViewData());
        }
    }

    /**
     * Handles the hard drop event.
     * Applies a 4x score multiplier for hard drops in Challenge Mode.
     *
     * @return DownData containing the landing result.
     */
    @Override
    public DownData onHardDropEvent() {
        int rowsDropped = board.hardDrop();
        if (rowsDropped > 0) {
            board.getScore().add(rowsDropped * 4); // Double the Score
        }
        return processBrickLanding();
    }

    /**
     * Calculates the score bonus for clearing lines.
     * Applies a 2x multiplier to the base score.
     *
     * @param clearRow The result of the row clearing.
     * @return The doubled score bonus.
     */
    @Override
    protected int calculateScore(ClearRow clearRow) {
        return clearRow.scoreBonus() * 2; // Specific: Double Score
    }

    /**
     * Handles the Game Over state.
     * Stops the internal timer of the InvisibleBoard before proceeding to the standard game over logic.
     */
    @Override
    protected void handleGameOver() {
        if (board instanceof InvisibleBlocksBoard invisibleBoard) {
            invisibleBoard.stopGame();
        }
        super.handleGameOver();
    }
}