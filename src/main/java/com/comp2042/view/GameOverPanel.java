package com.comp2042.view;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * A custom UI component representing the "Game Over" overlay screen.
 * <p>
 * This panel displays the "GAME OVER" text in the center.
 * </p>
 */
public class GameOverPanel extends BorderPane {

    private static final double PANEL_WIDTH = 270.0;
    private static final double PANEL_HEIGHT = 510.0;

    /**
     * Constructs the Game Over panel.
     * Sets up the styling, size, and layout of the overlay.
     */
    public GameOverPanel() {
        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");

        // Ensure the panel covers the specific dimensions of the game board
        this.setPrefSize(PANEL_WIDTH, PANEL_HEIGHT);
        setCenter(gameOverLabel);
    }

}
