package game;

import controller.GameController;

import model.service.GhostAIService.GhostPersonality;

public class Blinky extends Ghost {
    public Blinky(GameController gameController, int x, int y) {
        super(gameController, x, y, "red");
        this.active = true;
    }

    @Override
    protected GhostPersonality getPersonality() {
        return GhostPersonality.BLINKY;
    }
}
