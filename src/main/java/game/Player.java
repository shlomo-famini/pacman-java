package game;

import controller.GameController;

public class Player extends Entity {

    private int desiredDirX = 0;
    private int desiredDirY = 0;
    private int currentDirX = 0;
    private int currentDirY = 0;

    private int spriteCounter = 0;
    private int spriteNum = 1;
    private int score = 0;

    private int lives = 3;
    private int startX;
    private int startY;

    public Player(GameController gameController) {
        super(gameController);
        this.x = 10 * gameController.tileSize;
        this.y = 15 * gameController.tileSize;
        this.direction = "left";
    }

    @Override
    public void update() {
        boolean moved = false;

        int rightLimit = gameController.maxScreenCol * gameController.tileSize;
        int tunnelRow = 11 * gameController.tileSize;

        if (Math.abs(y - tunnelRow) <= speed) {
            if (x <= -gameController.tileSize) {
                x = rightLimit - gameController.tileSize;
                moved = true;
            } else if (x >= rightLimit) {
                x = 0;
                moved = true;
            }
        }

        if (!moved && (desiredDirX != 0 || desiredDirY != 0)) {
            int turnX = x + desiredDirX * speed;
            int turnY = y + desiredDirY * speed;
            if (canMoveTo(turnX, turnY)) {
                currentDirX = desiredDirX;
                currentDirY = desiredDirY;
                x = turnX;
                y = turnY;
                moved = true;
            }
        }
        if (!moved && (currentDirX != 0 || currentDirY != 0)) {
            int nextX = x + currentDirX * speed;
            int nextY = y + currentDirY * speed;
            if (canMoveTo(nextX, nextY)) {
                x = nextX;
                y = nextY;
                moved = true;
            } else {
                currentDirX = 0;
                currentDirY = 0;
            }
        }
        if (moved) {
            spriteCounter++;
            if (spriteCounter > 10) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }
        if (currentDirX == -1) direction = "left";
        else if (currentDirX == 1) direction = "right";
        else if (currentDirY == -1) direction = "up";
        else if (currentDirY == 1) direction = "down";
    }

    private boolean canMoveTo(int nextX, int nextY) {
        return gameController.collisionService.canMoveTo(nextX, nextY, speed);
    }

    public void setDesiredDirection(int dirX, int dirY) {
        this.desiredDirX = dirX;
        this.desiredDirY = dirY;
    }

    public void onKeyReleased(java.awt.event.KeyEvent e) {
    }

    public int directionX() {
        return currentDirX;
    }

    public int directionY() {
        return currentDirY;
    }

    public int getSpriteNum() {
        return spriteNum;
    }

    public void addScore(int points) {
        score += points;
    }

    public int getScore() {
        return score;
    }

    public boolean collidesWith(Entity other) {
        return gameController.collisionService.entitiesCollide(
                x, y, other.x, other.y, gameController.tileSize);
    }

    public int getLives() {
        return lives;
    }

    public void loseLife() {
        lives--;
    }

    public void resetPosition() {
        this.x = startX;
        this.y = startY;
        this.desiredDirX = 0;
        this.desiredDirY = 0;
        this.currentDirX = 0;
        this.currentDirY = 0;
        this.direction = "left";
        this.spriteNum = 1;
    }

    public void setStartPosition() {
        this.startX = this.x;
        this.startY = this.y;
    }
}
