package com.comp2042.controller;

import com.comp2042.model.GameStatus;
import com.comp2042.model.LevelManager;
import com.comp2042.model.event.EventSource;
import com.comp2042.model.event.EventType;
import com.comp2042.model.event.InputEventListener;
import com.comp2042.model.event.MoveEvent;
import com.comp2042.model.state.ClearRow;
import com.comp2042.model.state.DownData;
import com.comp2042.model.state.ViewData;
import com.comp2042.view.GameUIManager;
import com.comp2042.util.*;
import com.comp2042.view.renderers.GameRenderer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * The main controller class for the in-game UI.
 * <p>
 * This class manages the interaction between the user, the game model, and the view.
 * It handles input events, manages the game loop, updates the UI (score, level),
 * and coordinates rendering via the {@link GameRenderer}.
 * </p>
 */
public class GuiController implements Initializable {

    @FXML private GridPane gamePanel;
    @FXML private Group groupNotification;
    @FXML private GridPane brickPanel;
    @FXML private GridPane nextPiecePanel;
    @FXML private Label scoreLabel;
    @FXML private Label levelLabel;
    @FXML private VBox levelBox;
    @FXML private StackPane gameOverPanel;
    @FXML private StackPane rootContainer;
    @FXML private GridPane holdPiecePanel;

    // --- Renderers (created class to handle them) ---
    private GameRenderer gameRenderer;

    // --- Logic & Helpers ---
    private InputEventListener eventListener;
    private GameInputHandler inputHandler;
    private GameLoop gameLoop; // replaced timeline with GameLoop (which handles that now)
    private Stage gameStage;
    private AudioManager audioManager;
    private GameSettings gameSettings;
    private HighScoreManager highScoreManager;
    private GameUIManager uiManager;

