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

    /**
     * Constructs the Game Over panel.
     * Sets up the styling, size, and layout of the overlay.
     */
    public GameOverPanel() {
        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");

        // Ensure the panel covers the specific dimensions of the game board
        this.setPrefSize(270, 510);
        setCenter(gameOverLabel);
    }

}
