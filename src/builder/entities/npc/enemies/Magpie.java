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
 * Magpie enemy that attacks the player and steals coins.
 * After stealing a coin, the magpie returns to its spawn location.
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
     * Creates a new Magpie enemy.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param target the target to track (usually the player)
     */
    public Magpie(int x, int y, HasPosition target) {
        super(x, y);
        this.spawnX = x;
        this.spawnY = y;
        this.trackedTarget = target;
        this.setSprite(art.getSprite("down"));
        this.attacking = true;

        double deltaX = target.getX() - this.getX();
        double deltaY = target.getY() - this.getY();
        this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
    }

    /**
     * Gets the tracked target.
     *
     * @return the tracked target
     */
    public HasPosition getTrackedTarget() {
        return trackedTarget;
    }

    /**
     * Sets the tracked target.
     *
     * @param target the new tracked target
     */
    public void setTrackedTarget(HasPosition target) {
        this.trackedTarget = target;
    }

    /**
     * Checks if the magpie is attacking.
     *
     * @return true if attacking, false otherwise
     */
    public Boolean isAttacking() {
        return attacking;
    }

    /**
     * Sets whether the magpie is attacking.
     * Used by Scarecrow to make magpies flee.
     *
     * @param attacking true to attack, false to flee
     */
    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    /**
     * Gets the number of coins stolen.
     *
     * @return the number of coins
     */
    public int getCoins() {
        return coins;
    }

    /**
     * Sets the number of coins stolen.
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

    /**
     * Handles interaction behaviour with the player.
     *
     * @param engine The engine state
     * @param game The state of the game, including the player and world. Can be used to query or
     *     update the game state.
     */
    @Override
    public void interact(EngineState engine, GameState game) {
        // no-op for Magpie
    }
}