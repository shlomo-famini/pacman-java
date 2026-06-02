import javax.swing.JFrame;

import controller.GameController;
import controller.InputController;
import model.GameModel;
import model.map.MapLoader;
import model.map.MapModel;
import view.AssetManager;
import view.GameView;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Pacman Game");

        MapModel mapModel = MapLoader.loadFromResource("/res/maps/Map.txt");
        GameModel gameModel = new GameModel(mapModel);
        AssetManager assetManager = new AssetManager();
        GameView gameView = new GameView(gameModel, assetManager);
        GameController gameController = new GameController(gameModel, gameView, mapModel);
        InputController inputController = new InputController(gameController);

        gameView.setFocusable(true);
        gameView.addKeyListener(inputController);

        window.add(gameView);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gameView.requestFocusInWindow();
        gameController.startGameThread();
    }
}
