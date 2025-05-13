package game.achievements;

/**
 * Represents a tracker for player statistics.
 * Monitors number and accuracy of shots the player has fired since game start.
 */
public class PlayerStatsTracker {

    private final long startTime;
    private int shotsFired;
    private int shotsHit;

    /**
     * Constructs a PlayerStatsTracker with the current system time as the start time.
     */
    public PlayerStatsTracker() {
        this.startTime = System.currentTimeMillis();
    }

//    /**
//     * Constructs a PlayerStatsTracker with a custom start time.
//     *
//     * @param startTime the time when tracking began
//     */
//    public PlayerStatsTracker(long startTime) {
//        this.startTime = startTime;
//    }

    /**
     * Records the player firing one shot by incrementing shots fired.
     */
    public void recordShotFired() {
        shotsFired++;
    }

    /**
     * Records the player hitting one target by incrementing shots hit.
     */
    public void recordShotHit() {
        shotsHit++;
    }

    /**
     * Returns the total number of shots fired.
     *
     * @return the number of shots fired
     */
    public int getShotsFired() {
        return shotsFired;
    }

    /**
     * Returns the total number of successful hits.
     *
     * @return the number of shots hit
     */
    public int getShotsHit() {
        return shotsHit;
    }

    /**
     * Returns the number of seconds elapsed since tracking started.
     *
     * @return the elapsed time in seconds
     */
    public long getElapsedSeconds() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }

    /**
     * Returns the player's shooting accuracy as a decimal.
     * If no shots have been fired, accuracy is 0.0.
     *
     * @return the shooting accuracy percentage
     */
    public double getAccuracy() {
        if (shotsFired == 0) {
            return 0.0;
        }
        return (double) shotsHit / shotsFired;
    }
}
