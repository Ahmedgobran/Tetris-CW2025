package com.comp2042.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import com.comp2042.core.GameController;

import java.net.URL;
import java.util.ResourceBundle;

public class LevelSelectionController implements Initializable {

    @FXML private Button normalButton;
    @FXML private Button specialButton;
    @FXML private Button backButton;

    private Stage stage;
    private Runnable onBackCallback;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization logic can go here if needed
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setOnBackCallback(Runnable callback) {
        this.onBackCallback = callback;
    }

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

    @FXML
    private void onSpecialClicked(ActionEvent event) {
        AudioManager.getInstance().playPlayPress();
        // TODO: Implement special level game start
        System.out.println("Special level coming soon!");
    }

    @FXML
    private void onBackClicked(ActionEvent event) {
        AudioManager.getInstance().playButtonPress();
        if (onBackCallback != null) {
            onBackCallback.run();
        } else if (stage != null) {
            stage.close();
        }
    }

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
}