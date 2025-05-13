package game.achievements;

import java.util.*;

/**
 * GameAchievementManager coordinates achievement updates and file persistence management.
 * <p>
 * Responsibilities:
 * - Register new achievements.
 * - Update achievement progress.
 * - Check for Mastered achievements and log them using AchievementFile.
 * - Provide access to the current list of achievements.
 */
public class AchievementManager {

    private final Map<String, Achievement> achievementMap;
    private final AchievementFile fileHandler;
    private final Set<String> loggedAchievements = new HashSet<>();

    /**
     * Constructs a GameAchievementManager with the specified AchievementFile.
     *
     * @param fileHandler the AchievementFile instance to use (non-null)
     * @throws IllegalArgumentException if fileHandler is null
     * @requires fileHandler is not null
     */
    public AchievementManager(AchievementFile fileHandler) {
        if (fileHandler == null) {
            throw new IllegalArgumentException("AchievementFile must not be null.");
        }
        this.fileHandler = fileHandler;
        this.achievementMap = new HashMap<>();
    }

    /**
     * Registers a new achievement.
     *
     * @param achievement the Achievement to register.
     * @throws IllegalArgumentException if achievement is null or already registered
     * @requires achievement is not null
     */
    public void addAchievement(Achievement achievement) {
        if (achievement == null) {
            throw new IllegalArgumentException("Achievement must not be null.");
        }
        if (achievementMap.containsKey(achievement.getName())) {
            throw new IllegalArgumentException("Achievement already registered: " + achievement.getName());
        }
        achievementMap.put(achievement.getName(), achievement);
    }

    /**
     * Sets the progress of the specified achievement to a given amount.
     *
     * @param achievementName      the name of the achievement
     * @param absoluteProgressValue the value the achievement's progress will be set to
     * @throws IllegalArgumentException if name is invalid or not found
     * @requires achievementName is a non-null, non-empty string identifying a registered achievement
     */
    public void updateAchievement(String achievementName, double absoluteProgressValue) {
        if (achievementName == null || achievementName.isEmpty()) {
            throw new IllegalArgumentException("Invalid achievement name.");
        }
        Achievement achievement = achievementMap.get(achievementName);
        if (achievement == null) {
            throw new IllegalArgumentException("Achievement not registered: " + achievementName);
        }
        achievement.setProgress(absoluteProgressValue);
    }

    /**
     * Returns a list of all registered achievements.
     *
     * @return a List of Achievement objects.
     */
    public List<Achievement> getAchievements() {
        return new ArrayList<>(achievementMap.values());
    }

    /**
     * Checks all registered achievements. For any achievement that is mastered and has not yet
     * been logged, this method logs the event via AchievementFile, and marks the achievement
     * as logged.
     */
    public void logAchievementMastered() {
        for (Achievement a : achievementMap.values()) {
            if ("Master".equals(a.getCurrentTier()) && !loggedAchievements.contains(a.getName())) {
                fileHandler.save("Achievement Mastered: " + a.getName());
                loggedAchievements.add(a.getName());
            }
        }
    }
}
