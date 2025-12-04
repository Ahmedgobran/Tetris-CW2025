package com.comp2042.model.event;

import com.comp2042.model.state.DownData;
import com.comp2042.model.state.ViewData;

public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    void createNewGame();

    DownData onHardDropEvent();

    int[][] getBoard();
}
