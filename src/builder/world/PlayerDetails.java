package builder.world;

import engine.game.ImmutablePosition;

/**
 * Contains player-specific details and state information.
 * Provides access to the player's starting resources and position.
 */
public interface PlayerDetails extends ImmutablePosition {
    /**
     * Gets the player's starting food amount.
     *
     * @return the starting food amount
     */
    int getStartingFood();

    /**
     * Gets the player's starting coin amount.
     *
     * @return the starting coin amount
     */
    int getStartingCoins();
}