package game.achievements;

/**
 * A concrete implementation of the Achievement interface.
 * <p>Including:
 * - Registering new achievements
 * - Updating achievement progress
 * - Logging when achievements are mastered
 * - Providing access to achievement data
 */
public class GameAchievement implements Achievement {

    private final String name;
    private final String desc;
    private double progress;

    /**
     * Constructs an Achievement with the specified name and description.
     * The initial progress is 0.
     *
     * @param name the unique name (non-null, non-empty)
     * @param desc the achievement description (non-null, non-empty)
     * @throws IllegalArgumentException if name or description is null or empty
     */
    public GameAchievement(String name, String desc) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name must not be null or empty");
        }
        if (desc == null || desc.isEmpty()) {
            throw new IllegalArgumentException("description must not be null or empty");
        }
        this.name = name;
        this.desc = desc;
        this.progress = 0.0;
    }

    /**
     * Returns the unique name of the achievement.
     *
     * @return the achievement name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the description of the achievement.
     *
     * @return the achievement description
     */
    @Override
    public String getDescription() {
        return desc;
    }

    /**
     * Returns the current progress (between 0.0 and 1.0).
     *
     * @return the achievement progress
     */
    @Override
    public double getProgress() {
        return progress;
    }

    /**
     * Sets the progress to the specified value.
     * Caps at 1.0 and minimum 0.0.
     *
     * @param newProgress the new progress value
     */
    @Override
    public void setProgress(double newProgress) {
        if (newProgress < 0.0) {
            this.progress = 0.0;
        } else if (newProgress > 1.0) {
            this.progress = 1.0;
        } else {
            this.progress = newProgress;
        }
    }

    /**
     * Returns the achievement tier based on progress.
     * <p>
     * Progress is tracked as a value between 0.0 and 1.0.
     * Tiers are determined as:
     * - "Novice" if progress < 0.5
     * - "Expert" if 0.5 <= progress < 0.999
     * - "Master" if progress >= 0.999 s
     */
    @Override
    public String getCurrentTier() {
        if (progress >= 0.999) {
            return "Master";
        } else if (progress >= 0.5) {
            return "Expert";
        } else {
            return "Novice";
        }
    }
}
