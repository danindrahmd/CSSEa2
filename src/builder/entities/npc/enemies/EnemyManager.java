package builder.entities.npc.enemies;

import builder.GameState;
import builder.Tickable;
import builder.entities.Interactable;
import builder.entities.npc.spawners.Spawner;
import builder.player.Player;
import builder.ui.RenderableGroup;

import engine.EngineState;
import engine.game.HasPosition;
import engine.renderer.Dimensions;
import engine.renderer.Renderable;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all enemy spawners and active enemies in the game.
 * Responsible for spawning, updating, and cleaning up enemies.
 */
public class EnemyManager implements Tickable, Interactable, RenderableGroup {

    private final ArrayList<Spawner> spawners = new ArrayList<>();
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private int spawnX;
    private int spawnY;

    /**
     * Creates a new enemy manager.
     *
     * @param dimensions the game dimensions (currently unused but kept for compatibility)
     */
    public EnemyManager(Dimensions dimensions) {}

    /**
     * Removes all enemies marked for removal from the active enemy list.
     */
    public void cleanup() {
        enemies.removeIf(Enemy::isMarkedForRemoval);
    }

    /**
     * Adds a spawner to the manager.
     *
     * @param spawner the spawner to add
     */
    public void add(Spawner spawner) {
        this.spawners.add(spawner);
    }

    /**
     * Sets the spawn coordinates for the next enemy to be spawned.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void setSpawnLocation(int x, int y) {
        this.spawnX = x;
        this.spawnY = y;
    }

    /**
     * Creates and adds a Magpie to the game.
     *
     * @param player the player to target
     * @return the created Magpie
     */
    public Magpie mkM(Player player) {
        final Magpie magpie = new Magpie(this.spawnX, this.spawnY, player);
        this.enemies.add(magpie);
        return magpie;
    }

    /**
     * Creates and adds a Pigeon to the game.
     *
     * @param target the target position (usually a cabbage)
     * @return the created Pigeon
     */
    public Pigeon mkP(HasPosition target) {
        final Pigeon pigeon = new Pigeon(this.spawnX, this.spawnY, target);
        this.enemies.add(pigeon);
        return pigeon;
    }

    /**
     * Creates and adds an Eagle to the game.
     *
     * @param player the player to target
     * @return the created Eagle
     */
    public Eagle mkE(Player player) {
        final Eagle eagle = new Eagle(this.spawnX, this.spawnY, player);
        this.enemies.add(eagle);
        return eagle;
    }

    /**
     * Gets all active enemies.
     * Returns a defensive copy to prevent external modification.
     *
     * @return a list of all active enemies
     */
    public List<Enemy> getAllEnemies() {
        return new ArrayList<>(this.enemies);
    }

    /**
     * Gets all Magpies currently in the game.
     *
     * @return a list of all active Magpies
     */
    public List<Magpie> getMagpies() {
        final ArrayList<Magpie> magpies = new ArrayList<>();
        for (Enemy enemy : enemies) {
            if (enemy instanceof Magpie) {
                magpies.add((Magpie) enemy);
            }
        }
        return magpies;
    }

    @Override
    public void tick(EngineState state, GameState game) {
        this.cleanup();

        // Tick all spawners
        for (Spawner spawner : this.spawners) {
            spawner.tick(state, game);
        }

        // Tick all enemies (polymorphism - no instanceof needed!)
        for (Enemy enemy : enemies) {
            enemy.tick(state, game);
        }
    }

    @Override
    public void interact(EngineState state, GameState game) {
        // Currently no interaction needed for enemies
    }

    @Override
    public List<Renderable> render() {
        return new ArrayList<>(this.enemies);
    }
}