package com.comp2042.controller;

import com.comp2042.model.NormalModeController;
import com.comp2042.util.AudioManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.comp2042.model.ChallengeModeController;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controls the Level Selection screen interactions.
 * <p>
 * This class allows the user to choose between different game modes (Normal vs Challenge).
 * It handles the initialization of the specific Game Controller and Board type associated
 * with the selected mode.
 * </p>
 */
public class LevelSelectionController implements Initializable {

    private Stage stage;
    private Runnable onBackCallback;
    private static final int WINDOW_WIDTH = 610;
    private static final int WINDOW_HEIGHT = 515;

    /**
     * Initializes the controller class.
     * <p>
     * Called automatically after the FXML file has been loaded. Currently empty as
     * no specific initialization logic is required for the view elements.
     * </p>
     *
     * @param location  The location used to resolve relative paths for the root object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Sets the primary stage for scene transitions.
     *
     * @param stage The application window.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Registers a callback to be executed when the "Back" button is clicked.
     * <p>
     * This is typically used to return the user to the Main Menu scene.
     * </p>
     *
     * @param callback A {@link Runnable} defining the navigation action.
     */
    public void setOnBackCallback(Runnable callback) {
        this.onBackCallback = callback;
    }

    /**
     * Handles the "Normal Mode" selection event.
     * <p>
     * Plays a start sound and initiates the standard Tetris game logic.
     * </p>
     */
    @FXML
    private void onNormalClicked() {
        AudioManager.getInstance().playPlayPress();
        try {
            startNormalGame();
        } catch (Exception e) {
            System.err.println("Error starting normal game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Challenge Mode" selection event.
     * <p>
     * Plays a start sound and initiates the Challenge game logic (e.g., Invisible Blocks).
     * </p>
     */
    @FXML
    private void onSpecialClicked() {
        AudioManager.getInstance().playPlayPress();
        try {
            startChallengeGame();
        } catch (Exception e) {
            System.err.println("Error starting challenge game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Back" button click event.
     * <p>
     * Executes the registered callback (if any) to return to the previous screen,
     * otherwise closes the stage.
     * </p>
     */
    @FXML
    private void onBackClicked() {
        AudioManager.getInstance().playButtonPress();
        if (onBackCallback != null) {
            onBackCallback.run();
        } else if (stage != null) {
            stage.close();
        }
    }

    /**
     * Helper method to initialize and launch the game in Normal Mode.
     * <p>
     * Loads the game layout FXML, sets up the scene, and instantiates the
     * {@link NormalModeController} .
     * </p>
     *
     * @throws Exception If the FXML resource cannot be loaded.
     */
    private void startNormalGame() throws Exception {
        URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();
        GuiController guiController = fxmlLoader.getController();

        stage.setTitle("TetrisJFX - Normal Mode");
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.show();
        guiController.setGameStage(stage);
        new NormalModeController(guiController);
    }

    /**
     * Helper method to initialize and launch the game in Challenge Mode.
     * <p>
     * Loads the game layout FXML, sets up the scene, and instantiates the
     * {@link ChallengeModeController} .
     * </p>
     *
     * @throws Exception If the FXML resource cannot be loaded.
     */
    private void startChallengeGame() throws Exception {
        URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();
        GuiController guiController = fxmlLoader.getController();

        stage.setTitle("TetrisJFX - Challenge Mode ");
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.show();
        guiController.setGameStage(stage);
        new ChallengeModeController(guiController);
    }
}