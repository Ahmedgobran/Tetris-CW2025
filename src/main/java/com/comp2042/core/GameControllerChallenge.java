package com.comp2042.core;

import com.comp2042.ui.GuiController;

public class GameControllerChallenge implements InputEventListener {

    private final InvisibleBlocksBoard board = new InvisibleBlocksBoard(25, 11);

    private final GuiController viewGuiController;

    public GameControllerChallenge(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
    }

    @Override
    public int[][] getBoard() {
        return board.getBoardMatrix();
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        String countdown = board.getCountdown();
        if (countdown != null) {
            viewGuiController.showNotification(countdown);
        }
        if (!canMove) {
            return processBrickLanding();
        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
            /*there was a prob occuring where only refreshes if blocks touch fix:
             Force the UI to refresh the background board on every tick this triggers board.getBoardMatrix(), which checks the timer
             and toggles invisibility even while the piece is still falling.
            */
            viewGuiController.refreshGameBackground(board.getBoardMatrix());
            return new DownData(null, board.getViewData());
        }
    }

    @Override
    public DownData onHardDropEvent() {
        int rowsDropped = board.hardDrop();
        if (rowsDropped > 0) {
            board.getScore().add(rowsDropped * 2);
        }
        return processBrickLanding();
    }

    private DownData processBrickLanding() {
        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();

        if (clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());
        }

        if (board.createNewBrick()) {
            board.stopGame();
            viewGuiController.gameOver();
        }

        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        return board.getViewData();
    }

    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }
}