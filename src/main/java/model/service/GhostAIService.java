package model.service;

import model.GameConfig;
import model.entity.PlayerState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class GhostAIService {

    private static final int[][] CARDINAL_DIRS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    private static final int[][] BFS_DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    private final Random random = new Random();

    public GhostMoveResult calculateNextMove(GhostMoveRequest request, CollisionService collisionService) {
        int tileSize = GameConfig.TILE_SIZE;
        int x = request.getGhostX();
        int y = request.getGhostY();
        int dirX = request.getDirX();
        int dirY = request.getDirY();
        int speed = request.getSpeed();

        if (x % tileSize == 0 && y % tileSize == 0) {
            int[] chosenDirection;
            if (request.isFrightened()) {
                chosenDirection = chooseRandomDirection(y / tileSize, x / tileSize, collisionService);
            } else {
                chosenDirection = chooseChaseDirection(request, collisionService);
            }
            dirX = chosenDirection[0];
            dirY = chosenDirection[1];
        }

        int newX = x + dirX * speed;
        int newY = y + dirY * speed;

        if (collisionService.isWalkableWorldPosition(newX, newY)) {
            x = newX;
            y = newY;
        }

        if (x < 0) {
            x = (GameConfig.MAX_SCREEN_COL - 1) * tileSize;
        } else if (x >= GameConfig.MAX_SCREEN_COL * tileSize) {
            x = 0;
        }

        return new GhostMoveResult(dirX, dirY, x, y);
    }

    private int[] chooseChaseDirection(GhostMoveRequest request, CollisionService collisionService) {
        int tileSize = GameConfig.TILE_SIZE;
        int ghostX = request.getGhostX();
        int ghostY = request.getGhostY();

        int ghostRow = clamp((ghostY + tileSize / 2) / tileSize, 0, GameConfig.MAX_SCREEN_ROW - 1);
        int ghostCol = clamp((ghostX + tileSize / 2) / tileSize, 0, GameConfig.MAX_SCREEN_COL - 1);

        int[] target = getTargetTile(request);
        int[][] path = bfs(ghostRow, ghostCol, target[0], target[1], collisionService);

        if (path != null && path.length > 1) {
            int nextRow = path[1][0];
            int nextCol = path[1][1];
            return new int[]{nextCol - ghostCol, nextRow - ghostRow};
        }

        return chooseRandomDirection(ghostRow, ghostCol, collisionService);
    }

    private int[] chooseRandomDirection(int row, int col, CollisionService collisionService) {
        List<int[]> options = new ArrayList<>();
        for (int[] direction : CARDINAL_DIRS) {
            int nextRow = row + direction[1];
            int nextCol = col + direction[0];
            if (isWithinGrid(nextRow, nextCol) && collisionService.isWalkable(nextRow, nextCol)) {
                options.add(direction);
            }
        }

        if (!options.isEmpty()) {
            return options.get(random.nextInt(options.size()));
        }

        return new int[]{0, 0};
    }

    private int[] getTargetTile(GhostMoveRequest request) {
        PlayerState player = request.getPlayerState();
        int tileSize = GameConfig.TILE_SIZE;
        int playerRow = player.getY() / tileSize;
        int playerCol = player.getX() / tileSize;
        int ghostRow = request.getGhostY() / tileSize;
        int ghostCol = request.getGhostX() / tileSize;
        int playerDirX = request.getPlayerDirX();
        int playerDirY = request.getPlayerDirY();

        switch (request.getPersonality()) {
            case BLINKY:
                return new int[]{playerRow, playerCol};
            case PINKY:
                return getPinkyTarget(playerRow, playerCol, playerDirX, playerDirY);
            case INKY:
                return getInkyTarget(request, playerRow, playerCol, playerDirX, playerDirY);
            case CLYDE:
                return getClydeTarget(playerRow, playerCol, ghostRow, ghostCol);
            default:
                return new int[]{
                        clamp(playerRow, 0, GameConfig.MAX_SCREEN_ROW - 1),
                        clamp(playerCol, 0, GameConfig.MAX_SCREEN_COL - 1)
                };
        }
    }

    private int[] getPinkyTarget(int playerRow, int playerCol, int playerDirX, int playerDirY) {
        int targetRow;
        int targetCol;

        if (playerDirX == 0 && playerDirY == -1) {
            targetRow = playerRow - 4;
            targetCol = playerCol - 4;
        } else {
            targetRow = playerRow + playerDirY * 4;
            targetCol = playerCol + playerDirX * 4;
        }

        return new int[]{
                clamp(targetRow, 0, GameConfig.MAX_SCREEN_ROW - 1),
                clamp(targetCol, 0, GameConfig.MAX_SCREEN_COL - 1)
        };
    }

    private int[] getInkyTarget(GhostMoveRequest request, int playerRow, int playerCol,
                                int playerDirX, int playerDirY) {
        int aheadRow = clamp(playerRow + playerDirY * 2, 0, GameConfig.MAX_SCREEN_ROW - 1);
        int aheadCol = clamp(playerCol + playerDirX * 2, 0, GameConfig.MAX_SCREEN_COL - 1);

        if (!request.hasBlinky()) {
            return new int[]{aheadRow, aheadCol};
        }

        int blinkyRow = request.getBlinkyY() / GameConfig.TILE_SIZE;
        int blinkyCol = request.getBlinkyX() / GameConfig.TILE_SIZE;

        int vecRow = aheadRow - blinkyRow;
        int vecCol = aheadCol - blinkyCol;

        int targetRow = blinkyRow + 2 * vecRow;
        int targetCol = blinkyCol + 2 * vecCol;

        return new int[]{
                clamp(targetRow, 0, GameConfig.MAX_SCREEN_ROW - 1),
                clamp(targetCol, 0, GameConfig.MAX_SCREEN_COL - 1)
        };
    }

    private int[] getClydeTarget(int playerRow, int playerCol, int ghostRow, int ghostCol) {
        double distance = Math.hypot(playerRow - ghostRow, playerCol - ghostCol);
        if (distance > 5) {
            return new int[]{playerRow, playerCol};
        }
        return new int[]{GameConfig.MAX_SCREEN_ROW - 2, 1};
    }

    private int[][] bfs(int startRow, int startCol, int endRow, int endCol,
                        CollisionService collisionService) {
        endRow = clamp(endRow, 0, GameConfig.MAX_SCREEN_ROW - 1);
        endCol = clamp(endCol, 0, GameConfig.MAX_SCREEN_COL - 1);
        startRow = clamp(startRow, 0, GameConfig.MAX_SCREEN_ROW - 1);
        startCol = clamp(startCol, 0, GameConfig.MAX_SCREEN_COL - 1);

        int[][] prev = new int[GameConfig.MAX_SCREEN_ROW * GameConfig.MAX_SCREEN_COL][2];
        boolean[][] visited = new boolean[GameConfig.MAX_SCREEN_ROW][GameConfig.MAX_SCREEN_COL];
        Queue<int[]> queue = new LinkedList<>();

        queue.add(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];

            if (row == endRow && col == endCol) {
                LinkedList<int[]> path = new LinkedList<>();
                path.addFirst(new int[]{endRow, endCol});
                while (!(row == startRow && col == startCol)) {
                    int idx = row * GameConfig.MAX_SCREEN_COL + col;
                    int[] previous = prev[idx];
                    if (previous[0] == 0 && previous[1] == 0 && !(row == startRow && col == startCol)) {
                        return null;
                    }
                    path.addFirst(previous);
                    row = previous[0];
                    col = previous[1];
                }
                return path.toArray(new int[0][]);
            }

            for (int[] direction : BFS_DIRS) {
                int nextRow = row + direction[0];
                int nextCol = col + direction[1];
                if (isWithinGrid(nextRow, nextCol)
                        && collisionService.isWalkable(nextRow, nextCol)
                        && !visited[nextRow][nextCol]) {
                    queue.add(new int[]{nextRow, nextCol});
                    visited[nextRow][nextCol] = true;
                    prev[nextRow * GameConfig.MAX_SCREEN_COL + nextCol] = new int[]{row, col};
                }
            }
        }

        return null;
    }

    private boolean isWithinGrid(int row, int col) {
        return row >= 0 && row < GameConfig.MAX_SCREEN_ROW
                && col >= 0 && col < GameConfig.MAX_SCREEN_COL;
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public enum GhostPersonality {
        DEFAULT,
        BLINKY,
        PINKY,
        INKY,
        CLYDE
    }

    public static final class GhostMoveRequest {
        private final int ghostX;
        private final int ghostY;
        private final int dirX;
        private final int dirY;
        private final int speed;
        private final boolean frightened;
        private final GhostPersonality personality;
        private final PlayerState playerState;
        private final int playerDirX;
        private final int playerDirY;
        private final int blinkyX;
        private final int blinkyY;
        private final boolean hasBlinky;

        public GhostMoveRequest(int ghostX, int ghostY, int dirX, int dirY, int speed,
                                boolean frightened, GhostPersonality personality,
                                PlayerState playerState, int playerDirX, int playerDirY,
                                int blinkyX, int blinkyY, boolean hasBlinky) {
            this.ghostX = ghostX;
            this.ghostY = ghostY;
            this.dirX = dirX;
            this.dirY = dirY;
            this.speed = speed;
            this.frightened = frightened;
            this.personality = personality;
            this.playerState = playerState;
            this.playerDirX = playerDirX;
            this.playerDirY = playerDirY;
            this.blinkyX = blinkyX;
            this.blinkyY = blinkyY;
            this.hasBlinky = hasBlinky;
        }

        public int getGhostX() {
            return ghostX;
        }

        public int getGhostY() {
            return ghostY;
        }

        public int getDirX() {
            return dirX;
        }

        public int getDirY() {
            return dirY;
        }

        public int getSpeed() {
            return speed;
        }

        public boolean isFrightened() {
            return frightened;
        }

        public GhostPersonality getPersonality() {
            return personality;
        }

        public PlayerState getPlayerState() {
            return playerState;
        }

        public int getPlayerDirX() {
            return playerDirX;
        }

        public int getPlayerDirY() {
            return playerDirY;
        }

        public int getBlinkyX() {
            return blinkyX;
        }

        public int getBlinkyY() {
            return blinkyY;
        }

        public boolean hasBlinky() {
            return hasBlinky;
        }
    }

    public static final class GhostMoveResult {
        private final int dirX;
        private final int dirY;
        private final int x;
        private final int y;

        public GhostMoveResult(int dirX, int dirY, int x, int y) {
            this.dirX = dirX;
            this.dirY = dirY;
            this.x = x;
            this.y = y;
        }

        public int getDirX() {
            return dirX;
        }

        public int getDirY() {
            return dirY;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
