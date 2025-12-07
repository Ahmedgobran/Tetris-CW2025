package com.comp2042.controller;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameInputHandlerTest {

    private GameInputHandler inputHandler;
    private StubGuiController stubController;

    @BeforeEach
    void setUp() {
        stubController = new StubGuiController();
        inputHandler = new GameInputHandler(stubController);
    }

    @Test
    void testMoveLeft_KeyA() {
        // Simulate pressing 'A'
        KeyEvent event = createKeyEvent(KeyCode.A);
        inputHandler.handle(event);
        assertEquals("moveLeft", stubController.lastAction, "Pressing A should trigger moveLeft");
    }

    @Test
    void testMoveRight_KeyRightArrow() {
        // Simulate pressing 'RIGHT'
        KeyEvent event = createKeyEvent(KeyCode.RIGHT);
        inputHandler.handle(event);
        assertEquals("moveRight", stubController.lastAction, "Pressing RIGHT should trigger moveRight");
    }

    @Test
    void testPause_KeyP() {
        KeyEvent event = createKeyEvent(KeyCode.P);
        inputHandler.handle(event);
        assertEquals("pauseGame", stubController.lastAction, "Pressing P should trigger pauseGame");
    }

    @Test
    void testInputIgnored_WhenPaused() {
        stubController.setPaused(true);
        KeyEvent event = createKeyEvent(KeyCode.SPACE); // Hard Drop
        inputHandler.handle(event);
        assertNull(stubController.lastAction, "Input should be ignored when game is paused");
    }

    // --- Helper to create JavaFX KeyEvents ---
    private KeyEvent createKeyEvent(KeyCode code) {
        return new KeyEvent(KeyEvent.KEY_PRESSED, "", "", code,
                false, false, false, false);
    }

    // --- Stub Controller for testing ---
    // This pretends to be the real GuiController but just records the last method called
    static class StubGuiController extends GameViewController {
        String lastAction = null;
        boolean isPaused = false;

        public StubGuiController() {
            // No-op constructor to avoid loading FXML/Audio
        }

        @Override public void moveLeft() { lastAction = "moveLeft"; }
        @Override public void moveRight() { lastAction = "moveRight"; }
        @Override public void moveDown() { lastAction = "moveDown"; }
        @Override public void rotate() { lastAction = "rotate"; }
        @Override public void hardDrop() { lastAction = "hardDrop"; }
        @Override public void holdBrick() { lastAction = "holdBrick"; }
        @Override public void pauseGame() { lastAction = "pauseGame"; }
        @Override public void newGame() { lastAction = "newGame"; }
        @Override public void handleEscape() { lastAction = "handleEscape"; }

        public void setPaused(boolean paused) { this.isPaused = paused; }
        @Override public boolean isGamePaused() { return isPaused; }
        @Override public boolean isGameOver() { return false; }
    }
}