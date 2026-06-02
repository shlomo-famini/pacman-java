package game;

import controller.GameController;

import model.service.GhostAIService.GhostPersonality;

public class Clyde extends Ghost {
    public Clyde(GameController gameController, int x, int y) {
        super(gameController, x, y, "orange");
        this.active = true;
    }

    @Override
    protected GhostPersonality getPersonality() {
        return GhostPersonality.CLYDE;
    }
}
