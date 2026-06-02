package game;

import controller.GameController;

import java.awt.Rectangle;

public abstract class Entity {
    protected GameController gameController;
    public int x, y;
    public int speed = 3;
    public String direction = "down";

    protected Rectangle solidArea;
    protected int solidAreaDefaultX, solidAreaDefaultY;

    protected boolean collected = false;

    public Entity(GameController gameController) {
        this.gameController = gameController;
    }

    public abstract void update();

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        this.collected = true;
    }

    public boolean shouldRemove() {
        return false;
    }
}
