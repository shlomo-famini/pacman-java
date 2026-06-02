package game;

import controller.GameController;

public class Fruit extends Entity {

    private int timer = 0;
    private int lifeSpan;
    private boolean shouldRemove = false;

    public Fruit(GameController gameController, int x, int y, int lifeSpan) {
        super(gameController);
        this.x = x;
        this.y = y;
        this.lifeSpan = lifeSpan;
    }

    @Override
    public void update() {
        timer++;
        if (timer > lifeSpan) {
            shouldRemove = true;
        }

        if (gameController.player != null && gameController.player.collidesWith(this)) {
            gameController.player.addScore(100);
            shouldRemove = true;
        }
    }

    @Override
    public boolean shouldRemove() {
        return shouldRemove;
    }
}
