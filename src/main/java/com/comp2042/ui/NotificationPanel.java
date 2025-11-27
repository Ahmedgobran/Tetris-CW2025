package com.comp2042.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class NotificationPanel extends BorderPane {

    private final Label scoreLabel; // Kept reference to change color later

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
    // shows score then fades out
    public void showScore(ObservableList<Node> list) {
        FadeTransition ft = new FadeTransition(Duration.millis(2000), this);
        TranslateTransition tt = new TranslateTransition(Duration.millis(2500), this);
        tt.setToY(this.getLayoutY() - 40);
        ft.setFromValue(1);
        ft.setToValue(0);
        ParallelTransition transition = new ParallelTransition(tt, ft);
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                list.remove(NotificationPanel.this);
            }
        });
        transition.play();
    }

    // new method for challenge mode for Countdown (3, 2, 1)
    public void showCountdown(ObservableList<Node> list) {
        scoreLabel.setTextFill(Color.RED);
        scoreLabel.setStyle("-fx-font-size: 60px;"); // Optional: Make it bigger
        //  Zoom out effect
        ScaleTransition st = new ScaleTransition(Duration.millis(500), this);
        st.setFromX(2.0);
        st.setFromY(2.0);
        st.setToX(1.0);
        st.setToY(1.0);
        // Fade out quickly (so it clears before the next number)
        FadeTransition ft = new FadeTransition(Duration.millis(900), this);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ParallelTransition transition = new ParallelTransition(st, ft);
        transition.setOnFinished(event -> list.remove(NotificationPanel.this));
        transition.play();
    }
}
