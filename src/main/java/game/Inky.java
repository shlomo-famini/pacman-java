package game;

import controller.GameController;

import model.service.GhostAIService.GhostPersonality;

public class Inky extends Ghost {

    private Blinky blinky;

    public Inky(GameController gameController, int x, int y, Blinky blinky) {
        super(gameController, x, y, "blue");
        this.blinky = blinky;
        this.active = true;
    }

    @Override
    protected GhostPersonality getPersonality() {
        return GhostPersonality.INKY;
    }

    @Override
    protected Ghost getBlinkyForAI() {
        return blinky;
    }
}
