package model;

import model.map.MapModel;

public class GameModel {

    private final MapModel mapModel;
    private final WorldModel worldModel;
    private GameState gameState;

    public GameModel(MapModel mapModel) {
        this.mapModel = mapModel;
        this.worldModel = new WorldModel();
        this.gameState = GameState.MENU;
    }

    public MapModel getMapModel() {
        return mapModel;
    }

    public WorldModel getWorldModel() {
        return worldModel;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
