package com.comp2042;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.comp2042.controller.MainMenuController;

import java.io.IOException;
import java.net.URL;

/**
 * The main entry point for the Tetris application.
 * <p>
 * This class extends {@link Application} and is responsible for initializing the
 * primary stage, loading the Main Menu scene, and launching the application.
 * </p>
 */
public class Main extends Application {

    private static final String MAIN_MENU_FXML = "mainMenu.fxml";
    private static final String WINDOW_TITLE = "Tetris - Main Menu";
    private static final int WINDOW_WIDTH = 480;
    private static final int WINDOW_HEIGHT = 510;

    /**
     * Starts the JavaFX application by loading the Main Menu FXML layout.
     *
     * @param primaryStage The primary stage for this application, onto which
     *                     the application scene can be set.
     * @throws IOException If the FXML resource cannot be loaded.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = loadMainMenu(primaryStage);
        configureStage(primaryStage, root);
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
        if (location == null) {
            throw new IOException("Cannot find resource: " + MAIN_MENU_FXML);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        MainMenuController menuController = fxmlLoader.getController();
        menuController.setStage(stage);

        return root;
    }

    /**
     * Configures the primary stage with title, scene, and window properties.
     *
     * @param stage The primary stage to configure.
     * @param root  The root node of the scene.
     */
    private void configureStage(Stage stage, Parent root) {
        stage.setTitle(WINDOW_TITLE);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.setResizable(false);
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