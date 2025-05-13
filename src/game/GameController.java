package game;

import game.achievements.Achievement;
import game.achievements.AchievementManager;
import game.achievements.PlayerStatsTracker;
import game.core.SpaceObject;
import game.ui.UI;
import game.utility.Direction;

import java.util.ArrayList;
import java.util.List;

/**
 * The Controller handling the game flow and interactions.
 * <p>
 * Holds references to the UI and the Model, so it can pass information and references back and forth as necessary.<br>
 * Manages changes to the game, which are stored in the Model, and displayed by the UI.<br>
 */
public class GameController {
    private final long endTime;
    private final UI ui;
    private final GameModel model;
    private final AchievementManager aManager;
    private boolean isPaused = false;

    /**
     * An internal variable indicating whether certain methods should log their actions.
     * Not all methods respect isVerbose.
     */
    private boolean isVerbose = false;


    /**
     * Initializes the game controller with the given UI, GameModel and AchievementManager.<br>
     * Stores the UI, GameModel, AchievementManager and start time.<br>
     * The start time System.currentTimeMillis() should be stored as a long.<br>
     * Starts the UI using UI.start().<br>
     *
     * @param ui the UI used to draw the Game
     * @param model the model used to maintain game information
     * @param aManager the manager used to maintain achievement information
     *
     * @requires ui is not null
     * @requires model is not null
     * @requires achievementManager is not null
     * @provided
     */
    public GameController(UI ui, GameModel model, AchievementManager aManager) {
        this.ui = ui;
        ui.start();
        this.model = model;
        this.endTime = System.currentTimeMillis(); // Current time
        this.aManager = aManager;
    }


    /**
     * Initializes the game controller with the given UI and GameModel.<br>
     * Stores the ui, model and start time.<br>
     * The start time System.currentTimeMillis() should be stored as a long.<br>
     *
     * @param ui    the UI used to draw the Game
     * @param aManager the manager used to maintain achievement information
     *
     * @requires ui is not null
     * @requires achievementManager is not null
     * @provided
     */
    public GameController(UI ui, AchievementManager aManager) {
        this(ui, new GameModel(ui::log, new PlayerStatsTracker()), aManager);
    }

    /**
     * Starts the main game loop.<br>
     * <p>
     * Passes onTick and handlePlayerInput to ui.onStep and ui.onKey respectively.
     * @provided
     */
    public void startGame() {
        ui.onStep(this::onTick);
        ui.onKey(this::handlePlayerInput);
    }

    /**
     * Uses the provided tick to call and advance the following:<br>
     * - A call to model.updateGame(tick) to advance the game by the given tick.<br>
     * - A call to model.checkCollisions() to handle game interactions.<br>
     * - A call to model.spawnObjects() to handle object creation.<br>
     * - A call to model.levelUp() to check and handle leveling.<br>
     * - A call to refreshAchievements(tick) to handle achievement updating.<br>
     * - A call to renderGame() to draw the current state of the game.<br>
     * @param tick the provided tick
     * @provided
     */
    public void onTick(int tick) {
        model.updateGame(tick); // Update GameObjects
        model.checkCollisions(); // Check for Collisions
        model.spawnObjects(); // Handles new spawns
        model.levelUp(); // Level up when score threshold is met
        refreshAchievements(tick); // Handle achievement updating.
        renderGame(); // Update Visual

        // Check game over
        if (model.checkGameOver()) {
            pauseGame();
            showGameOverWindow();
        }
    }

    /**
     * Displays a Game Over window containing the player's final statistics and achievement
     * progress.<br>
     * <p>
     * This window includes:<br>
     * - Number of shots fired and shots hit<br>
     * - Number of Enemies destroyed<br>
     * - Survival time in seconds<br>
     * - Progress for each achievement, including name, description, completion percentage
     * and current tier<br>
     * @provided
     */
    private void showGameOverWindow() {

        // Create a new window to display game over stats.
        javax.swing.JFrame gameOverFrame = new javax.swing.JFrame("Game Over - Player Stats");
        gameOverFrame.setSize(400, 300);
        gameOverFrame.setLocationRelativeTo(null); // center on screen
        gameOverFrame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);


        StringBuilder sb = new StringBuilder();
        sb.append("Shots Fired: ").append(getStatsTracker().getShotsFired()).append("\n");
        sb.append("Shots Hit: ").append(getStatsTracker().getShotsHit()).append("\n");
        sb.append("Enemies Destroyed: ").append(getStatsTracker().getShotsHit()).append("\n");
        sb.append("Survival Time: ").append(getStatsTracker().getElapsedSeconds()).append(" seconds\n");


        List<Achievement> achievements= aManager.getAchievements();
        for (Achievement ach : achievements) {
            double progressPercent = ach.getProgress() * 100;
            sb.append(ach.getName())
                    .append(" - ")
                    .append(ach.getDescription())
                    .append(" (")
                    .append(String.format("%.0f%%", progressPercent))
                    .append(" complete, Tier: ")
                    .append(ach.getCurrentTier())
                    .append(")\n");
        }

