package builder.entities.npc;

import builder.GameState;
import builder.entities.npc.enemies.Enemy;
import builder.entities.npc.enemies.Magpie;
import builder.entities.npc.enemies.Pigeon;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;

import java.util.List;

/**
 * A scarecrow that scares away Magpies and Pigeons within a certain radius.
 * When birds get too close, they become frightened and return to their spawn.
 */
public class Scarecrow extends Npc {

    public static final int COIN_COST = 2;
    private static final int SCARE_RADIUS_MULTIPLIER = 4;
    private static final SpriteGroup art = SpriteGallery.scarecrow;

    /**
     * Creates a new scarecrow at the given position.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Scarecrow(int x, int y) {
        super(x, y);
        this.setSprite(art.getSprite("default"));
        this.setSpeed(0);
    }

    @Override
    public void tick(EngineState state) {
        super.tick(state);
    }

    @Override
    public void interact(EngineState state, GameState game) {
        super.interact(state, game);

        final int scareRadius = state.getDimensions().tileSize() * SCARE_RADIUS_MULTIPLIER;
        List<Enemy> enemies = game.getEnemies().getAllEnemies();

        for (Enemy enemy : enemies) {
            if (this.distanceFrom(enemy) < scareRadius) {
                if (enemy instanceof Magpie magpie) {
                    magpie.attacking = false;
                } else if (enemy instanceof Pigeon pigeon) {
                    pigeon.attacking = false;
                }
            }
        }
    }
}