package com.comp2042.ui;

import com.comp2042.core.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
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

    private BoardRender boardRenderer;
    private InputEventListener eventListener;
    private Rectangle[][] rectangles;
    private NextPieceRenderer nextPieceRenderer;
    private Group shadowGroup;
    private Timeline timeLine;
    private ShadowRender shadowRender;
    private LineClearAnimation lineClearAnimation;
    private Stage gameStage;

    private final BrickColor colorMapper = new BrickColor();
    private final BooleanProperty isPause = new SimpleBooleanProperty();
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                    if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                        refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                        refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                        refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.SPACE) {
                        hardDrop();
                        keyEvent.consume();
                    }
                }
                if (keyEvent.getCode() == KeyCode.N) {
                    newGame(null);
                }
                // add P key for pause
                if (keyEvent.getCode() == KeyCode.P) {
                    pauseGame(null);
                }
                if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    timeLine.stop();
                    try {
                        SceneLoader.openMainMenu(gameStage);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        gameOverPanel.setVisible(false);

    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        boardRenderer = new BoardRender(gamePanel, boardMatrix, colorMapper, BRICK_SIZE, BRICK_ARC_SIZE);
        lineClearAnimation = new LineClearAnimation(gamePanel, colorMapper, BRICK_SIZE, BRICK_ARC_SIZE);

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(colorMapper.getFillColor(brick.getBrickData()[i][j])); //changed to colorMapper.getFillColor() (used Brick color class)
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);


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
            shadowRender.updateShadow(
                    brick,
                    gamePanel.getLayoutX(),
                    gamePanel.getLayoutY(),
                    brickPanel.getVgap()
            );
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
            brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
            brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }
            }
            // check if ghost piece enabled
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

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(colorMapper.getFillColor(color)); //changed to colorMapper.getFillColor() (used Brick color class)
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);
            showClearRowNotification(downData.getClearRow());
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    private void hardDrop() {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onHardDropEvent();
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

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty integerProperty) {
        if (scoreLabel != null) {
            scoreLabel.textProperty().bind(integerProperty.asString());
        }
    }

    public void gameOver() {
        timeLine.stop();
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
    }

    public void newGame(ActionEvent actionEvent) {
        timeLine.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
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
                Scene gameScene = gameStage.getScene();
                PauseMenuController.showPauseMenu(gameStage, this, gameScene);
            } catch (Exception e) {
                System.err.println("Error showing pause menu: " + e.getMessage());
                e.printStackTrace();
            }
                    }
        gamePanel.requestFocus();
    }

    public void setGameStage(Stage stage) {
        this.gameStage = stage;
    }
    public void resumeGameFromPause() {
        timeLine.play();
        isPause.setValue(Boolean.FALSE);
        gamePanel.requestFocus();
    }
}