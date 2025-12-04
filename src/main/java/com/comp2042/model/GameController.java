package com.comp2042.model;

import com.comp2042.controller.GuiController;
import com.comp2042.model.board.SimpleBoard;
import com.comp2042.model.event.EventSource;
import com.comp2042.model.event.MoveEvent;
import com.comp2042.model.state.DownData;
import com.comp2042.model.state.ViewData;

public class GameController extends AbstractGameController {

    public GameController(GuiController c) {
        // Pass the specific board type to the parent
        super(c, new SimpleBoard(25, 11));
    }

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
            if (event.eventSource() == EventSource.USER) {
                board.getScore().add(1); // Normal Score
            }
            return new DownData(null, board.getViewData());
        }
    }

    @Override
    public DownData onHardDropEvent() {
        int rowsDropped = board.hardDrop();
        if (rowsDropped > 0) {
            board.getScore().add(rowsDropped * 2); // Normal Score
        }
        return processBrickLanding();
    }
}