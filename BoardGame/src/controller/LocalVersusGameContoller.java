package controller;

import game.Field;
import game.Game;
import game.GameInfo;
import player.Player;

public class LocalVersusGameContoller implements Runnable, GameController {

    private Player player1;
    private Player player2;
    private Player activePlayer;
    private Game game;
    private boolean active = true;
    private boolean pending = false;
    private final Object o = new Object();

    public LocalVersusGameContoller() {
        this.game = new Game(GameInfo.REVERSI);
        this.player1 = new Player("Henk de Vries", Field.BLACK, this);
        this.player2 = new Player("Kees van Bommel", Field.WHITE, this);
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void run() {
        try {

            player1.setGame(game);
            player2.setGame(game);

            game.start();
            while (active) {
                if (pending) {
                    pending = false;
                    synchronized (o) { o.wait(100); }
                    activePlayer.move(game.giveMove(activePlayer.getColor()));
                } else {
                    synchronized (o) { o.wait(); }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestInput(Player player) {
        activePlayer = player;
        pending = true;
        synchronized (o) { o.notify(); }
    }
}