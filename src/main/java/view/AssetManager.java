package view;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class AssetManager {

    private BufferedImage up1;
    private BufferedImage up2;
    private BufferedImage down1;
    private BufferedImage down2;
    private BufferedImage left1;
    private BufferedImage left2;
    private BufferedImage right1;
    private BufferedImage right2;

    private BufferedImage redGhost;
    private BufferedImage blueGhost;
    private BufferedImage pinkGhost;
    private BufferedImage orangeGhost;
    private BufferedImage scaredGhost;

    private BufferedImage wall;
    private BufferedImage fruit;

    public AssetManager() {
        loadAll();
    }

    private void loadAll() {
        up1 = loadImage("/res/up1.png");
        up2 = loadImage("/res/up2.png");
        down1 = loadImage("/res/down1.png");
        down2 = loadImage("/res/down2.png");
        left1 = loadImage("/res/left1.png");
        left2 = loadImage("/res/left2.png");
        right1 = loadImage("/res/right1.png");
        right2 = loadImage("/res/right2.png");

        redGhost = loadImage("/res/tiles/redGhost.png");
        blueGhost = loadImage("/res/tiles/blueGhost.png");
        pinkGhost = loadImage("/res/tiles/pinkGhost.png");
        orangeGhost = loadImage("/res/tiles/orangeGhost.png");
        scaredGhost = loadImage("/res/tiles/scaredGhost.png");

        wall = loadImage("/res/tiles/wall.png");
        fruit = loadImage("/res/fruit.png");
    }

    private BufferedImage loadImage(String path) {
        try {
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream(path));
            if (image == null) {
                System.err.println("Failed to load image: " + path);
            }
            return image;
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error loading image: " + path);
            e.printStackTrace();
            return null;
        }
    }

    public BufferedImage getPlayerSprite(String direction, int spriteNum) {
        switch (direction) {
            case "up":
                return spriteNum == 1 ? up1 : up2;
            case "down":
                return spriteNum == 1 ? down1 : down2;
            case "left":
                return spriteNum == 1 ? left1 : left2;
            case "right":
                return spriteNum == 1 ? right1 : right2;
            default:
                return null;
        }
    }

    public BufferedImage getGhostImage(String type) {
        switch (type.toLowerCase()) {
            case "red":
                return redGhost;
            case "blue":
                return blueGhost;
            case "pink":
                return pinkGhost;
            case "orange":
                return orangeGhost;
            case "scared":
                return scaredGhost;
            default:
                return redGhost;
        }
    }

    public BufferedImage getWallImage() {
        return wall;
    }

    public BufferedImage getFruitImage() {
        return fruit;
    }
}
