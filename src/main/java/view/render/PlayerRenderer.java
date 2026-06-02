package view.render;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import view.AssetManager;

public class PlayerRenderer {

    private final AssetManager assetManager;

    public PlayerRenderer(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public void render(Graphics2D g2, int x, int y, String direction, int spriteNum, int tileSize) {
        BufferedImage image = assetManager.getPlayerSprite(direction, spriteNum);
        if (image == null) {
            return;
        }

        int offset = tileSize / 8;
        int size = tileSize - offset * 2;
        g2.drawImage(image, x + offset, y + offset, size, size, null);
    }
}
