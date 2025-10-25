package builder.entities.npc.spawners;

import builder.GameState;
import builder.Tickable;

import engine.EngineState;
import engine.game.HasPosition;
import engine.timing.TickTimer;

/**
 * A spawner is responsible for spawning specific types of {@link builder.entities.npc.Npc}s or
 * {@link builder.entities.npc.enemies.Enemy}s
 */
public interface Spawner extends HasPosition, Tickable {

    /**
     * Gets the timer associated with this spawner.
     *
     * @return the tick timer
     */
    public TickTimer getTimer();

    /**
     * Updates the spawner state for the current game tick.
     * Handles spawn timing and logic.
     *
     * @param state the current engine state
     * @param game the current game state
     */
    @Override
    public void tick(EngineState state, GameState game);

    @Override
    public int getX();

    @Override
    public void setX(int x);

    @Override
    public int getY();

    @Override
    public void setY(int y);
}