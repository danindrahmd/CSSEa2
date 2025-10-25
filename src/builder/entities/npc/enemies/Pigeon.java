package builder.entities.npc.enemies;

import builder.GameState;
import builder.entities.npc.Expirable;
import builder.entities.resources.Cabbage;
import builder.entities.tiles.Tile;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;
import engine.game.Entity;
import engine.game.HasPosition;
import engine.timing.FixedTimer;

import java.util.List;

/**
 * Represents a Pigeon enemy that tracks and eats cabbages.
 * Pigeons search for cabbages, consume them when close enough,
 * and return to their spawn point after eating.
 */
public class Pigeon extends Enemy implements Expirable {

    private static final SpriteGroup art = SpriteGallery.pigeon;
    private FixedTimer lifespan = new FixedTimer(3000);
    private HasPosition trackedTarget;
    private Boolean attacking = true;
    private int spawnX = 0;
    private int spawnY = 0;

    /**
     * Creates a new Pigeon at the specified position.
     *
     * @param x the initial X coordinate
     * @param y the initial Y coordinate
     */
    public Pigeon(int x, int y) {
        super(x, y);
        this.spawnX = x;
        this.spawnY = y;
    }

    /**
     * Creates a new Pigeon at the specified position with a tracked target.
     *
     * @param x the initial X coordinate
     * @param y the initial Y coordinate
     * @param trackedTarget the target to track
     */
    public Pigeon(int x, int y, HasPosition trackedTarget) {
        super(x, y);
        this.spawnX = x;
        this.spawnY = y;
        this.trackedTarget = trackedTarget;
        this.setSpeed(1);
        this.setSprite(art.getSprite("down"));  // ← add ths
    }

    /**
     * Gets the tracked target entity.
     *
     * @return the tracked target
     */
    public HasPosition getTrackedTarget() {
        return trackedTarget;
    }

    /**
     * Sets the tracked target entity.
     *
     * @param trackedTarget the target to track
     */
    public void setTrackedTarget(HasPosition trackedTarget) {
        this.trackedTarget = trackedTarget;
    }

    /**
     * Checks if the pigeon is currently attacking.
     *
     * @return true if attacking, false otherwise
     */
    public Boolean isAttacking() {
        return attacking;
    }

    /**
     * Sets the attacking state of the pigeon.
     *
     * @param attacking true to set attacking, false otherwise
     */
    public void setAttacking(Boolean attacking) {
        this.attacking = attacking;
    }

    @Override
    public FixedTimer getLifespan() {
        return lifespan;
    }

    @Override
    public void setLifespan(FixedTimer timer) {
        this.lifespan = timer;
    }

    @Override
    public void tick(EngineState engine, GameState game) {
        super.tick(engine, game);
        if (!this.attacking) {
            double deltaX = (this.spawnX - this.getX());
            double deltaY = (this.spawnY - this.getY());
            this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));

            // Get close to spawn
            if (this.distanceFrom(this.spawnX, this.spawnY)
                    < engine.getDimensions().tileSize()) {
                this.markForRemoval();
            }
            if (this.spawnY < this.getY()) {
                this.setSprite(art.getSprite("up"));
            } else {
                this.setSprite(art.getSprite("down"));
            }
        }
        this.move();
        // If the pigeon has no target, it should go to the center of the screen if its hunting
        if (this.trackedTarget == null && this.attacking) {
            double deltaX = ((double) engine.getDimensions().windowSize() / 2 - this.getX());
            double deltaY = ((double) engine.getDimensions().windowSize() / 2 - this.getY());
            this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
            if (trackedTarget.getY() > this.getY()) {
                this.setSprite(art.getSprite("down"));
            } else {
                this.setSprite(art.getSprite("up"));
            }
        } else {
            // do nothing
        }
        if (this.trackedTarget != null && this.attacking) {
            double deltaX = (this.trackedTarget.getX() - this.getX());
            double deltaY = (this.trackedTarget.getY() - this.getY());
            this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
        } else {
            // do nothing
        }
        this.lifespan.tick();
        if (this.lifespan.isFinished()) {
            this.markForRemoval();
        } else {
            // do nothing
        }
        if (!attacking) {
            if (this.distanceFrom(spawnX, spawnY) < engine.getDimensions().tileSize()) {
                this.markForRemoval();
            }
            if (this.spawnY < this.getY()) {
                this.setSprite(art.getSprite("up"));
            } else {
                this.setSprite(art.getSprite("down"));
            }
        }

        List<Tile> tiles =
                game.getWorld()
                        .tileSelector(
                                tile -> {
                                    for (Entity entity : tile.getStackedEntities()) {
                                        if (entity instanceof Cabbage) {
                                            return true;
                                        } else {
                                            // do nothing
                                        }
                                    }
                                    return false;
                                });
        if (tiles.size() > 0) {
            int distance = this.distanceFrom(tiles.getFirst());
            Tile closest = tiles.getFirst();
            for (Tile tile : tiles) {
                if (this.distanceFrom(tile) < distance) {
                    closest = tile;
                } else {
                    // do nothing
                }
            }
            this.trackedTarget = closest;

            if (this.attacking
                    && this.distanceFrom(this.trackedTarget) < engine.getDimensions().tileSize()) {
                for (Entity entity : closest.getStackedEntities()) {
                    if (entity instanceof Cabbage cabbage) {
                        cabbage.markForRemoval();
                        this.attacking = false;
                    } else {
                        // do nothing
                    }
                }
            }
        } else {
            // No cabbages to get
            this.attacking = false;
        }
    }
}