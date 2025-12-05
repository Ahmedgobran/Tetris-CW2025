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
import com.comp2042.util.AudioManager;
import com.comp2042.util.GameLoop;
import com.comp2042.view.renderers.GameRenderer;
import com.comp2042.util.GameSettings;
import com.comp2042.util.SceneLoader;
import com.comp2042.view.GameOverPanel;
import com.comp2042.view.NotificationPanel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    @FXML private GridPane gamePanel;
    @FXML private Group groupNotification;
    @FXML private GridPane brickPanel;
    @FXML private GridPane nextPiecePanel;
    @FXML private Label scoreLabel;
    @FXML private Label levelLabel;
    @FXML private VBox levelBox;
    @FXML private GameOverPanel gameOverPanel;
    @FXML private StackPane rootContainer;
    @FXML private GridPane holdPiecePanel;

    // --- Renderers (created class to handle them) ---
    private GameRenderer gameRenderer;

    // --- Logic & Helpers ---
    private InputEventListener eventListener;
    private GameInputHandler inputHandler;
    private GameLoop gameLoop; // replaced timeline with GameLoop (which handles that now)
    private Stage gameStage;
    private Parent pauseMenuRoot = null;

    private final ObjectProperty<GameStatus> gameStatus = new SimpleObjectProperty<>(GameStatus.PLAYING);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
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

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        // Initialize the simplified Renderer
        gameRenderer = new GameRenderer(gamePanel, brickPanel, nextPiecePanel, holdPiecePanel, boardMatrix[0].length);

        // Setup initial state
        gameRenderer.refreshBackground(boardMatrix);
        gameRenderer.initActivePiece(brick); // Handles rectangle creation and first draw

        AudioManager.getInstance().playMusic("/music/game_music.mp3");

        // Start Game Loop
        gameLoop = new GameLoop(400, (unused) -> processMoveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD)));
        gameLoop.start();
        // Ensure state is playing
        gameStatus.set(GameStatus.PLAYING);
    }


    private void refreshBrick(ViewData brick) {
        if (gameStatus.get() == GameStatus.PLAYING) {
            // refactored
            gameRenderer.render(brick, GameSettings.getInstance().isGhostPieceEnabled());
        }
    }
    public void refreshGameBackground(int[][] board) {
        gameRenderer.refreshBackground(board);
    }

    // --- Public Actions ---
    public void moveLeft() { refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER))); }
    public void moveRight() { refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER))); }
    public void rotate() { refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER))); }
    public void moveDown() { processMoveDown(new MoveEvent(EventType.DOWN, EventSource.USER)); }
    public void holdBrick() {refreshBrick(eventListener.onHoldEvent());}

    public void hardDrop() {
        if (gameStatus.get() == GameStatus.PLAYING) {
            DownData downData = eventListener.onHardDropEvent();
            showClearRowNotification(downData.clearRow());
            refreshBrick(downData.viewData());
        }
        gamePanel.requestFocus();
    }
    public void handleEscape() {
        if (gameLoop != null) gameLoop.stop(); // Stop loop
        try {
            SceneLoader.openMainMenu(gameStage);
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
            AudioManager.getInstance().playSFX("/sfx/line-cleared.mp3");
            // Access animation through the new game renderer class
            gameRenderer.getLineClearAnimation().animateClearedRows(clearedRows, () -> {
                refreshGameBackground(eventListener.getBoard());
                NotificationPanel notificationPanel = new NotificationPanel("+" + clearRow.scoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            });
        }
    }

    public void showNotification(String message) {
        NotificationPanel notificationPanel = new NotificationPanel(message);
        groupNotification.getChildren().add(notificationPanel);

        // Check if it's a countdown number (length 1 or 2)
        if (message.length() <= 2) {
            // Use the new method for countdowns
            notificationPanel.showCountdown(groupNotification.getChildren());
        } else if (message.startsWith("LEVEL")) {
            // level up case
            notificationPanel.showLevelUp(groupNotification.getChildren());
        } else {
            // Score
            notificationPanel.showScore(groupNotification.getChildren());
        }
    }

    // -- setters & getters --
    public void setEventListener(InputEventListener eventListener) { this.eventListener = eventListener; }
    public void bindScore(IntegerProperty integerProperty) { if (scoreLabel != null) scoreLabel.textProperty().bind(integerProperty.asString()); }
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
            // Triggers the notification whenever level changes
            showNotification("LEVEL " + newVal);
        });
    }
    private void updateGameSpeed(double delayMillis) {
        if (gameLoop != null) {
            gameLoop.setSpeed(delayMillis); // Much cleaner!
        }
    }
    public void setGameStage(Stage stage) { this.gameStage = stage; }
    public boolean isGameOver() { return gameStatus.get() == GameStatus.GAME_OVER; }
    public boolean isGamePaused() { return gameStatus.get() == GameStatus.PAUSED; }

    // --- Game Control Methods ---
    public void gameOver() {
        if (gameLoop != null) gameLoop.stop(); // Stop loop
        AudioManager.getInstance().stopMusic();
        AudioManager.getInstance().playSFX("/sfx/negative_beeps.mp3");
        AudioManager.getInstance().playSFX("/sfx/game_over.mp3");
        gameOverPanel.setVisible(true);
        gameStatus.set(GameStatus.GAME_OVER);
    }

    public void newGame() {
        if (gameLoop != null) gameLoop.stop(); // Stop loop
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        AudioManager.getInstance().playMusic("/music/game_music.mp3");
        if (gameLoop != null) gameLoop.start(); // Restart loop
        gameStatus.set(GameStatus.PLAYING);
    }
//removed setupButtonIcons() method and replaced the logic into gameLayout.fxml

    public void pauseGame() {
        if (gameStatus.get() == GameStatus.PLAYING) {
            if (gameLoop != null) gameLoop.pause(); // Pause loop
            gameStatus.set(GameStatus.PAUSED);
            // Show pause menu
            try {
                if (pauseMenuRoot == null) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("pauseMenu.fxml"));
                    pauseMenuRoot = fxmlLoader.load();
                    // Configure the controller
                    PauseMenuController pauseController = fxmlLoader.getController();
                    pauseController.setGuiController(this);
                    pauseController.setStage(gameStage);
                }
                // Add the menu to the StackPane (Overlay on top of game)
                rootContainer.getChildren().add(pauseMenuRoot);
            } catch (Exception e) {
                e.printStackTrace();
            }
                    }
    }
    public void closePauseMenu() {
        if (pauseMenuRoot != null) {
            // Remove the overlay
            rootContainer.getChildren().remove(pauseMenuRoot);
            // Resume the game
            resumeGameFromPause();
        }
    }
    // Update resumeGameFromPause to ensure focus returns to game
    public void resumeGameFromPause() {
        if (gameLoop != null) gameLoop.start(); // Resume loop
        gameStatus.set(GameStatus.PLAYING);
        gamePanel.requestFocus();
    }
}