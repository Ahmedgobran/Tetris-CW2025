package com.comp2042.model.board;

import com.comp2042.model.Score;
import com.comp2042.model.state.ClearRow;
import com.comp2042.model.state.ViewData;

public interface Board {

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    boolean createNewBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();

    ClearRow clearRows();

    Score getScore();

    void newGame();

    //gets y pos of where curr brick would land (shadow position)
    int getShadowYPosition();

    int hardDrop();
}
