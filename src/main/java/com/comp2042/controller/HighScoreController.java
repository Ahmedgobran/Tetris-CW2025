package com.comp2042.controller;

import com.comp2042.util.AudioManager;
import com.comp2042.util.HighScoreManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controls the logic for the High Scores display screen.
 * <p>
 * This controller is responsible for fetching the top scores from the injected
 * {@link HighScoreManager} and dynamically populating the UI list. It also handles
 * navigation back to the previous screen.
 * </p>
 */
public class HighScoreController implements Initializable {

    @FXML private VBox scoreContainer;
    private Stage stage;
    private Runnable onCloseCallback;

    // Injected Dependencies
    private HighScoreManager highScoreManager;
    private AudioManager audioManager;

    /**
     * Standard FXML initialization hook.
     * <p>
     * Logic is deferred to {@link #initModel} because this controller requires
     * external service dependencies to function.
     * </p>
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    /**
     * Initializes the controller with the required service dependencies.
     * <p>
     * Immediately fetches and displays the high score list upon initialization.
     * </p>
     *
     * @param manager The HighScoreManager to retrieve persisted scores from.
     * @param audio   The AudioManager used for button interaction sounds.
     */
    public void initModel(HighScoreManager manager, AudioManager audio) {
        this.highScoreManager = manager;
        this.audioManager = audio;
        loadHighScores();
    }

    /**
     * Fetches the top scores and populates the UI container.
     * <p>
     * If no scores exist, a placeholder message is displayed. Otherwise, the scores
     * are listed with their rank.
     * </p>
     */
    private void loadHighScores() {
        List<Integer> scores = highScoreManager.getScores();
        scoreContainer.getChildren().clear();

        if (scores.isEmpty()) {
            Text emptyText = new Text("No High Scores Yet!");
            emptyText.getStyleClass().add("controls-text"); // Reusing existing CSS class
            scoreContainer.getChildren().add(emptyText);
        } else {
            for (int i = 0; i < scores.size(); i++) {
                String rank = (i + 1) + ".";
                String scoreText = String.valueOf(scores.get(i));

                Text text = new Text(rank + "      " + scoreText);
                text.getStyleClass().add("controls-text");
                scoreContainer.getChildren().add(text);
            }
        }
    }

    /**
     * Sets the primary stage for this controller.
     * @param stage The application window.
     */
    public void setStage(Stage stage) { this.stage = stage; }

    /**
     * Registers a callback to execute when the "Back" button is clicked.
     * @param callback The action to run (usually returning to the Main Menu).
     */
    public void setOnCloseCallback(Runnable callback) { this.onCloseCallback = callback; }

    /**
     * Handles the "Back" button click event.
     * <p>
     * Plays a button sound and executes the navigation callback.
     * </p>
     */
    @FXML
    private void onBackClicked() {
        audioManager.playButtonPress();
        if (onCloseCallback != null) {
            onCloseCallback.run();
        } else if (stage != null) {
            stage.close();
        }
    }
}