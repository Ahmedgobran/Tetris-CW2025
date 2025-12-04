package com.comp2042.controller;

import com.comp2042.util.AudioManager;
import javafx.fxml.FXML;
import javafx.stage.Stage;

// simple class to handle controls screen events
public class ControlsController {

    private Stage stage;
    private Runnable onCloseCallback;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setOnCloseCallback(Runnable callback) {
        this.onCloseCallback = callback;
    }

    @FXML
    private void onBackClicked() {
        AudioManager.getInstance().playButtonPress();
        if (onCloseCallback != null) {
            onCloseCallback.run();
        } else if (stage != null) {
            stage.close();
        }
    }
}