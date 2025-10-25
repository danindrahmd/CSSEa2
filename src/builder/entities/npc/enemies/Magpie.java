package builder.entities.npc.enemies;

import builder.GameState;
import builder.entities.npc.Expirable;
import builder.player.Player;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;
import engine.game.HasPosition;
import engine.timing.FixedTimer;
import engine.timing.RepeatingTimer;

/**
 * Represents a Magpie enemy that collects coins and attacks targets.
 * Magpies are attracted to shiny objects and will steal coins from the player,
 * then return to their spawn point.
 */
public class Magpie extends Enemy implements Expirable {

    private static final SpriteGroup art = SpriteGallery.magpie;
    private FixedTimer lifespan = new FixedTimer(10000);
    private HasPosition trackedTarget;
    private Boolean attacking;
    private int coins = 0;

    private RepeatingTimer directionalUpdateTimer = new RepeatingTimer(30);

    private final int spawnX;
    private final int spawnY;

    /**
     * Creates a new Magpie at the specified position that tracks the given target.
     *
     * @param xcoordinate the initial X coordinate
     * @param ycoordinate the initial Y coordinate
     * @param trackedTarget the target entity to track and steal from
     */
    public Magpie(int xcoordinate, int ycoordinate, HasPosition trackedTarget) {
        super(xcoordinate, ycoordinate);
        this.spawnX = xcoordinate;
        this.spawnY = ycoordinate;
        this.trackedTarget = trackedTarget;
        this.setSprite(art.getSprite("down"));
        this.attacking = true;

        double deltaX = trackedTarget.getX() - this.getX();
        double deltaY = trackedTarget.getY() - this.getY();
        this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
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
     * Checks if the magpie is currently attacking.
     *
     * @return true if attacking, false otherwise
     */
    public Boolean isAttacking() {
        return attacking;
    }

    /**
     * Sets the attacking state of the magpie.
     *
     * @param attacking true to set attacking, false otherwise
     */
    public void setAttacking(Boolean attacking) {
        this.attacking = attacking;
    }

    /**
     * Gets the number of coins collected by this magpie.
     *
     * @return the number of coins
     */
    public int getCoins() {
        return coins;
    }

    /**
     * Sets the number of coins collected by this magpie.
     *
     * @param coins the number of coins
     */
    public void setCoins(int coins) {
        this.coins = coins;
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
        this.lifespan.tick();
        if (this.lifespan.isFinished()) {
            this.markForRemoval();
        }
        if (this.attacking) {
            double deltaX = trackedTarget.getX() - this.getX();
            double deltaY = trackedTarget.getY() - this.getY();
            this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
            // Target is below
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
        this.move();
        this.directionalUpdateTimer.tick();

        Player player = game.getPlayer();

        final boolean hasHitPlayer =
                this.distanceFrom(player.getX(), player.getY()) < engine.getDimensions().tileSize();
        if (hasHitPlayer && game.getInventory().getCoins() > 0 && this.attacking) {
            game.getInventory().addCoins(-1);
            this.coins += 1;
            this.attacking = false;
            this.setSpeed(2); // book it
        }

        if (!attacking) {
            if (this.distanceFrom(spawnX, spawnY) < engine.getDimensions().tileSize()) {
                this.markForRemoval();
            }
        }

        if (this.isMarkedForRemoval() && attacking) {
            game.getInventory().addCoins(this.coins);
        }
    }

    @Override
    public void interact(EngineState engine, GameState game) {}
}