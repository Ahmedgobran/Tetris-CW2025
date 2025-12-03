package com.comp2042.controller;

import com.comp2042.util.AudioManager;
import com.comp2042.util.HighScoreManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HighScoreController implements Initializable {

    @FXML private VBox scoreContainer;
    private Stage stage;
    private Runnable onCloseCallback;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadHighScores();
    }

    private void loadHighScores() {
        List<Integer> scores = HighScoreManager.getInstance().getScores();

        scoreContainer.getChildren().clear();

        if (scores.isEmpty()) {
            Text emptyText = new Text("No High Scores Yet!");
            emptyText.getStyleClass().add("controls-text"); // Reusing your existing CSS class
            scoreContainer.getChildren().add(emptyText);
        } else {
            for (int i = 0; i < scores.size(); i++) {
                String rank = (i + 1) + ".";
                String scoreText = String.valueOf(scores.get(i));

                Text text = new Text(rank + "      " + scoreText);
                text.getStyleClass().add("controls-text");
                // Optional: make top 3 distinct colors logic here if you want
                scoreContainer.getChildren().add(text);
            }
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setOnCloseCallback(Runnable callback) {
        this.onCloseCallback = callback;
    }

    @FXML
    private void onBackClicked(ActionEvent event) {
        AudioManager.getInstance().playButtonPress();
        if (onCloseCallback != null) {
            onCloseCallback.run();
        } else if (stage != null) {
            stage.close();
        }
    }
}