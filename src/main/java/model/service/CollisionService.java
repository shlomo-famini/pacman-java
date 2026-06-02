package model.service;

import model.GameConfig;
import model.map.MapModel;

public class CollisionService {

    private static final int WALL_TILE = 1;
    private static final int TUNNEL_ROW = 11;

    private final MapModel mapModel;

    public CollisionService(MapModel mapModel) {
        this.mapModel = mapModel;
    }

    public boolean canMove(int x, int y, String direction, int speed) {
        int nextX = x;
        int nextY = y;

        switch (direction) {
            case "up":
                nextY -= speed;
                break;
            case "down":
                nextY += speed;
                break;
            case "left":
                nextX -= speed;
                break;
            case "right":
                nextX += speed;
                break;
            default:
                break;
        }

        return canMoveTo(nextX, nextY, speed);
    }

    public boolean canMoveTo(int nextX, int nextY, int speed) {
        int tileSize = GameConfig.TILE_SIZE;
        int tunnelRow = TUNNEL_ROW * tileSize;

        if (Math.abs(nextY - tunnelRow) <= speed) {
            if (nextX <= -tileSize || nextX >= (GameConfig.MAX_SCREEN_COL - 1) * tileSize) {
                return true;
            }
        }

        int leftCol = nextX / tileSize;
        int rightCol = (nextX + tileSize - 1) / tileSize;
        int topRow = nextY / tileSize;
        int bottomRow = (nextY + tileSize - 1) / tileSize;

        if (leftCol < 0 || rightCol >= GameConfig.MAX_SCREEN_COL
                || topRow < 0 || bottomRow >= GameConfig.MAX_SCREEN_ROW) {
            return false;
        }

        return isWalkable(topRow, leftCol)
                && isWalkable(topRow, rightCol)
                && isWalkable(bottomRow, leftCol)
                && isWalkable(bottomRow, rightCol);
    }

    public boolean isWalkable(int row, int col) {
        return mapModel.getTile(row, col) != WALL_TILE;
    }

    public boolean isWalkableWorldPosition(int worldX, int worldY) {
        int tileSize = GameConfig.TILE_SIZE;
        int row = (worldY + tileSize / 2) / tileSize;
        int col = (worldX + tileSize / 2) / tileSize;

        if (row < 0 || row >= GameConfig.MAX_SCREEN_ROW
                || col < 0 || col >= GameConfig.MAX_SCREEN_COL) {
            return false;
        }

        return isWalkable(row, col);
    }

    public boolean entitiesCollide(int x1, int y1, int x2, int y2, int entitySize) {
        return x1 < x2 + entitySize
                && x1 + entitySize > x2
                && y1 < y2 + entitySize
                && y1 + entitySize > y2;
    }

    public boolean checkTileCollision(int entityX, int entityY, int solidAreaX, int solidAreaY,
                                      int solidAreaWidth, int solidAreaHeight,
                                      String direction, int speed) {
        int tileSize = GameConfig.TILE_SIZE;

        int entityLeftWorldX = entityX + solidAreaX;
        int entityRightWorldX = entityX + solidAreaX + solidAreaWidth;
        int entityTopWorldY = entityY + solidAreaY;
        int entityBottomWorldY = entityY + solidAreaY + solidAreaHeight;

        int leftCol = entityLeftWorldX / tileSize;
        int rightCol = entityRightWorldX / tileSize;
        int topRow = entityTopWorldY / tileSize;
        int bottomRow = entityBottomWorldY / tileSize;

        switch (direction) {
            case "up":
                topRow = (entityTopWorldY - speed) / tileSize;
                if (topRow < 0) {
                    topRow = 0;
                }
                return !isWalkable(topRow, leftCol) || !isWalkable(topRow, rightCol);
            case "down":
                bottomRow = (entityBottomWorldY + speed) / tileSize;
                if (bottomRow >= GameConfig.MAX_SCREEN_ROW) {
                    bottomRow = GameConfig.MAX_SCREEN_ROW - 1;
                }
                return !isWalkable(bottomRow, leftCol) || !isWalkable(bottomRow, rightCol);
            case "left":
                leftCol = (entityLeftWorldX - speed) / tileSize;
                if (leftCol < 0) {
                    leftCol = 0;
                }
                return !isWalkable(topRow, leftCol) || !isWalkable(bottomRow, leftCol);
            case "right":
                rightCol = (entityRightWorldX + speed) / tileSize;
                if (rightCol >= GameConfig.MAX_SCREEN_COL) {
                    rightCol = GameConfig.MAX_SCREEN_COL - 1;
                }
                return !isWalkable(topRow, rightCol) || !isWalkable(bottomRow, rightCol);
            default:
                return false;
        }
    }
}
