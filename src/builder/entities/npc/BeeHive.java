package builder.entities.npc;

import builder.GameState;
import builder.entities.npc.enemies.Enemy;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;
import engine.timing.RepeatingTimer;

import java.util.List;

/**
 * A beehive that spawns guard bees to defend against nearby enemies.
 */
public class BeeHive extends Npc {

    public static final int DETECTION_DISTANCE = 350;
    public static final int TIMER = 240;
    public static final int FOOD_COST = 2;
    public static final int COIN_COST = 2;
    private static final SpriteGroup art = SpriteGallery.hive;
    private boolean loaded = true;

    private final RepeatingTimer timer = new RepeatingTimer(TIMER);

    /**
     * Creates a new beehive.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public BeeHive(int x, int y) {
        super(x, y);
        this.setSprite(art.getSprite("default"));
        this.setSpeed(0);
    }

    @Override
    public void tick(EngineState state, GameState game) {
        super.tick(state);
        this.timer.tick();
    }

    @Override
    public void interact(EngineState state, GameState game) {
        super.interact(state, game);

        timer.tick();
        Npc npc = this.checkAndSpawnBee(game.getEnemies().getAllEnemies());
        if (npc != null) {
            game.getNpcs().addNpc(npc);
        }
        if (timer.isFinished()) {
            this.loaded = true;
        }
    }

    /**
     * Checks if a bee should be spawned to defend against nearby enemies.
     *
     * @param targets list of potential enemy targets
     * @return a new GuardBee if spawned, null otherwise
     */
    public Npc checkAndSpawnBee(List<Enemy> targets) {
        for (Enemy enemy : targets) {
            if (this.distanceFrom(enemy) < DETECTION_DISTANCE && this.loaded) {
                this.loaded = false;
                return new GuardBee(this.getX(), this.getY(), enemy);
            }
        }
        return null;
    }
}