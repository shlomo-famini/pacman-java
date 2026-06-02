package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.GameConfig;
import model.GameModel;
import model.GameState;
import model.WorldModel;
import model.entity.GhostState;
import model.entity.PlayerState;
import model.map.MapModel;
import model.service.CollisionService;
import model.service.GameRulesService;
import model.service.GameRulesService.FrightenedModeCallbacks;
import model.service.GhostAIService;
import view.GameRenderBridge;
import view.GameRenderBridge.CollectibleRenderData;
import view.GameRenderBridge.GhostRenderData;
import view.GameRenderBridge.PlayerRenderData;
import view.GameView;

import game.Coin;
import game.Entity;
import game.Fruit;
import game.Ghost;
import game.ObjectManager;
import game.Player;
import game.PowerPellet;
import game.TileManager;

public class GameController implements Runnable, GameRenderBridge {

    public GameState gameState = GameState.MENU;
    private int menuSelection = 0;

    public final int tileSize = GameConfig.TILE_SIZE;
    public final int maxScreenCol = GameConfig.MAX_SCREEN_COL;
    public final int maxScreenRow = GameConfig.MAX_SCREEN_ROW;
    public final int screenWidth = GameConfig.SCREEN_WIDTH;
    public final int screenHeight = GameConfig.SCREEN_HEIGHT;

    private Thread gameThread;
    public Player player;
    public ObjectManager objectManager;
    public TileManager tileManager;
    public MapModel mapModel;
    public int[][] mapData;

    private final GameModel gameModel;
    private final GameView gameView;
    public CollisionService collisionService;
    public GameRulesService gameRulesService;
    public GhostAIService ghostAIService;

    public boolean gameOver = false;

    private int highScore = 0;
    public int lastScore = 0;
    private final File highScoreFile = new File("highscore.txt");

    public GameController(GameModel gameModel, GameView gameView, MapModel mapModel) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.mapModel = mapModel;
        this.mapData = mapModel.getTiles();

        gameView.setRenderBridge(this);

        player = new Player(this);
        tileManager = new TileManager(this);
        tileManager.setMapModel(mapModel);

        collisionService = new CollisionService(mapModel);
        gameRulesService = new GameRulesService();
        ghostAIService = new GhostAIService();

        objectManager = new ObjectManager(this);
        objectManager.setupObjects(mapData);

        initializeWorldModelFromEntities();
        loadHighScore();
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public GameView getGameView() {
        return gameView;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1_000_000_000.0 / GameConfig.FPS;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (gameThread != null) {
            long now = System.nanoTime();
            delta += (now - lastTime) / drawInterval;
            lastTime = now;

            if (delta >= 1) {
                update();
                gameView.repaint();
                delta--;
            }
        }
    }

    public void update() {
        if (gameState == GameState.RUNNING && !gameOver) {
            gameRulesService.updateFrightenedMode(new FrightenedModeCallbacks() {
                @Override
                public void onFrightenedModeExpired() {
                    for (Ghost ghost : objectManager.ghosts) {
                        ghost.setFrightened(false);
                        ghost.resetDirection();
                    }
                }
            });
            player.update();
            objectManager.update();
        }

        syncModelState();
    }

    private void initializeWorldModelFromEntities() {
        WorldModel worldModel = gameModel.getWorldModel();
        worldModel.clearGhostStates();
        for (int i = 0; i < objectManager.ghosts.size(); i++) {
            worldModel.addGhostState(new GhostState());
        }
    }

    private void syncModelState() {
        gameModel.setGameState(gameState);

        PlayerState playerState = gameModel.getWorldModel().getPlayerState();
        playerState.setX(player.x);
        playerState.setY(player.y);
        playerState.setSpeed(player.speed);
        playerState.setDirection(player.direction);

        for (int i = 0; i < objectManager.ghosts.size(); i++) {
            Ghost ghost = objectManager.ghosts.get(i);
            GhostState ghostState = gameModel.getWorldModel().getGhostStates().get(i);
            ghostState.setX(ghost.x);
            ghostState.setY(ghost.y);
            ghostState.setSpeed(ghost.getSpeed());
            ghostState.setDirection(ghost.getMovementDirection());
        }
    }

    public void startFrightenedTimer() {
        gameRulesService.startFrightenedMode();
    }

    public void startNewGame() {
        player = new Player(this);
        objectManager = new ObjectManager(this);
        objectManager.setupObjects(mapData);
        player.setStartPosition();
        initializeWorldModelFromEntities();
        gameOver = false;
        gameState = GameState.RUNNING;
    }

    public void gameOver() {
        lastScore = player.getScore();
        if (lastScore > highScore) {
            highScore = lastScore;
            saveHighScore();
        }
        gameOver = true;
        gameState = GameState.GAME_OVER;
    }

    public void returnToMenu() {
        gameState = GameState.MENU;
    }

    public void moveMenuSelectionUp() {
        menuSelection--;
        if (menuSelection < 0) {
            menuSelection = 1;
        }
    }

    public void moveMenuSelectionDown() {
        menuSelection++;
        if (menuSelection > 1) {
            menuSelection = 0;
        }
    }

    public int getMenuSelectionIndex() {
        return menuSelection;
    }

    public void confirmMenuSelection() {
        if (menuSelection == 0) {
            startNewGame();
        } else if (menuSelection == 1) {
            System.exit(0);
        }
    }

    private void loadHighScore() {
        if (highScoreFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(highScoreFile))) {
                String line = br.readLine();
                highScore = Integer.parseInt(line);
                System.out.println("Loaded high score: " + highScore);
            } catch (Exception e) {
                System.err.println("Could not read highscore file.");
            }
        }
    }

    private void saveHighScore() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(highScoreFile))) {
            bw.write(Integer.toString(highScore));
            System.out.println("Saved high score: " + highScore);
        } catch (IOException e) {
            System.err.println("Could not save highscore.");
        }
    }

    @Override
    public int getMenuSelection() {
        return menuSelection;
    }

    @Override
    public int getHighScore() {
        return highScore;
    }

    @Override
    public int getLastScore() {
        return lastScore;
    }

    @Override
    public PlayerRenderData getPlayerRenderData() {
        return new PlayerRenderData(
                player.x,
                player.y,
                player.direction,
                player.getSpriteNum(),
                player.getScore(),
                player.getLives());
    }

    @Override
    public List<GhostRenderData> getGhostRenderData() {
        List<GhostRenderData> ghosts = new ArrayList<>();
        for (Ghost ghost : objectManager.ghosts) {
            ghosts.add(new GhostRenderData(
                    ghost.x,
                    ghost.y,
                    ghost.isActive(),
                    ghost.isEaten(),
                    ghost.isFrightened(),
                    ghost.getOriginalType()));
        }
        return ghosts;
    }

    @Override
    public List<CollectibleRenderData> getCollectibleRenderData() {
        List<CollectibleRenderData> collectibles = new ArrayList<>();
        for (Entity obj : objectManager.objects) {
            if (obj instanceof Coin && !obj.isCollected()) {
                collectibles.add(new CollectibleRenderData(CollectibleRenderData.Type.COIN, obj.x, obj.y));
            } else if (obj instanceof PowerPellet && !obj.isCollected()) {
                collectibles.add(new CollectibleRenderData(CollectibleRenderData.Type.POWER_PELLET, obj.x, obj.y));
            }
        }
        for (Fruit fruit : objectManager.fruits) {
            collectibles.add(new CollectibleRenderData(CollectibleRenderData.Type.FRUIT, fruit.x, fruit.y));
        }
        return collectibles;
    }
}
