package com.comp2042.model;

import com.comp2042.controller.GuiController;

public class GameControllerChallenge extends AbstractGameController {

    public GameControllerChallenge(GuiController c) {
        super(c, new InvisibleBlocksBoard(25, 11));
    }

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
            if (currentView.getyPosition() == currentView.getShadowYPosition()) {
                return processBrickLanding();
            }
        }

        if (!canMove) {
            return processBrickLanding();
        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(2); //  Double Score
            }
            refreshView(); //  Force refresh for invisible effect
            return new DownData(null, board.getViewData());
        }
    }

    @Override
    public DownData onHardDropEvent() {
        int rowsDropped = board.hardDrop();
        if (rowsDropped > 0) {
            board.getScore().add(rowsDropped * 4); // Double the Score
        }
        return processBrickLanding();
    }

    @Override
    protected int calculateScore(ClearRow clearRow) {
        return clearRow.getScoreBonus() * 2; // Specific: Double Score
    }

    @Override
    protected void handleGameOver() {
        if (board instanceof InvisibleBlocksBoard invisibleBoard) {
            invisibleBoard.stopGame();
        }
        super.handleGameOver();
    }
}