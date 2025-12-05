package com.comp2042.view;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;


public class GameOverPanel extends BorderPane {

    public GameOverPanel() {
        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");
        this.setPrefSize(260, 510);
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        setCenter(gameOverLabel);
    }

}
