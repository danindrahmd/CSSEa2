package builder.entities.npc;

import builder.GameState;
import builder.Tickable;
import builder.entities.Interactable;

import engine.EngineState;
import engine.game.Entity;
import engine.game.HasPosition;

/**
 * Represents a non-player character (NPC) in the game.
 * NPCs can move, interact with players, and be directed in various ways.
 * They have a position, direction, and speed that determines their movement.
 */
public class Npc extends Entity implements Interactable, Tickable, Directable {

    private int direction = 0;
    private double speed = 1;

    /**
     * Creates a new NPC at the specified coordinates.
     *
     * @param x the initial X coordinate
     * @param y the initial Y coordinate
     */
    public Npc(int x, int y) {
        super(x, y);
    }

    /**
     * Gets the current speed of this NPC.
     *
     * @return the speed value
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Sets the speed of this NPC.
     *
     * @param speed the new speed value
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Gets the current direction of this NPC in degrees.
     *
     * @return the direction in degrees
     */
    public int getDirection() {
        return this.direction;
    }

    /**
     * Sets the direction of this NPC.
     *
     * @param direction the new direction in degrees
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * Moves the NPC based on its current direction and speed.
     * Adjusts the X and Y coordinates accordingly.
     */
    public void move() {
        final int deltaX = (int) Math.round(Math.cos(Math.toRadians(this.direction)) * this.speed);
        final int deltaY = (int) Math.round(Math.sin(Math.toRadians(this.direction)) * this.speed);
        this.setX(this.getX() + deltaX);
        this.setY(this.getY() + deltaY);
    }

    /**
     * Updates the NPC state for the current game tick.
     * Moves the NPC based on its direction and speed.
     *
     * @param state the current engine state
     */
    @Override
    public void tick(EngineState state) {
        this.move();
    }

    /**
     * Updates the NPC state for the current game tick.
     * Moves the NPC based on its direction and speed.
     *
     * @param state the current engine state
     * @param game the current game state
     */
    @Override
    public void tick(EngineState state, GameState game) {
        this.move();
    }

    /**
     * Handles interaction with this NPC.
     * Default implementation does nothing.
     *
     * @param state the current engine state
     * @param game the current game state
     */
    @Override
    public void interact(EngineState state, GameState game) {}

    /**
     * Returns how far away this NPC is from the given position.
     *
     * @param position the position to measure distance to
     * @return the distance from this NPC to the given position
     */
    public int distanceFrom(HasPosition position) {
        int deltaX = position.getX() - this.getX();
        int deltaY = position.getY() - this.getY();
        return (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    /**
     * Returns how far away this NPC is from the given coordinates.
     *
     * @param xcoordinate the x coordinate to measure distance to
     * @param ycoordinate the y coordinate to measure distance to
     * @return the distance from this NPC to the given coordinates
     */
    public int distanceFrom(int xcoordinate, int ycoordinate) {
        int deltaX = xcoordinate - this.getX();
        int deltaY = ycoordinate - this.getY();
        return (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}