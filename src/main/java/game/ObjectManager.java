package game;

import controller.GameController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import model.service.GameRulesService;
import model.service.GameRulesService.CollectibleOutcome;
import model.service.GameRulesService.GhostEncounterOutcome;

public class ObjectManager {

    GameController gameController;
    public ArrayList<Entity> objects;
    public ArrayList<Ghost> ghosts;
    public ArrayList<Fruit> fruits;

    private int totalCoins = 0;
    private int fruitsSpawnedAtThird = 0;
    private int fruitsSpawnedAtTwoThirds = 0;
    private final int fruitsPerSpawn = 2;
    private final int fruitLifeSpan = 600;

    public ObjectManager(GameController gameController) {
        this.gameController = gameController;
        objects = new ArrayList<>();
        ghosts = new ArrayList<>();
        fruits = new ArrayList<>();
    }

    public void setupObjects(int[][] mapData) {
        objects.clear();
        ghosts.clear();
        fruits.clear();
        fruitsSpawnedAtThird = 0;
        fruitsSpawnedAtTwoThirds = 0;
        totalCoins = 0;

        Blinky blinky = null;

        for (int row = 0; row < gameController.maxScreenRow; row++) {
            for (int col = 0; col < gameController.maxScreenCol; col++) {
                int code = mapData[row][col];
                int x = col * gameController.tileSize;
                int y = row * gameController.tileSize;

                switch (code) {
                    case 5:
                        objects.add(new Coin(gameController, x, y));
                        totalCoins++;
                        break;
                    case 6:
                        gameController.player.x = x;
                        gameController.player.y = y;
                        break;
                    case 7:
                        ghosts.add(new Pinky(gameController, x, y));
                        break;
                    case 8:
                        blinky = new Blinky(gameController, x, y);
                        ghosts.add(blinky);
                        break;
                    case 9:
                        objects.add(new PowerPellet(gameController, x, y));
                        break;
                    case 10:
                        if (blinky != null) {
                            ghosts.add(new Inky(gameController, x, y, blinky));
                        } else {
                            ghosts.add(new Inky(gameController, x, y, null));
                        }
                        break;
                    case 11:
                        ghosts.add(new Clyde(gameController, x, y));
                        break;
                }
            }
        }
    }

    public void update() {
        GameRulesService gameRulesService = gameController.gameRulesService;

        for (Ghost ghost : ghosts) {
            ghost.update();
            if (gameController.player.collidesWith(ghost)) {
                GhostEncounterOutcome outcome = gameRulesService.evaluateGhostEncounter(
                        ghost.isFrightened(), ghost.isEaten(), gameController.player.getLives());

                switch (outcome.getType()) {
                    case EAT_GHOST:
                        ghost.setEaten(true);
                        gameController.player.addScore(outcome.getScorePoints());
                        break;
                    case LOSE_LIFE:
                        gameController.player.loseLife();
                        System.out.println("חיים נותרו: " + gameController.player.getLives());
                        gameController.player.resetPosition();
                        for (Ghost g : ghosts) {
                            g.resetToHome();
                        }
                        return;
                    case GAME_OVER:
                        gameController.player.loseLife();
                        System.out.println("GAME OVER!");
                        gameController.gameOver();
                        return;
                    default:
                        break;
                }
            }
        }

        Iterator<Entity> objectIterator = objects.iterator();
        while (objectIterator.hasNext()) {
            Entity obj = objectIterator.next();
            if (gameController.player.collidesWith(obj)) {
                CollectibleOutcome outcome = gameRulesService.evaluateCollectible(resolveCollectibleType(obj));
                if (outcome.getType() != CollectibleOutcome.Type.NONE) {
                    gameController.player.addScore(outcome.getScorePoints());
                    objectIterator.remove();
                    if (outcome.shouldTriggerFrightenedMode()) {
                        setAllGhostsFrightened(true);
                        gameController.startFrightenedTimer();
                    }
                }
            }
        }

        int collectedCoinsAfter = gameRulesService.getCollectedCoinCount(totalCoins, countRemainingCoins());

        Iterator<Fruit> fruitIterator = fruits.iterator();
        while (fruitIterator.hasNext()) {
            Fruit fruit = fruitIterator.next();
            fruit.update();
            if (fruit.shouldRemove()) {
                fruitIterator.remove();
            }
        }
        if (gameRulesService.shouldSpawnFruitsAtThird(fruitsSpawnedAtThird, collectedCoinsAfter, totalCoins)) {
            spawnMultipleFruits(fruitsPerSpawn);
            fruitsSpawnedAtThird = 1;
        }
        if (gameRulesService.shouldSpawnFruitsAtTwoThirds(fruitsSpawnedAtTwoThirds, collectedCoinsAfter, totalCoins)) {
            spawnMultipleFruits(fruitsPerSpawn);
            fruitsSpawnedAtTwoThirds = 1;
        }
    }

    private GameRulesService.CollectibleType resolveCollectibleType(Entity obj) {
        if (obj instanceof Coin) {
            return GameRulesService.CollectibleType.COIN;
        }
        if (obj instanceof PowerPellet) {
            return GameRulesService.CollectibleType.POWER_PELLET;
        }
        return GameRulesService.CollectibleType.NONE;
    }

    private int countRemainingCoins() {
        int remainingCoins = 0;
        for (Entity obj : objects) {
            if (obj instanceof Coin) {
                remainingCoins++;
            }
        }
        return remainingCoins;
    }

    private void spawnMultipleFruits(int count) {
        Random rand = new Random();
        int created = 0;
        while (created < count) {
            int row = rand.nextInt(gameController.maxScreenRow);
            int col = rand.nextInt(gameController.maxScreenCol);
            if (gameController.collisionService.isWalkable(row, col)) {
                int x = col * gameController.tileSize;
                int y = row * gameController.tileSize;
                fruits.add(new Fruit(gameController, x, y, fruitLifeSpan));
                created++;
            }
        }
    }

    public void setAllGhostsFrightened(boolean state) {
        for (Ghost ghost : ghosts) {
            ghost.setFrightened(state);
        }
    }
}
