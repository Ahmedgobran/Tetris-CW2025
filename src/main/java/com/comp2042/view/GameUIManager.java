package com.comp2042.view;

import com.comp2042.controller.GuiController;
import com.comp2042.controller.PauseMenuController;
import com.comp2042.util.AudioManager;
import com.comp2042.util.GameSettings;
import com.comp2042.util.HighScoreManager;
import com.comp2042.util.SceneLoader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Manages the UI overlays and notifications for the game.
 * <p>
 * This class extracts the responsibility of handling the Pause Menu, Game Over screen,
 * and floating notifications from the main {@link GuiController}, adhering to the
 * Single Responsibility Principle.
 * </p>
 */
public class GameUIManager {

    private final StackPane rootContainer;
    private final Group groupNotification;
    private final Parent gameOverPanel;
    private final Stage gameStage;
    private final GuiController guiController;

    // State for the Pause Menu
    private Parent pauseMenuRoot;

    // Dependencies to pass to screens
    private final AudioManager audioManager;
    private final GameSettings gameSettings;
    private final HighScoreManager highScoreManager;

    public GameUIManager(StackPane rootContainer, Group groupNotification, Parent gameOverPanel, Stage gameStage, GuiController guiController, AudioManager audioManager, GameSettings gameSettings, HighScoreManager highScoreManager)
    {
        this.rootContainer = rootContainer;
        this.groupNotification = groupNotification;
        this.gameOverPanel = gameOverPanel;
        this.gameStage = gameStage;
        this.guiController = guiController;
        this.audioManager = audioManager;
        this.gameSettings = gameSettings;
        this.highScoreManager = highScoreManager;
    }

    /**
     * Loads and displays the Pause Menu overlay.
     */
    public void showPauseMenu() {
        try {
            if (pauseMenuRoot == null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("pauseMenu.fxml"));
                pauseMenuRoot = fxmlLoader.load();

                PauseMenuController pauseController = fxmlLoader.getController();
                pauseController.setGuiController(guiController);
                pauseController.setStage(gameStage);
                // Inject dependencies
                pauseController.initModel(audioManager, gameSettings, highScoreManager);
            }
            // Add overlay
            rootContainer.getChildren().add(pauseMenuRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes the Pause Menu overlay.
     */
    public void closePauseMenu() {
        if (pauseMenuRoot != null) {
            rootContainer.getChildren().remove(pauseMenuRoot);
        }
    }

    /**
     * Displays the Game Over overlay.
     */
    public void showGameOver() {
        gameOverPanel.setVisible(true);
    }

    /**
     * Hides the Game Over overlay (for new games).
     */
    public void hideGameOver() {
        gameOverPanel.setVisible(false);
    }

    /**
     * Displays a floating notification message.
     *
     * @param message The text to display (e.g., "+100", "LEVEL 2").
     */
    public void showNotification(String message) {
        NotificationPanel notificationPanel = new NotificationPanel(message);
        groupNotification.getChildren().add(notificationPanel);

        if (message.length() <= 2) {
            notificationPanel.showCountdown(groupNotification.getChildren());
        } else if (message.startsWith("LEVEL")) {
            notificationPanel.showLevelUp(groupNotification.getChildren());
        } else {
            notificationPanel.showScore(groupNotification.getChildren());
        }
    }
}