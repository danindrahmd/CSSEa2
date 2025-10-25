package builder.entities.npc.enemies;

import builder.GameState;
import builder.entities.npc.Expirable;
import engine.EngineState;
import engine.game.HasPosition;
import engine.timing.FixedTimer;

/**
 * Abstract base class for bird enemies that attack targets and return to spawn.
 * Provides common behavior for tracking targets, returning to spawn, and lifespan management.
 */
public abstract class AbstractBird extends Enemy implements Expirable {

    protected final int spawnX;
    protected final int spawnY;
    protected HasPosition trackedTarget;
    public boolean attacking;
    private FixedTimer lifespan;

    /**
     * Creates a new bird enemy.
     *
     * @param x the spawn x coordinate
     * @param y the spawn y coordinate
     * @param trackedTarget the initial target to attack
     * @param lifespanDuration the lifespan duration in ticks
     */
    protected AbstractBird(int x, int y, HasPosition trackedTarget, int lifespanDuration) {
        super(x, y);
        this.spawnX = x;
        this.spawnY = y;
        this.trackedTarget = trackedTarget;
        this.attacking = true;
        this.lifespan = new FixedTimer(lifespanDuration);
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
     * Updates the bird's direction to face towards a target position.
     *
     * @param targetX the target x coordinate
     * @param targetY the target y coordinate
     */
    protected void faceTowards(int targetX, int targetY) {
        double deltaX = targetX - this.getX();
        double deltaY = targetY - this.getY();
        this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
    }

    /**
     * Updates the bird's direction to face towards the spawn point.
     */
    protected void faceTowardsSpawn() {
        faceTowards(spawnX, spawnY);
    }

    /**
     * Checks if the bird has reached its spawn point.
     *
     * @param engine the engine state
     * @return true if at spawn, false otherwise
     */
    protected boolean isAtSpawn(EngineState engine) {
        return this.distanceFrom(spawnX, spawnY) < engine.getDimensions().tileSize();
    }

    /**
     * Handles the bird's return to spawn behavior.
     * When the bird reaches spawn, it is marked for removal.
     *
     * @param engine the engine state
     * @param game the game state
     */
    protected void handleReturnToSpawn(EngineState engine, GameState game) {
        faceTowardsSpawn();
        if (isAtSpawn(engine)) {
            this.markForRemoval();
        }
    }

    @Override
    public void tick(EngineState state, GameState game) {
        super.tick(state, game);

        // Handle lifespan
        lifespan.tick();
        if (lifespan.isFinished()) {
            this.markForRemoval();
        }

        // Subclasses implement specific behavior
        if (attacking) {
            handleAttacking(state, game);
        } else {
            handleReturnToSpawn(state, game);
        }
    }

    /**
     * Handles the bird's attacking behavior.
     * Subclasses must implement their specific attacking logic.
     *
     * @param state the engine state
     * @param game the game state
     */
    protected abstract void handleAttacking(EngineState state, GameState game);
}