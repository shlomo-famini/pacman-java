package game;

import controller.GameController;

public class CollisionChecker {
    GameController gameController;

    public CollisionChecker(GameController gameController) {
        this.gameController = gameController;
    }

    public boolean checkTileCollision(Entity entity) {
        if (entity.solidArea == null) {
            return false;
        }

        return gameController.collisionService.checkTileCollision(
                entity.x,
                entity.y,
                entity.solidArea.x,
                entity.solidArea.y,
                entity.solidArea.width,
                entity.solidArea.height,
                entity.direction,
                entity.speed);
    }
}
