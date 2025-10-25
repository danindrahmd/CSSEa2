package builder.entities.npc.spawners;

import builder.GameState;
import builder.entities.npc.BeeHive;

import engine.EngineState;
import engine.timing.RepeatingTimer;
import engine.timing.TickTimer;

/**
 * Spawner that allows players to create BeeHives by spending resources.
 * Players can spawn a BeeHive at their current position by pressing 'h'
 * when they have at least 3 food and 3 coins.
 */
public class BeeHiveSpawner implements Spawner {

    private RepeatingTimer timer;
    private int xPos = 0;
    private int yPos = 0;

    /**
     * Creates a new BeeHive spawner at the specified position.
     *
     * @param xPos the X coordinate of the spawner
     * @param yPos the Y coordinate of the spawner
     * @param duration the timer duration (currently unused)
     */
    public BeeHiveSpawner(int xPos, int yPos, int duration) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.timer = new RepeatingTimer(300);
    }

    /**
     * Gets the timer for this spawner.
     *
     * @return the timer
     */
    public RepeatingTimer getTimer() {
        return this.timer;
    }

    /**
     * Sets the timer for this spawner.
     *
     * @param timer the new timer
     */
    public void setTimer(RepeatingTimer timer) {
        this.timer = timer;
    }

    @Override
    public void tick(EngineState state, GameState game) {
        timer.tick();
        final boolean canAfford =
                game.getInventory().getFood() >= 3 && game.getInventory().getCoins() >= 3;

        if (canAfford && state.getKeys().isDown('h')) {
            game.getInventory().addFood(-3);
            game.getInventory().addCoins(-3);
            game.getNpcs().addNpc(new BeeHive(game.getPlayer().getX(), game.getPlayer().getY()));
        }
    }

    @Override
    public int getX() {
        return this.xPos;
    }

    @Override
    public void setX(int xPos) {
        this.xPos = xPos;
    }

    @Override
    public int getY() {
        return this.yPos;
    }

    @Override
    public void setY(int yPos) {
        this.yPos = yPos;
    }
}