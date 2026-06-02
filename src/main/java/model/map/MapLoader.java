package model.map;

import model.GameConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class MapLoader {

    private MapLoader() {}

    public static MapModel loadFromResource(String path) {
        return loadFromResource(path, GameConfig.MAX_SCREEN_ROW, GameConfig.MAX_SCREEN_COL);
    }

    public static MapModel loadFromResource(String path, int maxRows, int maxCols) {
        MapModel mapModel = new MapModel(maxRows, maxCols);
        int[][] tiles = mapModel.getTiles();

        try {
            InputStream is = MapLoader.class.getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int row = 0;
            String line;
            while ((line = br.readLine()) != null && row < maxRows) {
                String[] tokens = line.trim().split(",");
                for (int col = 0; col < tokens.length && col < maxCols; col++) {
                    tiles[row][col] = Integer.parseInt(tokens[col].trim());
                }
                row++;
            }
            br.close();
        } catch (IOException | NullPointerException e) {
            System.err.println("Could not load map: " + path);
            e.printStackTrace();
        }

        return mapModel;
    }
}
