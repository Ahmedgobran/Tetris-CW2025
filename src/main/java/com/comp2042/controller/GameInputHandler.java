package com.comp2042.controller;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Handles raw keyboard input events for the game scene.
 * <p>
 * This class interprets key presses (WASD, Arrows, Space, etc.) and translates
 * them into game actions by calling methods on the {@link GameViewController}.
 * It also acts as a filter to prevent input when the game is paused or over.
 * </p>
 */
public class GameInputHandler {

    private final GameViewController controller;

    /**
     * Creates an input handler for the specified controller.
     *
     * @param controller The game controller receiving the commands.
     */
    public GameInputHandler(GameViewController controller) {
        this.controller = controller;
    }

    /**
     * Processes a key event and triggers the corresponding game action.
     *
     * @param event The KeyEvent captured from the scene.
     */
    public void handle(KeyEvent event) {
        // Allow these keys even if paused/game over
        if (event.getCode() == KeyCode.N) {
            controller.newGame();
            return;
        }
        if (event.getCode() == KeyCode.P) {
            controller.pauseGame();
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

        // Gameplay keys
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
            case H:
                controller.holdBrick();
                break;
            default:
                break;
        }
        event.consume();
    }
}