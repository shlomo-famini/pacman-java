package view;

import java.util.List;

public interface GameRenderBridge {

    int getMenuSelection();

    int getHighScore();

    int getLastScore();

    PlayerRenderData getPlayerRenderData();

    List<GhostRenderData> getGhostRenderData();

    List<CollectibleRenderData> getCollectibleRenderData();

    final class PlayerRenderData {
        private final int x;
        private final int y;
        private final String direction;
        private final int spriteNum;
        private final int score;
        private final int lives;

        public PlayerRenderData(int x, int y, String direction, int spriteNum, int score, int lives) {
            this.x = x;
            this.y = y;
            this.direction = direction;
            this.spriteNum = spriteNum;
            this.score = score;
            this.lives = lives;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public String getDirection() {
            return direction;
        }

        public int getSpriteNum() {
            return spriteNum;
        }

        public int getScore() {
            return score;
        }

        public int getLives() {
            return lives;
        }
    }

    final class GhostRenderData {
        private final int x;
        private final int y;
        private final boolean active;
        private final boolean eaten;
        private final boolean frightened;
        private final String originalType;

        public GhostRenderData(int x, int y, boolean active, boolean eaten,
                               boolean frightened, String originalType) {
            this.x = x;
            this.y = y;
            this.active = active;
            this.eaten = eaten;
            this.frightened = frightened;
            this.originalType = originalType;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public boolean isActive() {
            return active;
        }

        public boolean isEaten() {
            return eaten;
        }

        public boolean isFrightened() {
            return frightened;
        }

        public String getOriginalType() {
            return originalType;
        }
    }

    final class CollectibleRenderData {
        public enum Type {
            COIN,
            POWER_PELLET,
            FRUIT
        }

        private final Type type;
        private final int x;
        private final int y;

        public CollectibleRenderData(Type type, int x, int y) {
            this.type = type;
            this.x = x;
            this.y = y;
        }

        public Type getType() {
            return type;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
