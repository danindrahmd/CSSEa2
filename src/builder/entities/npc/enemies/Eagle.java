package builder.entities.npc.enemies;

import builder.GameState;
import builder.entities.npc.Expirable;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;
import engine.game.HasPosition;
import engine.timing.FixedTimer;

/**
 * Represents an Eagle enemy that tracks and attacks targets to steal food.
 * Eagles are aggressive birds that pursue their targets, steal food when close enough,
 * and then return to their spawn point at increased speed.
 */
public class Eagle extends Enemy implements Expirable {

    private static final SpriteGroup art = SpriteGallery.eagle;
    private FixedTimer lifespan = new FixedTimer(5000);
    private HasPosition trackedTarget;
    private boolean attacking = true;
    private int spawnX = 0;
    private int spawnY = 0;
    private int food = 0;

    /**
     * Creates a new Eagle at the specified position that tracks the given target.
     *
     * @param x the initial X coordinate
     * @param y the initial Y coordinate
     * @param trackedTarget the target entity to track and attack
     */
    public Eagle(int x, int y, HasPosition trackedTarget) {
        super(x, y);
        this.spawnX = x;
        this.spawnY = y;

        // derive direction based on where the eagle is and the initial target is
        int direction = 20;
        this.setDirection(direction);
        this.setSpeed(2);
        this.trackedTarget = trackedTarget;

        this.setSprite(art.getSprite("default"));

        if (attacking) {
            double deltaX = trackedTarget.getX() - this.getX();
            double deltaY = trackedTarget.getY() - this.getY();
            this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
        } else {
            double deltaX = trackedTarget.getX() - this.getX();
            double deltaY = trackedTarget.getY() - this.getY();
            this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
        }
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
        super.tick(engine);
        this.lifespan.tick();
        if (this.lifespan.isFinished()) {
            this.markForRemoval();
        }
        if ((this.distanceFrom(game.getPlayer().getX(), game.getPlayer().getY())
                < engine.getDimensions().tileSize())
                && this.attacking) {
            this.attacking = false;
            //      if (game.getInventory().getFood() > 0) {
            if (this.food == 0) {
                game.getInventory().addFood(-3);
                this.food = 3;
            }
            this.setSpeed(4); // the eagle BOOKS it once it has the food
            //      }
        }
        if ((this.distanceFrom(this.spawnX, this.spawnY) < engine.getDimensions().tileSize())
                && !this.attacking) {
            this.markForRemoval();
        }
        this.move();

        if (attacking) {
            double deltaX = trackedTarget.getX() - this.getX();
            double deltaY = trackedTarget.getY() - this.getY();
            this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
            if (trackedTarget.getY() > this.getY()) {
                this.setSprite(art.getSprite("down"));
            } else {
                this.setSprite(art.getSprite("up"));
            }
        } else {
            double deltaX = this.spawnX - this.getX();
            double deltaY = this.spawnY - this.getY();
            this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
            if (this.spawnY < this.getY()) {
                this.setSprite(art.getSprite("up"));
            } else {
                this.setSprite(art.getSprite("down"));
            }
        }

        if (this.isMarkedForRemoval()
                && this.distanceFrom(this.spawnX, this.spawnY)
                > engine.getDimensions().tileSize()) {
            game.getInventory().addFood(this.food);
        }
    }
}