package com.comp2042.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.comp2042.core.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private Text titleText;

    @FXML
    private Button playButton;

    @FXML
    private Button controlsButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button highScoresButton;

    @FXML
    private Button exitButton;

    @FXML
    private VBox controlsPanel;

    @FXML
    private ScrollPane controlsScrollPane;

    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Hide controls panel initially
        if (controlsScrollPane != null) {
            controlsScrollPane.setVisible(false);
            controlsScrollPane.setManaged(false);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void onPlayClicked(ActionEvent event) {
        try {
            startGame();
        } catch (Exception e) {
            System.err.println("Error starting game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onControlsClicked(ActionEvent event) {
        // Toggle controls panel visibility
        if (controlsScrollPane != null) {
            boolean isVisible = controlsScrollPane.isVisible();
            controlsScrollPane.setVisible(!isVisible);
            controlsScrollPane.setManaged(!isVisible);
            // Adjust window size if controls panel is collapsed/expanded
            if (!isVisible) {
                // Expanding - make window taller
                stage.setHeight(670);
            } else {
                // Collapsing - restore original size
                stage.setHeight(550);
            }
        }
    }

    @FXML
    private void onSettingsClicked(ActionEvent event) {
        // TODO: Implement later
        System.out.println("do later");
    }

    @FXML
    private void onHighScoresClicked(ActionEvent event) {
        // TODO: Implement later
        System.out.println("do later");
    }

    @FXML
    private void onExitClicked(ActionEvent event) {
        // Close the application
        stage.close();
        System.exit(0);
    }

    private void startGame() throws Exception {
        URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
        ResourceBundle resources = null;
        FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
        Parent root = fxmlLoader.load();
        GuiController guiController = fxmlLoader.getController();

        stage.setTitle("TetrisJFX");
        Scene scene = new Scene(root, 435, 510); //adjusts window size of game when launched
        stage.setScene(scene);
        stage.show();

        new GameController(guiController);
    }
}