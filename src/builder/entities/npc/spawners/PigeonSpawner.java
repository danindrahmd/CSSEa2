package builder.entities.npc.spawners;

import builder.GameState;
import builder.entities.resources.Cabbage;
import builder.entities.tiles.Tile;
import engine.EngineState;
import engine.game.Entity;
import engine.game.HasPosition;

import java.util.List;

/**
 * Spawner for Pigeon enemies.
 * Pigeons only spawn when there is cabbage in the world to steal.
 */
public class PigeonSpawner extends AbstractEnemySpawner {

    private static final int DEFAULT_DURATION = 100;

    /**
     * Creates a pigeon spawner with default spawn interval.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public PigeonSpawner(int x, int y) {
        super(x, y, DEFAULT_DURATION);
    }

    /**
     * Creates a pigeon spawner with custom spawn interval.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param duration the spawn interval in ticks
     */
    public PigeonSpawner(int x, int y, int duration) {
        super(x, y, duration);
    }

    @Override
    protected boolean canSpawn(EngineState state, GameState game) {
        // Only spawn if there is cabbage in the world
        return findClosestCabbage(game) != null;
    }

    @Override
    protected void spawnEnemy(EngineState state, GameState game) {
        Tile closestCabbage = findClosestCabbage(game);
        if (closestCabbage != null) {
            game.getEnemies().setSpawnLocation(getX(), getY());
            game.getEnemies().mkP(closestCabbage);
        }
    }

    /**
     * Finds the closest tile containing a cabbage.
     *
     * @param game the game state
     * @return the closest tile with cabbage, or null if no cabbage exists
     */
    private Tile findClosestCabbage(GameState game) {
        List<Tile> tilesWithCabbage = game.getWorld().tileSelector(tile -> {
            for (Entity entity : tile.getStackedEntities()) {
                if (entity instanceof Cabbage) {
                    return true;
                }
            }
            return false;
        });

        if (tilesWithCabbage.isEmpty()) {
            return null;
        }

        // Find closest cabbage
        Tile closest = tilesWithCabbage.get(0);
        int minDistance = distanceFrom(closest);

        for (Tile tile : tilesWithCabbage) {
            int distance = distanceFrom(tile);
            if (distance < minDistance) {
                closest = tile;
                minDistance = distance;
            }
        }

        return closest;
    }

    /**
     * Calculates distance from spawner to a position.
     *
     * @param position the target position
     * @return the distance
     */
    private int distanceFrom(HasPosition position) {
        int deltaX = position.getX() - getX();
        int deltaY = position.getY() - getY();
        return (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}