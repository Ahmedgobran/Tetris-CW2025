package com.comp2042.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.comp2042.core.GameController;
import com.comp2042.core.GameControllerChallenge;

import java.net.URL;
import java.util.ResourceBundle;

public class LevelSelectionController implements Initializable {


    private Stage stage;
    private Runnable onBackCallback;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setOnBackCallback(Runnable callback) {
        this.onBackCallback = callback;
    }
    // Event handler for the normal mode button
    @FXML
    private void onNormalClicked(ActionEvent event) {
        AudioManager.getInstance().playPlayPress();
        try {
            startNormalGame();
        } catch (Exception e) {
            System.err.println("Error starting normal game: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // Event handler for the special mode button
    @FXML
    private void onSpecialClicked(ActionEvent event) {
        AudioManager.getInstance().playPlayPress();
        try {
            startChallengeGame();
        } catch (Exception e) {
            System.err.println("Error starting challenge game: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // Event handler for the back button
    @FXML
    private void onBackClicked(ActionEvent event) {
        AudioManager.getInstance().playButtonPress();
        if (onBackCallback != null) {
            onBackCallback.run();
        } else if (stage != null) {
            stage.close();
        }
    }
    // Sets up the scene for normal mode where blocks are visible
    private void startNormalGame() throws Exception {
        URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();
        GuiController guiController = fxmlLoader.getController();

        stage.setTitle("TetrisJFX - Normal Mode");
        Scene scene = new Scene(root, 435, 510);
        stage.setScene(scene);
        stage.show();

        new GameController(guiController);
    }
    // Sets up the scene for challenge mode where blocks are invisible
    private void startChallengeGame() throws Exception {
        URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();
        GuiController guiController = fxmlLoader.getController();

        stage.setTitle("TetrisJFX - Challenge Mode ");
        Scene scene = new Scene(root, 435, 510);
        stage.setScene(scene);
        stage.show();

        new GameControllerChallenge(guiController);
    }
}