        String statsText = sb.toString();

        // Create a text area to show stats.
        javax.swing.JTextArea statsArea = new javax.swing.JTextArea(statsText);
        statsArea.setEditable(false);
        statsArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 14));

        // Add the text area to a scroll pane (optional) and add it to the frame.
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(statsArea);
        gameOverFrame.add(scrollPane);

        // Make the window visible.
        gameOverFrame.setVisible(true);
    }

    /**
     * Returns the current GameModel.
     *
     * @return the current GameModel.
     */
    public GameModel getModel() {
        return model;
    }

    /**
     * Returns the current PlayerStatsTracker.
     *
     * @return the current PlayerStatsTracker.
     */
    public PlayerStatsTracker getStatsTracker() {
        return model.getStatsTracker();
    }

    /**
     * Sets verbose state to the provided input.
     * Also sets the model's verbose state to match.
     *
     * @param verbose whether verbose mode is enabled.
     */
    public void setVerbose(boolean verbose) {
        this.isVerbose = verbose;
        model.setVerbose(verbose);
    }

    /**
     * Updates the player's progress towards achievements each tick.
     *
     * @param tick the current tick count.
     */
    public void refreshAchievements(int tick) {
        long survivalSeconds = (System.currentTimeMillis() - endTime) / 1000;
        double survivorProgress = Math.min(1.0, survivalSeconds / 120.0);

        int hits = getStatsTracker().getShotsHit();
        double exterminatorProgress = Math.min(1.0, hits / 20.0);

        int fired = getStatsTracker().getShotsFired();
        double accuracy = fired > 0 ? (double) hits / fired : 0.0;
        double sharpShooterProgress = (fired > 10) ? Math.min(1.0, accuracy / 0.99) : 0.0;

        aManager.updateAchievement("Survivor", survivorProgress);
        aManager.updateAchievement("Enemy Exterminator", exterminatorProgress);
        aManager.updateAchievement("Sharp Shooter", sharpShooterProgress);

        ui.setAchievementProgressStat("Survivor", survivorProgress);
        ui.setAchievementProgressStat("Enemy Exterminator", exterminatorProgress);
        ui.setAchievementProgressStat("Sharp Shooter", sharpShooterProgress);

        if (isVerbose && tick % 100 == 0) {
            ui.log("Survivor progress: " + (int) (survivorProgress * 100) + "%");
            ui.log("Enemy Exterminator progress: " + (int) (exterminatorProgress * 100) + "%");
            ui.log("Sharp Shooter progress: " + (int) (sharpShooterProgress * 100) + "%");
        }

        aManager.logAchievementMastered();
    }

    /**
     * Handles player input and performs actions such as moving the ship or firing bullets.
     *
     * @param input the player's input command.
     * @requires input is a single character
     */
    public void handlePlayerInput(String input) {
        if (input == null || input.length() != 1) return;

        char key = Character.toUpperCase(input.charAt(0));

        // Always allow pause toggle
        if (key == 'P') {
            pauseGame();
            return;
        }

        // No input allowed while paused
        if (isPaused) return;

        boolean moved = false;

        switch (key) {
            case 'W' -> {
                model.getShip().move(Direction.UP);
                moved = true;
            }
            case 'A' -> {
                model.getShip().move(Direction.LEFT);
                moved = true;
            }
            case 'S' -> {
                model.getShip().move(Direction.DOWN);
                moved = true;
            }
            case 'D' -> {
                model.getShip().move(Direction.RIGHT);
                moved = true;
            }
            case 'F' -> {
                model.fireBullet();
                getStatsTracker().recordShotFired();
            }
            default -> ui.log("Invalid input. Use W, A, S, D, F, or P.");
        }

        // Log movement if verbose
        if (moved && isVerbose) {
            int x = model.getShip().getX();
            int y = model.getShip().getY();
            ui.log("Ship moved to (" + x + ", " + y + ")");
        }
    }

    /**
     * Calls ui.pause() to pause the game until the method is called again.
     * Logs the pause status.
     */
    public void pauseGame() {
        ui.pause();
        isPaused = !isPaused;
        ui.log(isPaused ? "Game paused." : "Game unpaused.");
    }

    /**
     * Renders the current game state, including score, health, level, and survival time.
     */
    public void renderGame() {
        int score = model.getShip().getScore();
        int health = model.getShip().getHealth();
        int level = model.getLevel();
        long seconds = (System.currentTimeMillis() - endTime) / 1000;

        ui.setStat("Score", String.valueOf(score));
        ui.setStat("Health", String.valueOf(health));
        ui.setStat("Level", String.valueOf(level));
        ui.setStat("Time Survived", seconds + " seconds");

        // Combine space objects and the ship into one list
        List<SpaceObject> renderables = new ArrayList<>(model.getSpaceObjects());
        renderables.add(model.getShip());

        // Pass the combined list to the UI
        ui.render(renderables);
    }

}

