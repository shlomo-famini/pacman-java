package view.render;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import view.AssetManager;

public class CollectibleRenderer {

    public enum CollectibleType {
        COIN,
        POWER_PELLET,
        FRUIT
    }

    private final AssetManager assetManager;

    public CollectibleRenderer(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public void render(Graphics2D g2, CollectibleType type, int x, int y, int tileSize) {
        switch (type) {
            case COIN:
                renderCoin(g2, x, y, tileSize);
                break;
            case POWER_PELLET:
                renderPowerPellet(g2, x, y, tileSize);
                break;
            case FRUIT:
                renderFruit(g2, x, y, tileSize);
                break;
            default:
                break;
        }
    }

    private void renderCoin(Graphics2D g2, int x, int y, int tileSize) {
        g2.setColor(Color.WHITE);
        int size = tileSize / 5;
        g2.fillOval(
                x + (tileSize - size) / 2,
                y + (tileSize - size) / 2,
                size,
                size
        );
    }

    private void renderPowerPellet(Graphics2D g2, int x, int y, int tileSize) {
        g2.setColor(Color.WHITE);
        int size = tileSize / 2;
        g2.fillOval(
                x + (tileSize - size) / 2,
                y + (tileSize - size) / 2,
                size,
                size
        );
    }

    private void renderFruit(Graphics2D g2, int x, int y, int tileSize) {
        BufferedImage image = assetManager.getFruitImage();
        if (image != null) {
            g2.drawImage(image, x, y, tileSize, tileSize, null);
        }
    }
}
