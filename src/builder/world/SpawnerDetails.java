package builder.world;

import engine.game.HasPosition;

/**
 * Contains details about entity spawners in the game world.
 * Provides information about spawner position and spawn duration.
 */
public interface SpawnerDetails extends HasPosition {
    /**
     * Gets the X coordinate of the spawner.
     *
     * @return the X coordinate
     */
    public int getX();

    /**
     * Gets the Y coordinate of the spawner.
     *
     * @return the Y coordinate
     */
    public int getY();

    /**
     * Sets the X coordinate of the spawner.
     *
     * @param x the new X coordinate
     */
    public void setX(int x);

    /**
     * Sets the Y coordinate of the spawner.
     *
     * @param y the new Y coordinate
     */
    public void setY(int y);

    /**
     * Gets the spawn duration or interval for this spawner.
     *
     * @return the duration in game ticks
     */
    public int getDuration();
}