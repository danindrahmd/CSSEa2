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

    private int xpos;
    private int ypos;
    private final TickTimer timer;

    /**
     * Creates a spawner at the given location with a default spawn interval.
     *
     * @param xpos the x coordinate of the spawner
     * @param ypos the y coordinate of the spawner
     * @param defaultDuration the default spawn interval in ticks
     */
    protected AbstractEnemySpawner(int xpos, int ypos, int defaultDuration) {
        this.xpos = xpos;
        this.ypos = ypos;
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
        return xpos;
    }

    @Override
    public void setX(int xpos) {
        this.xpos = xpos;
    }

    @Override
    public int getY() {
        return ypos;
    }

    @Override
    public void setY(int ypos) {
        this.ypos = ypos;
    }
}