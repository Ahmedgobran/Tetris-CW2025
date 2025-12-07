package com.comp2042;

import com.comp2042.controller.MainMenuController;
import com.comp2042.util.AudioManager;
import com.comp2042.util.GameSettings;
import com.comp2042.util.HighScoreManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    private static final String MAIN_MENU_FXML = "mainMenu.fxml";

    // Core Dependencies
    private AudioManager audioManager;
    private GameSettings gameSettings;
    private HighScoreManager highScoreManager;

    @Override
    public void start(Stage primaryStage) throws IOException {
        // 1. Initialize Dependencies
        audioManager = new AudioManager();
        gameSettings = new GameSettings(audioManager);
        highScoreManager = new HighScoreManager();

        // 2. Load Main Menu and inject dependencies
        Parent root = loadMainMenu(primaryStage);

        primaryStage.setTitle("TetrisJFX");
        Scene scene = new Scene(root, 480, 510);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Loads the main menu FXML and configures its controller.
     *
     * @param stage The primary stage to pass to the controller.
     * @return The loaded root node.
     * @throws IOException If the FXML resource cannot be found or loaded.
     */
    private Parent loadMainMenu(Stage stage) throws IOException {
        URL location = getClass().getClassLoader().getResource(MAIN_MENU_FXML);
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        MainMenuController menuController = fxmlLoader.getController();
        menuController.setStage(stage);

        // Inject Dependencies!
        menuController.initModel(audioManager, gameSettings, highScoreManager);

        return root;
    }

    /**
     * The main method that launches the JavaFX application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}