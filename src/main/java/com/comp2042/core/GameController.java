package com.comp2042.core;

import com.comp2042.ui.GuiController;
import com.comp2042.ui.HighScoreManager;

public class GameController implements InputEventListener {

    private final Board board = new SimpleBoard(25, 11); //adjust gameboard dimentiones

    private final GuiController viewGuiController;

    public GameController(GuiController c) {
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

        // instant lock
        if (canMove) {
            ViewData currentView = board.getViewData();
            if (currentView.getyPosition() == currentView.getShadowYPosition()) {
                return processBrickLanding();
            }
        }

        if (!canMove) {
            return processBrickLanding(); // use helper method
        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
            return new DownData(null, board.getViewData());
        }
    }

    @Override
    public DownData onHardDropEvent() {
        int rowsDropped = board.hardDrop();
        if (rowsDropped > 0) {
            board.getScore().add(rowsDropped * 2);
        }
        return processBrickLanding(); //
    }

    // added this helper method due to code duplication in onDownEvent() and onHardDropEvent()
    private DownData processBrickLanding() {
        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();

        if (clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());
        }

        // This logic was duplicated in both methods
        if (board.createNewBrick()) {
            HighScoreManager.getInstance().addScore(board.getScore().scoreProperty().get());
            viewGuiController.gameOver();
        }

        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }


    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }

}
