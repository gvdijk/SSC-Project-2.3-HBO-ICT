package models;

import models.gamecontroller.ServerGameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.game.GameInfo;

public class RemoteTest extends Application {

        public static void main(String[] args) {
            Application.launch(args);
        }

        @Override
        public void start(Stage primaryStage) {
            ServerGameController controller = new ServerGameController(GameInfo.REVERSI, "Quetzal", 7);
            Thread thread = new Thread(controller);
            thread.start();
            Scene scene = new Scene(controller.getDisplay());
            primaryStage.setScene(scene);
            primaryStage.show();
        }
}
