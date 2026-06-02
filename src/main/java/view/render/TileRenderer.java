package view.render;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import model.GameConfig;
import model.map.MapModel;
import view.AssetManager;

public class TileRenderer {

    private final AssetManager assetManager;

    public TileRenderer(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public void render(Graphics2D g2, MapModel mapModel, int tileSize) {
        for (int row = 0; row < GameConfig.MAX_SCREEN_ROW; row++) {
            for (int col = 0; col < GameConfig.MAX_SCREEN_COL; col++) {
                int tileCode = mapModel.getTile(row, col);
                int x = col * tileSize;
                int y = row * tileSize;

                if (tileCode == 1) {
                    BufferedImage wallImage = assetManager.getWallImage();
                    if (wallImage != null) {
                        g2.drawImage(wallImage, x, y, tileSize, tileSize, null);
                    } else {
                        g2.setColor(Color.BLUE);
                        g2.fillRect(x, y, tileSize, tileSize);
                    }
                }
            }
        }
    }
}
