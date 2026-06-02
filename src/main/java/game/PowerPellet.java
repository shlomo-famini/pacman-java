package game;

import controller.GameController;

public class PowerPellet extends Entity {

    public PowerPellet(GameController gameController, int x, int y) {
        super(gameController);
        this.x = x;
        this.y = y;
    }

    @Override
    public void update() {
        if (!collected && gameController.player.collidesWith(this)) {
            collected = true;
            gameController.player.addScore(50);
            gameController.objectManager.setAllGhostsFrightened(true);
            gameController.startFrightenedTimer();
        }
    }
}
