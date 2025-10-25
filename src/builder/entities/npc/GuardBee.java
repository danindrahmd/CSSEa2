package builder.entities.npc;

import builder.GameState;
import builder.entities.npc.enemies.Enemy;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;
import engine.game.HasPosition;
import engine.timing.FixedTimer;

import java.util.List;

/**
 * A highly trained Guard Bee that protects the hive by attacking nearby enemies.
 * Guard bees track and eliminate threats, then return to their spawn point.
 */
public class GuardBee extends Npc implements Expirable {

    private final int spawnX;
    private final int spawnY;
    private static final int SPEED = 2;
    private static final int LOCK_ON_DISTANCE = 300;
    private static final SpriteGroup art = SpriteGallery.bee;
    private FixedTimer lifespan = new FixedTimer(300);
    private final HasPosition trackedTarget;

    /**
     * Creates a new guard bee.
     *
     * @param xcoordinate horizontal spawning position
     * @param ycoordinate vertical spawning position
     * @param trackedTarget target with a position we want this to track
     */
    public GuardBee(int xcoordinate, int ycoordinate, HasPosition trackedTarget) {
        super(xcoordinate, ycoordinate);
        this.setSprite(art.getSprite("default"));
        this.trackedTarget = trackedTarget;

        this.spawnX = xcoordinate;
        this.spawnY = ycoordinate;

        double deltaX = trackedTarget.getX() - this.getX();
        double deltaY = trackedTarget.getY() - this.getY();
        this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
        this.setSpeed(SPEED);
    }

    @Override
    public FixedTimer getLifespan() {
        return lifespan;
    }

    @Override
    public void setLifespan(FixedTimer timer) {
        this.lifespan = timer;
    }

    /**
     * Updates the bee's sprite based on its current direction.
     */
    public void updateArtBasedOnDirection() {
        boolean goingUp = (this.getDirection() >= 230 && this.getDirection() < 310);
        boolean goingDown = (this.getDirection() >= 40 && this.getDirection() < 140);
        boolean goingRight = (this.getDirection() >= 310 && this.getDirection() < 40);

        if (goingDown) {
            this.setSprite(art.getSprite("down"));
        } else if (goingUp) {
            this.setSprite(art.getSprite("up"));
        } else if (goingRight) {
            this.setSprite(art.getSprite("right"));
        } else {
            this.setSprite(art.getSprite("left"));
        }
    }

    @Override
    public void tick(EngineState state, GameState game) {
        super.tick(state);
        this.move();

        // If no target, return to spawn
        if (this.trackedTarget == null) {
            double deltaX = this.spawnX - this.getX();
            double deltaY = this.spawnY - this.getY();
            this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
            this.updateArtBasedOnDirection();
            lifespan.tick();
            if (lifespan.isFinished()) {
                this.markForRemoval();
            }
            return;
        }

        // Lock onto closest enemy within range
        List<Enemy> enemies = game.getEnemies().getAllEnemies();
        for (Enemy enemy : enemies) {
            if (this.distanceFrom(enemy) < LOCK_ON_DISTANCE) {
                double deltaX = enemy.getX() - this.getX();
                double deltaY = enemy.getY() - this.getY();
                this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
                break;
            }
        }

        // Check collision with enemies
        for (Enemy enemy : enemies) {
            if (this.distanceFrom(enemy) < state.getDimensions().tileSize()) {
                enemy.markForRemoval();
                this.markForRemoval();
            }
        }

        this.updateArtBasedOnDirection();
        lifespan.tick();
        if (lifespan.isFinished()) {
            this.markForRemoval();
        }
    }
}