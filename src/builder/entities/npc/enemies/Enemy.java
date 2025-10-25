package builder.entities.npc.enemies;

import builder.GameState;
import builder.entities.npc.Npc;

import engine.EngineState;

/**
 * Base class for all enemy entities.
 */
public abstract class Enemy extends Npc {

    public Enemy(int x, int y) {
        super(x, y);
    }

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