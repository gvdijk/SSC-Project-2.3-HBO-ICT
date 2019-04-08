package controller;

import exceptions.IllegalGamePlayerException;
import game.Field;
import game.Game;
import game.ReversiGame;
import player.Player;
import player.UiPlayer;
import servercom.ServerWorker;

import java.util.Scanner;

public class RemoteReversiController implements Runnable, Controller {

    private Player player;
    private Player opponent;
    private Player activePlayer;
    private Game game;
    private boolean active = true;
    private boolean pending = false;

    private boolean confirm = false;

    private String name = "";


    private final Object waitForPlayerInput = new Object();
    private final Object waitForServerResponse = new Object();
    private final Object delay = new Object();

    public RemoteReversiController() {
        this.game = new ReversiGame();
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void run() {

        ServerWorker worker = new ServerWorker("localhost", 7789, this);
        Thread thread = new Thread(worker);
        thread.start();

        try {
            synchronized (delay) { delay.wait(1000); }
            Scanner scanner = new Scanner(System.in);
            while (!confirm) {
                name = scanner.nextLine();
                worker.loginPlayer(name);
                synchronized (waitForServerResponse) { waitForServerResponse.wait(); }
            }
            confirm = false;
            while (!confirm) {
                worker.subscribeToGame(game.getGameName());
                synchronized (waitForServerResponse) { waitForServerResponse.wait(); }
            }
            confirm = false;
            while (!confirm) {
                synchronized (waitForServerResponse) { waitForServerResponse.wait(); }
            }
            /*
            player.setGame(game);
            opponent.setGame(game);

            game.start();
            while (active) {
                if (pending) {
                    pending = false;
                    activePlayer.move(game.giveMove(activePlayer.getColor()));
                } else {
                    synchronized (waitForPlayerInput) { waitForPlayerInput.wait(); }
                }
            }
            */
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestInput(Player player) {
        activePlayer = player;
        pending = true;
        synchronized (waitForPlayerInput) { waitForPlayerInput.notify(); }
    }

    @Override
    public void confirmation (boolean result) {
        confirm = result;
        synchronized (waitForServerResponse) { waitForServerResponse.notifyAll(); }
    }

    @Override
    public void matchStart(String opponentName, boolean myTurn) throws IllegalGamePlayerException
    {
        if (myTurn) {
            player = new UiPlayer(name);
            player.setColor(Field.BLACK);
            opponent = new UiPlayer(opponentName);
            opponent.setColor(Field.WHITE);
        } else {
            player = new UiPlayer(name);
            player.setColor(Field.WHITE);
            opponent = new UiPlayer(opponentName);
            opponent.setColor(Field.BLACK);
        }

        player.setController(this);
        opponent.setController(this);

        synchronized (waitForServerResponse) { waitForServerResponse.notifyAll(); }
    }
}
