package builder.entities.npc.spawners;

import builder.GameState;
import engine.EngineState;
import engine.timing.RepeatingTimer;
import engine.timing.TickTimer;

/**
 * Abstract base class for enemy spawners that spawn enemies at regular intervals.
 * Subclasses must implement the {@link #spawnEnemy(EngineState, GameState)} method
 * to define how their specific enemy type is spawned.
 */
public abstract class AbstractEnemySpawner implements Spawner {

    private int x;
    private int y;
    private final TickTimer timer;

    /**
     * Creates a spawner at the given location with a default spawn interval.
     *
     * @param x the x coordinate of the spawner
     * @param y the y coordinate of the spawner
     * @param defaultDuration the default spawn interval in ticks
     */
    protected AbstractEnemySpawner(int x, int y, int defaultDuration) {
        this.x = x;
        this.y = y;
        this.timer = new RepeatingTimer(defaultDuration);
    }

    @Override
    public TickTimer getTimer() {
        return timer;
    }

    @Override
    public void tick(EngineState state, GameState game) {
        timer.tick();
        if (timer.isFinished() && canSpawn(state, game)) {
            spawnEnemy(state, game);
        }
    }

    /**
     * Checks if the spawner can spawn an enemy.
     * Default implementation always returns true.
     * Subclasses can override to add spawn conditions.
     *
     * @param state the engine state
     * @param game the game state
     * @return true if enemy can be spawned, false otherwise
     */
    protected boolean canSpawn(EngineState state, GameState game) {
        return true;
    }

    /**
     * Spawns the specific enemy type.
     * Subclasses must implement this to spawn their specific enemy.
     *
     * @param state the engine state
     * @param game the game state
     */
    protected abstract void spawnEnemy(EngineState state, GameState game);

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }
}