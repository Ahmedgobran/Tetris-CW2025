package com.comp2042.controller;

import com.comp2042.util.AudioManager;
import com.comp2042.util.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PauseMenuController implements Initializable {

    private Stage stage;
    private GuiController guiController;
    // Removed 'private Scene gameScene;' - no longer needed

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization logic if needed
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setGuiController(GuiController guiController) {
        this.guiController = guiController;
    }

    // Removed the setGameScene method
    // Removed the static showPauseMenu method (Logic moved to GuiController)

    @FXML
    private void onResumeClicked(ActionEvent event) {
        AudioManager.getInstance().playButtonPress();
        // Instead of switching scenes, we just close the overlay
        guiController.closePauseMenu();
    }

    @FXML
    private void onRestartClicked(ActionEvent event) {
        AudioManager.getInstance().playButtonPress();
        // Start new game
        guiController.newGame(null);
        // Close the overlay
        guiController.closePauseMenu();
    }

    @FXML
    private void onSettingsClicked(ActionEvent event) {
        AudioManager.getInstance().playButtonPress();
        try {
            // This still needs to switch scenes so we levae as is
            SceneLoader.openSettings(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onMainMenuClicked(ActionEvent event) {
        AudioManager.getInstance().playButtonPress();
        try {
            // This still needs to switch scenes so we levae as is
            SceneLoader.openMainMenu(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}