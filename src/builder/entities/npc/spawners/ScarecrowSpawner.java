package builder.entities.npc.spawners;

import builder.GameState;
import builder.entities.npc.Scarecrow;

import engine.EngineState;
import engine.timing.RepeatingTimer;
import engine.timing.TickTimer;

/**
 * Spawner that allows players to create Scarecrows by spending coins.
 * Players can spawn a Scarecrow at their current position by pressing 'c'
 * when they have at least 2 coins.
 */
public class ScarecrowSpawner implements Spawner {

    private int xPos = 0;
    private int yPos = 0;
    private RepeatingTimer timer = new RepeatingTimer(300);

    /**
     * Creates a new Scarecrow spawner at the specified position.
     *
     * @param xPos the X coordinate of the spawner
     * @param yPos the Y coordinate of the spawner
     */
    public ScarecrowSpawner(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    @Override
    public TickTimer getTimer() {
        return this.timer;
    }

    @Override
    public void tick(EngineState state, GameState game) {
        this.timer.tick();
        // Look at use code to spawn
        if (game.getInventory().getCoins() >= 2 && state.getKeys().isDown('c')) {
            game.getInventory().addCoins(-2);
            game.getNpcs().addNpc(new Scarecrow(game.getPlayer().getX(), game.getPlayer().getY()));
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