package builder.entities.npc.spawners;

import builder.GameState;
import engine.EngineState;

/**
 * Spawner for Magpie enemies.
 * Spawns magpies at regular intervals.
 */
public class MagpieSpawner extends AbstractEnemySpawner {

    private static final int DEFAULT_DURATION = 360;

    /**
     * Creates a magpie spawner with default spawn interval.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public MagpieSpawner(int x, int y) {
        super(x, y, DEFAULT_DURATION);
    }

    /**
     * Creates a magpie spawner with custom spawn interval.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param duration the spawn interval in ticks
     */
    public MagpieSpawner(int x, int y, int duration) {
        super(x, y, duration);
    }

    @Override
    protected void spawnEnemy(EngineState state, GameState game) {
        game.getEnemies().setSpawnLocation(getX(), getY());
        game.getEnemies().mkM(game.getPlayer());
    }
}