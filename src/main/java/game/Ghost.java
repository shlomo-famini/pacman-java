package game;

import controller.GameController;

import model.entity.PlayerState;
import model.service.GhostAIService.GhostMoveRequest;
import model.service.GhostAIService.GhostMoveResult;
import model.service.GhostAIService.GhostPersonality;

public class Ghost extends Entity {

    protected int speed = 2;
    protected boolean frightened = false;
    protected boolean eaten = false;
    protected boolean active = false;

    protected int homeX, homeY;
    protected int dirX = 0;
    protected int dirY = 0;

    protected String originalType;
    private boolean forceNewTarget = false;

    public Ghost(GameController gameController, int x, int y, String type) {
        super(gameController);
        this.x = x;
        this.y = y;
        this.homeX = x;
        this.homeY = y;
        this.originalType = type;
    }

    public void update() {
        if (!active) return;

        if (eaten) {
            moveTo(homeX, homeY);
            if (x == homeX && y == homeY) {
                eaten = false;
                frightened = false;
            }
            return;
        }

        applyAIMovement();
    }

    protected GhostPersonality getPersonality() {
        return GhostPersonality.DEFAULT;
    }

    protected Ghost getBlinkyForAI() {
        return null;
    }

    public String getOriginalType() {
        return originalType;
    }

    public boolean isActive() {
        return active;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDirX() {
        return dirX;
    }

    public int getDirY() {
        return dirY;
    }

    public String getMovementDirection() {
        if (dirX == -1) return "left";
        if (dirX == 1) return "right";
        if (dirY == -1) return "up";
        if (dirY == 1) return "down";
        return direction;
    }

    private void applyAIMovement() {
        PlayerState playerState = new PlayerState();
        playerState.setX(gameController.player.x);
        playerState.setY(gameController.player.y);
        playerState.setDirection(gameController.player.direction);

        Ghost blinky = getBlinkyForAI();
        GhostMoveRequest request = new GhostMoveRequest(
                x,
                y,
                dirX,
                dirY,
                speed,
                frightened,
                getPersonality(),
                playerState,
                gameController.player.directionX(),
                gameController.player.directionY(),
                blinky != null ? blinky.x : 0,
                blinky != null ? blinky.y : 0,
                blinky != null
        );

        GhostMoveResult result = gameController.ghostAIService.calculateNextMove(
                request, gameController.collisionService);
        dirX = result.getDirX();
        dirY = result.getDirY();
        x = result.getX();
        y = result.getY();
    }

    protected void moveTo(int targetX, int targetY) {
        if (x < targetX) x += speed;
        else if (x > targetX) x -= speed;
        if (y < targetY) y += speed;
        else if (y > targetY) y -= speed;
    }

    public void setFrightened(boolean state) {
        frightened = state;
        speed = state ? 1 : 2;
        if (!state) {
            int tileSize = gameController.tileSize;
            x = Math.round((float) x / tileSize) * tileSize;
            y = Math.round((float) y / tileSize) * tileSize;
            resetDirection();
        }
        forceNewTarget = true;
    }

    public void resetDirection() {
        dirX = 0;
        dirY = 0;
        forceNewTarget = true;
    }

    public boolean isFrightened() {
        return frightened;
    }

    public void setEaten(boolean eaten) {
        this.eaten = eaten;
        frightened = false;
        speed = eaten ? 3 : 2;
        resetDirection();
    }

    public boolean isEaten() {
        return eaten;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void resetToHome() {
        this.x = homeX;
        this.y = homeY;
        this.eaten = false;
        this.frightened = false;
        this.active = true;
        resetDirection();
    }
}
