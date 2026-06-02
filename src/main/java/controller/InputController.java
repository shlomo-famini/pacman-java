package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import model.GameState;

import game.Player;

public class InputController implements KeyListener {

    private final GameController gameController;

    public InputController(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        GameState gameState = gameController.gameState;

        if (gameState == GameState.RUNNING && !gameController.gameOver) {
            handlePlayerMovement(e);
        } else if (gameState == GameState.MENU) {
            handleMenuInput(e);
        } else if (gameState == GameState.GAME_OVER) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                gameController.returnToMenu();
            }
        }
    }

    private void handlePlayerMovement(KeyEvent e) {
        Player player = gameController.player;
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_UP) {
            player.setDesiredDirection(0, -1);
        } else if (code == KeyEvent.VK_DOWN) {
            player.setDesiredDirection(0, 1);
        } else if (code == KeyEvent.VK_LEFT) {
            player.setDesiredDirection(-1, 0);
        } else if (code == KeyEvent.VK_RIGHT) {
            player.setDesiredDirection(1, 0);
        }
    }

    private void handleMenuInput(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            gameController.moveMenuSelectionUp();
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            gameController.moveMenuSelectionDown();
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            gameController.confirmMenuSelection();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameController.gameState == GameState.RUNNING && !gameController.gameOver) {
            gameController.player.onKeyReleased(e);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
