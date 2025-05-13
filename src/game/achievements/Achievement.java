package game.achievements;

/**
 * Represents a single achievement with progress tracking and tier information.
 */
public interface Achievement {

    /**
     * Returns the unique name of the achievement.
     *
     * @return the achievement name
     */
    String getName();

    /**
     * Returns a description of what the achievement is for.
     *
     * @return the achievement description
     */
    String getDescription();

    /**
     * Returns the current progress as a value between 0.0 and 1.0.
     *
     * @return the achievement progress
     * @ensures 0.0 <= getProgress() <= 1.0
     */
    double getProgress();

    /**
     * Sets the progress to the specified value.
     *
     * @param newProgress the updated progress value
     * @requires 0.0 <= newProgress <= 1.0
     * @ensures getProgress() == min(1.0, max(0.0, newProgress))
     */
    void setProgress(double newProgress);

    /**
     * Returns the current tier based on progress.
     * - "Novice" if progress < 0.5
     * - "Expert" if 0.5 <= progress < 0.999
     * - "Master" if progress >= 0.999
     *
     * @return a string representing the tier
     */
    String getCurrentTier();
}