    private final ObjectProperty<GameStatus> gameStatus = new SimpleObjectProperty<>(GameStatus.PLAYING);

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if unknown.
     * @param resources The resources used to localize the root object, or null if not found.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(Objects.requireNonNull(getClass().getClassLoader().getResource("digital.ttf")).toExternalForm(), 38);
        // Initialize the new Input Handler
        this.inputHandler = new GameInputHandler(this);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        // Delegate key events to the handler
        gamePanel.setOnKeyPressed(event -> inputHandler.handle(event));
        gameOverPanel.setVisible(false);
        // Hide Level display by default (It stays hidden for Challenge Mode)
        if (levelBox != null) {
            levelBox.setVisible(false);
            levelBox.setManaged(false);
        }
    }

    /**
     * Injects the required dependencies.
     */
    public void initModel(Stage stage, AudioManager audioManager, GameSettings gameSettings, HighScoreManager highScoreManager) {
        this.gameStage = stage;
        this.audioManager = audioManager;
        this.gameSettings = gameSettings;
        this.highScoreManager = highScoreManager;
        // Initialize UI Manager with dependencies
        this.uiManager = new GameUIManager(rootContainer, groupNotification, gameOverPanel, gameStage, this, audioManager, gameSettings, highScoreManager);
    }

    /**
     * Sets up the game view, renderers, and starts the game loop.
     *
     * @param boardMatrix The initial configuration of the board.
     * @param brick       The initial active brick data.
     */
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        // Initialize the simplified Renderer
        gameRenderer = new GameRenderer(gamePanel, brickPanel, nextPiecePanel, holdPiecePanel, boardMatrix[0].length);

        // Setup initial state
        gameRenderer.refreshBackground(boardMatrix);
        gameRenderer.initActivePiece(brick); // Handles rectangle creation and first draw

        audioManager.playMusic("/music/game_music.mp3");
        // Start Game Loop
        gameLoop = new GameLoop(400, (unused) -> processMoveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD)));
        gameLoop.start();
        // Ensure state is playing
        gameStatus.set(GameStatus.PLAYING);
    }


    private void refreshBrick(ViewData brick) {
        if (gameStatus.get() == GameStatus.PLAYING) {
            // refactored
            gameRenderer.render(brick, gameSettings.isGhostPieceEnabled());
        }
    }

    /**
     * Forces a full refresh of the static background board grid.
     *
     * @param board The 2D array representing the board state.
     */
    public void refreshGameBackground(int[][] board) {
        gameRenderer.refreshBackground(board);
    }

    // --- Public Actions ---

    /**
     * Handles the request to move the active brick to the left.
     */
    public void moveLeft() { refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER))); }

    /**
     * Handles the request to move the active brick to the right.
     */
    public void moveRight() { refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER))); }

    /**
     * Handles the request to rotate the active brick.
     */
    public void rotate() { refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER))); }

    /**
     * Handles the request to move the active brick down one step.
     */
    public void moveDown() { processMoveDown(new MoveEvent(EventType.DOWN, EventSource.USER)); }

    /**
     * Handles the request to hold (swap) the current brick.
     */
    public void holdBrick() {refreshBrick(eventListener.onHoldEvent());}

    /**
     * Performs a hard drop, instantly moving the brick to the bottom.
     */
    public void hardDrop() {
        if (gameStatus.get() == GameStatus.PLAYING) {
            DownData downData = eventListener.onHardDropEvent();
            showClearRowNotification(downData.clearRow());
            refreshBrick(downData.viewData());
        }
        gamePanel.requestFocus();
    }

    /** Helper to stop the loop when leaving the scene */
    public void stopGameLoop() {
        if (gameLoop != null) gameLoop.stop();
    }

    /**
     * Handles the Escape key action, stopping the game loop and returning to the main menu.
     */
    public void handleEscape() {
        if (gameLoop != null) gameLoop.stop(); // Stop loop
        try {
            // Pass all dependencies back to Main Menu
            SceneLoader.openMainMenu(gameStage, audioManager, gameSettings, highScoreManager);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void processMoveDown(MoveEvent event) {
        if (gameStatus.get() == GameStatus.PLAYING) {
            DownData downData = eventListener.onDownEvent(event);
            showClearRowNotification(downData.clearRow());
            refreshBrick(downData.viewData());
        }
    }

    //added this method to prevent bcoz of duplicated code in processMoveDown() and hardDrop() methods
    private void showClearRowNotification(ClearRow clearRow) {
        if (clearRow != null && clearRow.getLinesRemoved() > 0) {
            List<Integer> clearedRows = clearRow.clearedRowIndices();
            audioManager.playSFX("/sfx/line-cleared.mp3");
            gameRenderer.getLineClearAnimation().animateClearedRows(clearedRows, () -> {
                refreshGameBackground(eventListener.getBoard());
                uiManager.showNotification("+" + clearRow.scoreBonus());
            });
        }
    }

    /**
     * Delegate notification display to UI Manager.
     */
    public void showNotification(String message) {
        uiManager.showNotification(message);
    }

    // -- setters & getters --
    public void setEventListener(InputEventListener eventListener) { this.eventListener = eventListener; }
    public void bindScore(IntegerProperty integerProperty) { if (scoreLabel != null) scoreLabel.textProperty().bind(integerProperty.asString()); }

    /**
     * Binds the LevelManager to the GUI components to enable level updates and speed changes.
     *
     * @param levelManager The LevelManager instance handling game progression.
     */
    public void bindLevel(LevelManager levelManager) {
        if (levelBox != null) {
            levelBox.setVisible(true);
            levelBox.setManaged(true);
        }
        if (levelLabel != null) {
            levelLabel.textProperty().bind(levelManager.levelProperty().asString());
        }
        levelManager.levelProperty().addListener((obs, oldVal, newVal) -> {
            updateGameSpeed(levelManager.getCurrentDelay());
            uiManager.showNotification("LEVEL " + newVal);
        });
    }
    private void updateGameSpeed(double delayMillis) {
        if (gameLoop != null) {
            gameLoop.setSpeed(delayMillis); // Much cleaner!
        }
    }
    public boolean isGameOver() { return gameStatus.get() == GameStatus.GAME_OVER; }
    public boolean isGamePaused() { return gameStatus.get() == GameStatus.PAUSED; }

    /**
     * Triggers the Game Over state, stops the game loop, and shows the Game Over panel.
     */
    public void gameOver() {
        if (gameLoop != null) gameLoop.stop();
        audioManager.stopMusic();
        audioManager.playSFX("/sfx/negative_beeps.mp3");
        audioManager.playSFX("/sfx/game_over.mp3");
        // Delegate UI update
        uiManager.showGameOver();
        gameStatus.set(GameStatus.GAME_OVER);
    }

    /**
     * Resets the game UI and starts a new game session.
     */
    public void newGame() {
        if (gameLoop != null) gameLoop.stop();
        // Delegate UI update
        uiManager.hideGameOver();
        eventListener.createNewGame();
        gamePanel.requestFocus();
        audioManager.playMusic("/music/game_music.mp3");
        if (gameLoop != null) gameLoop.start();
        gameStatus.set(GameStatus.PLAYING);
    }
//removed setupButtonIcons() method and replaced the logic into gameLayout.fxml
    /**
     * Pauses the game loop and displays the Pause Menu overlay.
     */
    public void pauseGame() {
        if (gameStatus.get() == GameStatus.PLAYING) {
            if (gameLoop != null) gameLoop.pause();
            gameStatus.set(GameStatus.PAUSED);
            // Delegate loading and showing pause menu
            uiManager.showPauseMenu();
        }
    }

    /**
     * Closes the Pause Menu and resumes the game.
     */
    public void closePauseMenu() {
        // Delegate hiding pause menu
        uiManager.closePauseMenu();
        resumeGameFromPause();
    }
    /**
     * Resumes the game loop and restores focus to the game panel.
     */
    public void resumeGameFromPause() {
        if (gameLoop != null) gameLoop.start(); // Resume loop
        gameStatus.set(GameStatus.PLAYING);
        gamePanel.requestFocus();
    }
}