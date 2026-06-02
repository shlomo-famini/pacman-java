package game;

import controller.GameController;

import model.service.GhostAIService.GhostPersonality;

public class Pinky extends Ghost {

    public Pinky(GameController gameController, int x, int y) {
        super(gameController, x, y, "pink");
        this.active = true;
    }

    @Override
    protected GhostPersonality getPersonality() {
        return GhostPersonality.PINKY;
    }
}
