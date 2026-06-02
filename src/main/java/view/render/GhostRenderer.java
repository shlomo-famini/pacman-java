package view.render;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import view.AssetManager;

public class GhostRenderer {

    private final AssetManager assetManager;

    public GhostRenderer(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public void render(Graphics2D g2, int x, int y, boolean active, boolean eaten,
                       boolean frightened, String originalType, int tileSize) {
        if (!active || eaten) {
            return;
        }

        String imageType = frightened ? "scared" : originalType;
        BufferedImage image = assetManager.getGhostImage(imageType);
        if (image == null) {
            return;
        }

        g2.drawImage(image, x, y, tileSize, tileSize, null);
    }
}
