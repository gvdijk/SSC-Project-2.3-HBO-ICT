package models.display;

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
        initiateComponents();
    }

    private void initiateComponents() {
        boardPane.setPrefSize(300, 300);
        this.setPrefSize(300, 300);

        for (int i = 0; i < boardSize; i++){
            for (int j = 0; j < boardSize; j++){
                Rectangle box = new Rectangle();
                box.widthProperty().bind(boardPane.widthProperty().divide(boardSize));
                box.heightProperty().bind(boardPane.heightProperty().divide(boardSize));
                box.xProperty().bind(boardPane.widthProperty().divide(boardSize).multiply(i));
                box.yProperty().bind(boardPane.heightProperty().divide(boardSize).multiply(j));
                if((i%2==0 && j%2==1) || (i%2==1 && j%2==0)){
                    box.setFill(Color.LIGHTGREEN);
                }else{
                    box.setFill(Color.DARKGREEN);
                }
                boardPane.getChildren().add(box);
            }
        }
        this.getChildren().add(boardPane);
        this.getChildren().add(piecesPane);
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

        });
    }
}
