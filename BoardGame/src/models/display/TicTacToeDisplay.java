package models.display;

import javafx.scene.input.MouseEvent;
import models.gamecontroller.GameController;
import models.game.Field;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class TicTacToeDisplay extends Display {

    final int boardSize = 3;

    public TicTacToeDisplay(GameController gameController) {
        super(gameController);
        initiateComponents(boardSize, 200, Color.LIGHTGREEN, Color.DARKGREEN);
    }

    @Override
    public void update(Field[] board) {
        Platform.runLater(() -> {
            piecesPane.getChildren().clear();
            for (int i = 0; i < boardSize*boardSize; i++){
                if (board[i] != Field.EMPTY) {
                    Circle circle = new Circle();
                    circle.radiusProperty().bind(boardPane.widthProperty().divide(boardSize * 3));
                    circle.centerXProperty().bind(boardPane.widthProperty().divide(boardSize).multiply(i%boardSize + 0.5));
                    circle.centerYProperty().bind(boardPane.heightProperty().divide(boardSize).multiply( Math.floorDiv(i, boardSize) + 0.5));
                    if (board[i] == Field.WHITE) {
                        circle.setFill(Color.WHITE);
                    } else if (board[i] == Field.BLACK) {
                        circle.setFill(Color.BLACK);
                    }
                    piecesPane.getChildren().add(circle);
                }
            }
            eventPane.toFront();
        });
    }
}
