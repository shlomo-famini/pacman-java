package model.map;

import model.GameConfig;

public class MapModel {

    private final int[][] tiles;
    private final int rows;
    private final int cols;

    public MapModel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.tiles = new int[rows][cols];
    }

    public MapModel(int[][] tiles) {
        this.tiles = tiles;
        this.rows = tiles.length;
        this.cols = tiles[0].length;
    }

    public int getTile(int row, int col) {
        return tiles[row][col];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    /**
     * Returns the underlying grid for legacy callers during the MVC migration.
     */
    public int[][] getTiles() {
        return tiles;
    }

    public static MapModel empty() {
        return new MapModel(GameConfig.MAX_SCREEN_ROW, GameConfig.MAX_SCREEN_COL);
    }
}
