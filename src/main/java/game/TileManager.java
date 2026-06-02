package game;

import controller.GameController;

import model.map.MapModel;

public class TileManager {

    GameController gameController;
    private MapModel mapModel;

    public TileManager(GameController gameController) {
        this.gameController = gameController;
    }

    public void setMapModel(MapModel mapModel) {
        this.mapModel = mapModel;
    }

    public int getTile(int row, int col) {
        return mapModel.getTile(row, col);
    }
}
