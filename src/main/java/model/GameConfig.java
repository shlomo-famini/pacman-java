package model;

public final class GameConfig {

    private GameConfig() {}

    public static final int ORIGINAL_TILE_SIZE = 15;
    public static final int SCALE = 2;
    public static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;

    public static final int MAX_SCREEN_COL = 21;
    public static final int MAX_SCREEN_ROW = 23;
    public static final int SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COL;
    public static final int SCREEN_HEIGHT = TILE_SIZE * MAX_SCREEN_ROW;

    public static final int FPS = 60;
}
