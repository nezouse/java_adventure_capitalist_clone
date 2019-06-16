package main;

import controller.MainSceneController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    MainSceneController mainSceneController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/base.fxml"));
        mainSceneController = new MainSceneController();
        loader.setController(mainSceneController);
        Parent root = loader.load();
        primaryStage.setTitle("Adventure Capitalist!");
        primaryStage.setScene(new Scene(root, 800, 450));
        primaryStage.show();
    }

    @Override
    public void stop() {
        mainSceneController.saveGame();
        System.out.println("Game was saved!");
    }
}
