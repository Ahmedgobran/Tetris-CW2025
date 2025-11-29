package com.comp2042.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PauseMenuController implements Initializable {

    private Stage stage;
    private GuiController guiController;
    private Scene gameScene;

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

    public void setGameScene(Scene gameScene) {
        this.gameScene = gameScene;
    }

    public static void showPauseMenu(Stage stage, GuiController guiController, Scene gameScene) throws Exception {
        URL location = guiController.getClass().getClassLoader().getResource("pauseMenu.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        PauseMenuController pauseMenuController = fxmlLoader.getController();
        pauseMenuController.setStage(stage);
        pauseMenuController.setGuiController(guiController);
        pauseMenuController.setGameScene(gameScene);

        Scene pauseMenuScene = new Scene(root, 435, 510);
        stage.setScene(pauseMenuScene);
    }

    @FXML
    private void onResumeClicked(ActionEvent event) {
        AudioManager.getInstance().playButtonPress();
        stage.setScene(gameScene);
        guiController.resumeGameFromPause();
    }

    @FXML
    private void onRestartClicked(ActionEvent event) {
        AudioManager.getInstance().playButtonPress();
        stage.setScene(gameScene);
        guiController.newGame(null);
    }

    @FXML
    private void onSettingsClicked(ActionEvent event) {
        AudioManager.getInstance().playButtonPress();
        try {
            openSettings();
        } catch (Exception e) {
            System.err.println("Error opening settings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onMainMenuClicked(ActionEvent event) {
        AudioManager.getInstance().playButtonPress();
        try {
            returnToMainMenu();
        } catch (Exception e) {
            System.err.println("Error returning to main menu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void openSettings() throws Exception {
        URL location = getClass().getClassLoader().getResource("settingsPanel.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        SettingsController settingsController = fxmlLoader.getController();

        // Save current pause scene to return to
        Scene currentScene = stage.getScene();

        // Set callback to return to pause menu
        settingsController.setOnCloseCallback(() -> stage.setScene(currentScene));

        Scene settingsScene = new Scene(root, 440, 510);
        stage.setScene(settingsScene);
    }

    private void returnToMainMenu() throws Exception {
        URL location = getClass().getClassLoader().getResource("mainMenu.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        MainMenuController mainMenuController = fxmlLoader.getController();
        mainMenuController.setStage(stage);

        stage.setTitle("TetrisJFX");
        Scene mainMenuScene = new Scene(root, 500, 600);
        stage.setScene(mainMenuScene);
    }
}