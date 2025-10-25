package builder.entities.npc.spawners;

import builder.GameState;
import engine.EngineState;

/**
 * Spawner for Eagle enemies.
 * Spawns eagles at regular intervals.
 */
public class EagleSpawner extends AbstractEnemySpawner {

    private static final int DEFAULT_DURATION = 1000;

    /**
     * Creates an eagle spawner with default spawn interval.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public EagleSpawner(int x, int y) {
        super(x, y, DEFAULT_DURATION);
    }

    /**
     * Creates an eagle spawner with custom spawn interval.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param duration the spawn interval in ticks
     */
    public EagleSpawner(int x, int y, int duration) {
        super(x, y, duration);
    }

    @Override
    protected void spawnEnemy(EngineState state, GameState game) {
        game.getEnemies().setSpawnLocation(getX(), getY());
        game.getEnemies().mkE(game.getPlayer());
    }
}