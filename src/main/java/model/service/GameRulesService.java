package model.service;

public class GameRulesService {

    private static final int FRIGHTENED_DURATION_MS = 5000;
    private static final int COIN_SCORE = 10;
    private static final int POWER_PELLET_SCORE = 50;
    private static final int GHOST_EAT_SCORE = 200;

    private long frightenedStartTime = 0;

    public void startFrightenedMode() {
        frightenedStartTime = System.currentTimeMillis();
    }

    public void updateFrightenedMode(FrightenedModeCallbacks callbacks) {
        if (frightenedStartTime > 0
                && System.currentTimeMillis() - frightenedStartTime > FRIGHTENED_DURATION_MS) {
            callbacks.onFrightenedModeExpired();
            frightenedStartTime = 0;
        }
    }

    public GhostEncounterOutcome evaluateGhostEncounter(boolean frightened, boolean eaten, int currentLives) {
        if (frightened) {
            return GhostEncounterOutcome.eatGhost(GHOST_EAT_SCORE);
        }
        if (eaten) {
            return GhostEncounterOutcome.noAction();
        }
        if (currentLives <= 1) {
            return GhostEncounterOutcome.gameOver();
        }
        return GhostEncounterOutcome.loseLife();
    }

    public CollectibleOutcome evaluateCollectible(CollectibleType collectibleType) {
        switch (collectibleType) {
            case COIN:
                return CollectibleOutcome.coin(COIN_SCORE);
            case POWER_PELLET:
                return CollectibleOutcome.powerPellet(POWER_PELLET_SCORE);
            default:
                return CollectibleOutcome.none();
        }
    }

    public int getCollectedCoinCount(int totalCoins, int remainingCoins) {
        return totalCoins - remainingCoins;
    }

    public boolean shouldSpawnFruitsAtThird(int fruitsSpawnedFlag, int collectedCoins, int totalCoins) {
        return fruitsSpawnedFlag == 0 && collectedCoins >= totalCoins / 3;
    }

    public boolean shouldSpawnFruitsAtTwoThirds(int fruitsSpawnedFlag, int collectedCoins, int totalCoins) {
        return fruitsSpawnedFlag == 0 && collectedCoins >= (2 * totalCoins) / 3;
    }

    public enum CollectibleType {
        NONE,
        COIN,
        POWER_PELLET
    }

    public interface FrightenedModeCallbacks {
        void onFrightenedModeExpired();
    }

    public static final class GhostEncounterOutcome {
        public enum Type {
            NO_ACTION,
            EAT_GHOST,
            LOSE_LIFE,
            GAME_OVER
        }

        private final Type type;
        private final int scorePoints;

        private GhostEncounterOutcome(Type type, int scorePoints) {
            this.type = type;
            this.scorePoints = scorePoints;
        }

        public static GhostEncounterOutcome noAction() {
            return new GhostEncounterOutcome(Type.NO_ACTION, 0);
        }

        public static GhostEncounterOutcome eatGhost(int scorePoints) {
            return new GhostEncounterOutcome(Type.EAT_GHOST, scorePoints);
        }

        public static GhostEncounterOutcome loseLife() {
            return new GhostEncounterOutcome(Type.LOSE_LIFE, 0);
        }

        public static GhostEncounterOutcome gameOver() {
            return new GhostEncounterOutcome(Type.GAME_OVER, 0);
        }

        public Type getType() {
            return type;
        }

        public int getScorePoints() {
            return scorePoints;
        }
    }

    public static final class CollectibleOutcome {
        public enum Type {
            NONE,
            COIN,
            POWER_PELLET
        }

        private final Type type;
        private final int scorePoints;
        private final boolean triggerFrightenedMode;

        private CollectibleOutcome(Type type, int scorePoints, boolean triggerFrightenedMode) {
            this.type = type;
            this.scorePoints = scorePoints;
            this.triggerFrightenedMode = triggerFrightenedMode;
        }

        public static CollectibleOutcome none() {
            return new CollectibleOutcome(Type.NONE, 0, false);
        }

        public static CollectibleOutcome coin(int scorePoints) {
            return new CollectibleOutcome(Type.COIN, scorePoints, false);
        }

        public static CollectibleOutcome powerPellet(int scorePoints) {
            return new CollectibleOutcome(Type.POWER_PELLET, scorePoints, true);
        }

        public Type getType() {
            return type;
        }

        public int getScorePoints() {
            return scorePoints;
        }

        public boolean shouldTriggerFrightenedMode() {
            return triggerFrightenedMode;
        }
    }
}
