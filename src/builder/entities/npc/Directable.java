package builder.entities.npc;

/**
 * Represents an entity that can be directed or have a direction.
 * Entities implementing this interface can have their direction set,
 * retrieved, and can indicate whether their direction is changeable.
 */
public interface Directable {

    /**
     * Gets the current direction of this entity.
     * @return the current direction
     */
    public int getDirection();

    /**
     * Sets the direction of this entity.
     * @param direction the direction to set
     */
    public void setDirection(int direction);

    /**
     * Determines if this entity's direction can be changed.
     */
    public void move();
}
