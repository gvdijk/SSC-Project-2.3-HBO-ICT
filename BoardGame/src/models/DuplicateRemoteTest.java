package models;

import models.controller.ServerGameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.game.GameInfo;

public class DuplicateRemoteTest extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ServerGameController controller = new ServerGameController(GameInfo.REVERSI, "Ijsbeertje", 7);
        Thread thread = new Thread(controller);
        thread.start();
        Scene scene = new Scene(controller.getPane());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

