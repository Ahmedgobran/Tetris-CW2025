package com.comp2042.model.board;

import com.comp2042.model.state.ClearRow;
import com.comp2042.util.MatrixOperations;

public class TetrisBoard extends AbstractBoard {

    public TetrisBoard(int height, int width) {
        super(height, width);
    }

    @Override
    public int[][] getBoardMatrix() {
        return boardMatrix;
    }

    @Override
    public void mergeBrickToBackground() {
        boardMatrix = MatrixOperations.merge(boardMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(boardMatrix);
        boardMatrix = clearRow.newMatrix();
        return clearRow;
    }

    @Override
    public void newGame() {
        boardMatrix = new int[height][width];
        score.reset();
        createNewBrick();
    }
}