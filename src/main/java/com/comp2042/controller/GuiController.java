package com.comp2042.controller;

import com.comp2042.model.*;
import com.comp2042.util.AudioManager;
import com.comp2042.util.GameSettings;
import com.comp2042.util.SceneLoader;
import com.comp2042.view.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GuiController implements Initializable {
    private static final int BRICK_SIZE = 20;
    private static final int BRICK_ARC_SIZE = 9;

    @FXML private GridPane gamePanel;
    @FXML private Group groupNotification;
    @FXML private GridPane brickPanel;
    @FXML private GridPane nextPiecePanel;
    @FXML private Label scoreLabel;
    @FXML private GameOverPanel gameOverPanel;
    @FXML private StackPane rootContainer;

    // --- Renderers ---
    private BoardRender boardRenderer;
    private ActivePieceRenderer activePieceRenderer; // NEW: Replaces Rectangle[][]
    private NextPieceRenderer nextPieceRenderer;
    private Group shadowGroup;
    private ShadowRender shadowRender;
    private LineClearAnimation lineClearAnimation;

    // --- Logic & Helpers ---
    private InputEventListener eventListener;
    private GameInputHandler inputHandler;
    private Timeline timeLine;
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
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        boardRenderer = new BoardRender(gamePanel, boardMatrix, colorMapper, BRICK_SIZE, BRICK_ARC_SIZE);
        lineClearAnimation = new LineClearAnimation(gamePanel, colorMapper, BRICK_SIZE, BRICK_ARC_SIZE, boardMatrix[0].length);

        /* Old code manually creating rectangles is gone
        We now use the activepiecerenderer*/
        activePieceRenderer = new ActivePieceRenderer(brickPanel, colorMapper, BRICK_SIZE);
        activePieceRenderer.initRectangles(brick.getBrickData());
        activePieceRenderer.update(brick, gamePanel.getLayoutX(), gamePanel.getLayoutY());
        AudioManager.getInstance().playMusic("/music/game_music.mp3");


        if (nextPiecePanel != null) {
            nextPieceRenderer = new NextPieceRenderer(nextPiecePanel, colorMapper, BRICK_SIZE,BRICK_ARC_SIZE );
            nextPieceRenderer.update(brick.getNextBrickData());
        }

        // Initialize shadow group
        shadowGroup = new Group();
        if (brickPanel.getParent() instanceof javafx.scene.layout.Pane parent) {
            parent.getChildren().add(shadowGroup);
            shadowGroup.toBack(); //shadow behind brick like earlier
        }
        // Create ShadowRender and use it
        shadowRender = new ShadowRender(shadowGroup, colorMapper, BRICK_SIZE);
        // check if ghost piece enabled before showing shadow
        if (GameSettings.getInstance().isGhostPieceEnabled()) {
            shadowRender.updateShadow(brick, gamePanel.getLayoutX(), gamePanel.getLayoutY(), brickPanel.getVgap());
        }
        timeLine = new Timeline(new KeyFrame(
                Duration.millis(400),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    private void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            // refactored
            // Delegating drawing to the renderer
            activePieceRenderer.update(brick, gamePanel.getLayoutX(), gamePanel.getLayoutY());

            if (GameSettings.getInstance().isGhostPieceEnabled()) {
                shadowRender.updateShadow(brick, gamePanel.getLayoutX(), gamePanel.getLayoutY(), brickPanel.getVgap());
            } else {
                shadowRender.hide(); // Hide the shadow
            }
            if (nextPieceRenderer != null) {
                nextPieceRenderer.update(brick.getNextBrickData());
            }
        }
    }

    // deleted old updateShadow() method (now handled by ShadowRender)

    public void refreshGameBackground(int[][] board) {
        boardRenderer.refresh(board);
    }

    // --- Public Actions (Called by GameInputHandler) ---
    public void moveLeft() {
        refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
    }
    public void moveRight() {
        refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
    }
    public void rotate() {
        refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
    }
    public void moveDown() {
        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
    }
    public void hardDrop() {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onHardDropEvent();
            showClearRowNotification(downData.getClearRow());
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }
    public void handleEscape() {
        timeLine.stop();
        try {
            SceneLoader.openMainMenu(gameStage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);
            showClearRowNotification(downData.getClearRow());
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    //added this method to prevent bcoz of duplicated code in moveDown() and hardDrop() methods
    private void showClearRowNotification(ClearRow clearRow) {
        if (clearRow != null && clearRow.getLinesRemoved() > 0) {
            // Get the cleared row indices before the board updates
            List<Integer> clearedRows = clearRow.getClearedRowIndices();
            // play line clear sound effect
            AudioManager.getInstance().playSFX("/sfx/line-cleared.mp3");
            // Play animation on current board state
            lineClearAnimation.animateClearedRows(clearedRows, () -> {
                refreshGameBackground(eventListener.getBoard());
                //only show the score notification to appear if the hard drop actually clears a row
                NotificationPanel notificationPanel = new NotificationPanel("+" + clearRow.getScoreBonus());
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
        } else {
            // Use the original method for scores
            notificationPanel.showScore(groupNotification.getChildren());
        }
    }

    // -- setters & getters --
    public void setEventListener(InputEventListener eventListener) { this.eventListener = eventListener; }
    public void bindScore(IntegerProperty integerProperty) { if (scoreLabel != null) scoreLabel.textProperty().bind(integerProperty.asString()); }
    public void setGameStage(Stage stage) { this.gameStage = stage; }
    public boolean isGameOver() { return isGameOver.getValue(); }
    public boolean isGamePaused() { return isPause.getValue(); }

    // --- Game Control Methods ---
    public void gameOver() {
        timeLine.stop();
        AudioManager.getInstance().stopMusic();
        AudioManager.getInstance().playSFX("/sfx/negative_beeps.mp3");
        AudioManager.getInstance().playSFX("/sfx/game_over.mp3");
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
    }

    public void newGame(ActionEvent actionEvent) {
        timeLine.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        AudioManager.getInstance().playMusic("/music/game_music.mp3");
        timeLine.play();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
    }
//removed setupButtonIcons() method and replaced the logic into gameLayout.fxml

    public void pauseGame(ActionEvent actionEvent) {
        if (isPause.getValue() == Boolean.FALSE) {
            timeLine.pause();
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
        timeLine.play();
        isPause.setValue(Boolean.FALSE);
        gamePanel.requestFocus();
    }
}