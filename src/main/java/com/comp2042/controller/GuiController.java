package com.comp2042.controller;

import com.comp2042.model.LevelManager;
import com.comp2042.model.event.EventSource;
import com.comp2042.model.event.EventType;
import com.comp2042.model.event.InputEventListener;
import com.comp2042.model.event.MoveEvent;
import com.comp2042.model.state.ClearRow;
import com.comp2042.model.state.DownData;
import com.comp2042.model.state.ViewData;
import com.comp2042.util.AudioManager;
import com.comp2042.util.GameLoop; // NEW IMPORT
import com.comp2042.util.GameSettings;
import com.comp2042.util.SceneLoader;
import com.comp2042.view.BrickColor;
import com.comp2042.view.GameOverPanel;
import com.comp2042.view.LineClearAnimation;
import com.comp2042.view.NotificationPanel;
import com.comp2042.view.renderers.ActivePieceRenderer;
import com.comp2042.view.renderers.BoardRender;
import com.comp2042.view.renderers.NextPieceRenderer;
import com.comp2042.view.renderers.ShadowRender;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
    private static final int BRICK_SIZE = 20;
    private static final int BRICK_ARC_SIZE = 9;
    private static final int GAME_TICK_DURATION_MS = 400;

    @FXML private GridPane gamePanel;
    @FXML private Group groupNotification;
    @FXML private GridPane brickPanel;
    @FXML private GridPane nextPiecePanel;
    @FXML private Label scoreLabel;
    @FXML private Label levelLabel;
    @FXML private VBox levelBox;
    @FXML private GameOverPanel gameOverPanel;
    @FXML private StackPane rootContainer;

    // --- Renderers ---
    private BoardRender boardRenderer;
    private ActivePieceRenderer activePieceRenderer;
    private NextPieceRenderer nextPieceRenderer;
    private ShadowRender shadowRender;
    private LineClearAnimation lineClearAnimation;

    // --- Logic & Helpers ---
    private InputEventListener eventListener;
    private GameInputHandler inputHandler;
    private GameLoop gameLoop; // replaced timeline with GameLoop (which handles that now)
    private Stage gameStage;
    private Parent pauseMenuRoot = null;

    private final BrickColor colorMapper = new BrickColor();
    private final BooleanProperty isPause = new SimpleBooleanProperty();
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

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
        boardRenderer = new BoardRender(gamePanel, boardMatrix, colorMapper, BRICK_SIZE, BRICK_ARC_SIZE);
        lineClearAnimation = new LineClearAnimation(gamePanel, colorMapper, BRICK_SIZE, BRICK_ARC_SIZE, boardMatrix[0].length);

        /* Old code manually creating rectangles is gone
        We now use the activepiecerenderer*/
        activePieceRenderer = new ActivePieceRenderer(brickPanel, colorMapper, BRICK_SIZE);
        activePieceRenderer.initRectangles(brick.brickData());
        activePieceRenderer.update(brick, getGamePanelX(), getGamePanelY());
        AudioManager.getInstance().playMusic("/music/game_music.mp3");


        if (nextPiecePanel != null) {
            nextPieceRenderer = new NextPieceRenderer(nextPiecePanel, colorMapper, BRICK_SIZE,BRICK_ARC_SIZE );
            nextPieceRenderer.update(brick.nextBrickData());
        }

        // Initialize shadow group
        Group shadowGroup = new Group();
        if (brickPanel.getParent() instanceof javafx.scene.layout.Pane parent) {
            parent.getChildren().add(shadowGroup);
            shadowGroup.toBack();
        }
        // Create ShadowRender and use it
        shadowRender = new ShadowRender(shadowGroup, colorMapper, BRICK_SIZE);

        if (GameSettings.getInstance().isGhostPieceEnabled()) {
            shadowRender.updateShadow(brick, getGamePanelX(), getGamePanelY(), brickPanel.getVgap());
        }

        // refactored game loop
        gameLoop = new GameLoop(GAME_TICK_DURATION_MS, (unused) -> {
            processMoveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD));
        });
        gameLoop.start();
    }

    private double getGamePanelX() { return gamePanel.getLayoutX(); }
    private double getGamePanelY() { return gamePanel.getLayoutY(); }

    private void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            // refactored
            // Delegating drawing to the renderer
            activePieceRenderer.update(brick, getGamePanelX(), getGamePanelY());

            if (GameSettings.getInstance().isGhostPieceEnabled()) {
                shadowRender.updateShadow(brick, getGamePanelX(), getGamePanelY(), brickPanel.getVgap());
            } else {
                shadowRender.hide(); // Hide the shadow
            }
            if (nextPieceRenderer != null) {
                nextPieceRenderer.update(brick.nextBrickData());
            }
        }
    }

    // deleted old updateShadow() method (now handled by ShadowRender)

    public void refreshGameBackground(int[][] board) {
        boardRenderer.refresh(board);
    }

    // --- Public Actions ---
    public void moveLeft() { refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER))); }
    public void moveRight() { refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER))); }
    public void rotate() { refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER))); }
    public void moveDown() { processMoveDown(new MoveEvent(EventType.DOWN, EventSource.USER)); }

    public void hardDrop() {
        if (isPause.getValue() == Boolean.FALSE) {
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
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);
            showClearRowNotification(downData.clearRow());
            refreshBrick(downData.viewData());
        }
        gamePanel.requestFocus();
    }

    //added this method to prevent bcoz of duplicated code in processMoveDown() and hardDrop() methods
    private void showClearRowNotification(ClearRow clearRow) {
        if (clearRow != null && clearRow.getLinesRemoved() > 0) {
            // Get the cleared row indices before the board updates
            List<Integer> clearedRows = clearRow.clearedRowIndices();
            // play line clear sound effect
            AudioManager.getInstance().playSFX("/sfx/line-cleared.mp3");
            // Play animation on current board state
            lineClearAnimation.animateClearedRows(clearedRows, () -> {
                refreshGameBackground(eventListener.getBoard());
                //only show the score notification to appear if the hard drop actually clears a row
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
    public boolean isGameOver() { return isGameOver.getValue(); }
    public boolean isGamePaused() { return isPause.getValue(); }

    // --- Game Control Methods ---
    public void gameOver() {
        if (gameLoop != null) gameLoop.stop(); // Stop loop
        AudioManager.getInstance().stopMusic();
        AudioManager.getInstance().playSFX("/sfx/negative_beeps.mp3");
        AudioManager.getInstance().playSFX("/sfx/game_over.mp3");
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
    }

    public void newGame() {
        if (gameLoop != null) gameLoop.stop(); // Stop loop
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        AudioManager.getInstance().playMusic("/music/game_music.mp3");
        if (gameLoop != null) gameLoop.start(); // Restart loop
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
    }
//removed setupButtonIcons() method and replaced the logic into gameLayout.fxml

    public void pauseGame() {
        if (isPause.getValue() == Boolean.FALSE) {
            if (gameLoop != null) gameLoop.pause(); // Pause loop
            isPause.setValue(Boolean.TRUE);
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
        isPause.setValue(Boolean.FALSE);
        gamePanel.requestFocus();
    }
}