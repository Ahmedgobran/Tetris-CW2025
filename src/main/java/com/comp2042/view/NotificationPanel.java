package com.comp2042.view;

import javafx.animation.*;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * A custom UI component for displaying in-game notifications.
 * <p>
 * This panel handles various types of floating text notifications such as:
 * score bonuses, countdown timers, and level-up alerts. It manages its own
 * animations and self-removal from the UI scene graph.
 * </p>
 */
public class NotificationPanel extends BorderPane {

    private final Label scoreLabel;

    /**
     * Creates a new notification panel with the specified text.
     *
     * @param text The text to display in the notification.
     */
    public NotificationPanel(String text) {
        setMinHeight(200);
        setMinWidth(220);
        scoreLabel = new Label(text);
        scoreLabel.getStyleClass().add("bonusStyle");
        final Effect glow = new Glow(0.6);
        scoreLabel.setEffect(glow);
        scoreLabel.setTextFill(Color.WHITE); // Default to white
        setCenter(scoreLabel);
    }

    /**
     * Animates the panel as a Score Notification (Fade out and float up).
     * Used for standard points (+100, etc).
     *
     * @param list The parent list of nodes (used to remove itself after animation).
     */
    public void showScore(ObservableList<Node> list) {
        FadeTransition ft = new FadeTransition(Duration.millis(2000), this);
        TranslateTransition tt = new TranslateTransition(Duration.millis(2500), this);
        tt.setToY(this.getLayoutY() - 40);
        ft.setFromValue(1);
        ft.setToValue(0);
        ParallelTransition transition = new ParallelTransition(tt, ft);
        transition.setOnFinished(event -> list.remove(NotificationPanel.this));
        transition.play();
    }

    /**
     * Animates the panel as a Countdown (Zoom out and fade).
     * Used for the "3, 2, 1" countdown in Challenge Mode.
     *
     * @param list The parent list of nodes (used to remove itself after animation).
     */
    public void showCountdown(ObservableList<Node> list) {
        scoreLabel.setTextFill(Color.RED);
        scoreLabel.setStyle("-fx-font-size: 60px;");

        // Zoom out effect
        ScaleTransition st = new ScaleTransition(Duration.millis(500), this);
        st.setFromX(2.0);
        st.setFromY(2.0);
        st.setToX(1.0);
        st.setToY(1.0);

        // Fade out quickly
        FadeTransition ft = new FadeTransition(Duration.millis(900), this);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);

        ParallelTransition transition = new ParallelTransition(st, ft);
        transition.setOnFinished(event -> list.remove(NotificationPanel.this));
        transition.play();
    }

    /**
     * Animates the panel as a Level Up Alert (Pop in, Gold text, Fade out).
     * Used when the player advances a level in Normal Mode.
     *
     * @param list The parent list of nodes (used to remove itself after animation).
     */
    public void showLevelUp(ObservableList<Node> list) {
        scoreLabel.setTextFill(Color.GOLD);
        scoreLabel.setStyle("-fx-font-size: 45px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, black, 2, 1, 0, 0);");

        // 1. Zoom In (Pop effect)
        ScaleTransition st = new ScaleTransition(Duration.millis(500), this);
        st.setFromX(0.0);
        st.setFromY(0.0);
        st.setToX(1.0);
        st.setToY(1.0);

        // 2. Fade Out slowly
        FadeTransition ft = new FadeTransition(Duration.millis(2000), this);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);

        // Play Zoom then Fade
        SequentialTransition sequence = new SequentialTransition(st, ft);
        sequence.setOnFinished(event -> list.remove(NotificationPanel.this));
        sequence.play();
    }
}