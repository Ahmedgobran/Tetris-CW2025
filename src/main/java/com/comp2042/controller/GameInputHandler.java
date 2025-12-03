package com.comp2042.controller;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GameInputHandler {

    private final GuiController controller;

    public GameInputHandler(GuiController controller) {
        this.controller = controller;
    }

    public void handle(KeyEvent event) {
        // Allow these keys even if paused/game over
        if (event.getCode() == KeyCode.N) {
            controller.newGame(null);
            return;
        }
        if (event.getCode() == KeyCode.P) {
            controller.pauseGame(null);
            return;
        }
        if (event.getCode() == KeyCode.ESCAPE) {
            controller.handleEscape();
            return;
        }

        // Block movement keys if paused or game over
        if (controller.isGamePaused() || controller.isGameOver()) {
            return;
        }

        switch (event.getCode()) {
            case LEFT:
            case A:
                controller.moveLeft();
                break;
            case RIGHT:
            case D:
                controller.moveRight();
                break;
            case UP:
            case W:
                controller.rotate();
                break;
            case DOWN:
            case S:
                controller.moveDown();
                break;
            case SPACE:
                controller.hardDrop();
                break;
            default:
                break;
        }
        event.consume();
    }
}