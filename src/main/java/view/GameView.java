package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import model.GameConfig;
import model.GameModel;
import model.GameState;
import view.render.CollectibleRenderer;
import view.render.CollectibleRenderer.CollectibleType;
import view.render.GhostRenderer;
import view.render.PlayerRenderer;
import view.render.TileRenderer;
import view.screen.GameOverScreenRenderer;
import view.screen.HudRenderer;
import view.screen.MenuScreenRenderer;

public class GameView extends JPanel {

    private final GameModel gameModel;
    private final AssetManager assetManager;
    private GameRenderBridge renderBridge;

    private final PlayerRenderer playerRenderer;
    private final GhostRenderer ghostRenderer;
    private final TileRenderer tileRenderer;
    private final CollectibleRenderer collectibleRenderer;
    private final MenuScreenRenderer menuScreenRenderer;
    private final HudRenderer hudRenderer;
    private final GameOverScreenRenderer gameOverScreenRenderer;

    public GameView(GameModel gameModel, AssetManager assetManager) {
        this.gameModel = gameModel;
        this.assetManager = assetManager;

        this.setPreferredSize(new Dimension(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);

        this.playerRenderer = new PlayerRenderer(assetManager);
        this.ghostRenderer = new GhostRenderer(assetManager);
        this.tileRenderer = new TileRenderer(assetManager);
        this.collectibleRenderer = new CollectibleRenderer(assetManager);
        this.menuScreenRenderer = new MenuScreenRenderer();
        this.hudRenderer = new HudRenderer();
        this.gameOverScreenRenderer = new GameOverScreenRenderer();
    }

    public void setRenderBridge(GameRenderBridge renderBridge) {
        this.renderBridge = renderBridge;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (renderBridge == null) {
            g2.dispose();
            return;
        }

        switch (gameModel.getGameState()) {
            case MENU:
                menuScreenRenderer.render(
                        g2,
                        renderBridge.getMenuSelection(),
                        GameConfig.SCREEN_WIDTH,
                        GameConfig.SCREEN_HEIGHT);
                break;
            case RUNNING:
                renderRunningScreen(g2);
                break;
            case GAME_OVER:
                gameOverScreenRenderer.render(
                        g2,
                        renderBridge.getLastScore(),
                        renderBridge.getHighScore(),
                        GameConfig.SCREEN_WIDTH,
                        GameConfig.SCREEN_HEIGHT);
                break;
            default:
                break;
        }

        g2.dispose();
    }

    private void renderRunningScreen(Graphics2D g2) {
        int tileSize = GameConfig.TILE_SIZE;

        tileRenderer.render(g2, gameModel.getMapModel(), tileSize);
        renderCollectibles(g2, tileSize);

        for (GameRenderBridge.GhostRenderData ghost : renderBridge.getGhostRenderData()) {
            ghostRenderer.render(
                    g2,
                    ghost.getX(),
                    ghost.getY(),
                    ghost.isActive(),
                    ghost.isEaten(),
                    ghost.isFrightened(),
                    ghost.getOriginalType(),
                    tileSize);
        }

        GameRenderBridge.PlayerRenderData player = renderBridge.getPlayerRenderData();
        playerRenderer.render(
                g2,
                player.getX(),
                player.getY(),
                player.getDirection(),
                player.getSpriteNum(),
                tileSize);
        hudRenderer.render(g2, player.getScore(), renderBridge.getHighScore(), player.getLives());
    }

    private void renderCollectibles(Graphics2D g2, int tileSize) {
        for (GameRenderBridge.CollectibleRenderData collectible : renderBridge.getCollectibleRenderData()) {
            CollectibleType type;
            switch (collectible.getType()) {
                case COIN:
                    type = CollectibleType.COIN;
                    break;
                case POWER_PELLET:
                    type = CollectibleType.POWER_PELLET;
                    break;
                case FRUIT:
                    type = CollectibleType.FRUIT;
                    break;
                default:
                    continue;
            }
            collectibleRenderer.render(g2, type, collectible.getX(), collectible.getY(), tileSize);
        }
    }
}
