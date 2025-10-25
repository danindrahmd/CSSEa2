package builder.entities.npc.enemies;

import builder.GameState;
import builder.entities.npc.Npc;

import engine.EngineState;

/**
 * Base class for all enemy entities.
 */
public abstract class Enemy extends Npc {

    /**
     * Creates a new Enemy at the specified position.
     *
     * @param x the initial X coordinate
     * @param y the initial Y coordinate
     */
    public Enemy(int x, int y) {
        super(x, y);
    }

    /**
     * Updates the enemy state for this game tick.
     *
     * @param state the current engine state
     * @param game the current game state
     */
    @Override
    public void tick(EngineState state, GameState game) {
        super.tick(state, game);
    }

    @Override
    public void interact(EngineState state, GameState game) {}

    /**
     * Makes the enemy stop attacking and return to spawn.
     * Default implementation does nothing - subclasses should override if needed.
     */
    public void scare() {
        // Default: do nothing
        // Subclasses with attacking behavior should override this
    }
}