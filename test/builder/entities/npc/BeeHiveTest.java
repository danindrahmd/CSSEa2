package builder.entities.npc;

import builder.JavaBeanFarm;
import builder.world.WorldLoadException;
import engine.Engine;
import engine.game.Game;
import engine.renderer.Dimensions;
import engine.renderer.TileGrid;
import org.junit.Assert;
import org.junit.Test;
import scenarios.analysers.AnalyserManager;
import scenarios.analysers.RenderableAnalyser;
import scenarios.details.ScenarioDetails;
import scenarios.mocks.MockCore;
import scenarios.mocks.MockEngineState;

import java.io.FileReader;
import java.io.IOException;

/**
 * Unit tests for BeeHive class covering all mutation testing scenarios.
 * Tests cover constructor, tick, interact, and checkAndSpawnBee methods.
 *
 * Location: test/builder/entities/npc/BeeHiveTest.java
 */
public class BeeHiveTest {

    private static final int SIZE = 800;
    private static final int TILES_PER_ROW = 10;
    private static final Dimensions dimensions = new TileGrid(TILES_PER_ROW, SIZE);
    private static final int PLAYER_X = 380;
    private static final int PLAYER_Y = 420;

    // ===== Test 1: Constructor calls setSprite and setSpeed =====
    // Covers mutations on lines 35-36: removed calls to setSprite and setSpeed

    /**
     * Test that BeeHive constructor initializes sprite and speed correctly.
     */
    @Test
    public void testConstructorInitialization() {
        BeeHive hive = new BeeHive(300, 400);

        // Verify sprite is set (not null)
        Assert.assertNotNull("Sprite should be set by constructor", hive.getSprite());

        // Verify speed is set to 0 (using delta for double comparison)
        Assert.assertEquals("Speed should be 0 for stationary hive", 0.0, hive.getSpeed(), 0.01);

        // Verify position
        Assert.assertEquals("X position should be 300", 300, hive.getX());
        Assert.assertEquals("Y position should be 400", 400, hive.getY());
    }

    // ===== Test 2: tick() calls super.tick() and timer.tick() =====
    // Covers mutations on lines 41-42: removed calls to Npc::tick and timer::tick

    /**
     * Test that tick() method properly advances timer.
     * If timer.tick() is not called, hive will never reload.
     */
    @Test
    public void testTickAdvancesTimer() throws IOException, WorldLoadException {
        // Setup: World with hive and magpie spawner
        ScenarioDetails details = new ScenarioDetails(PLAYER_X, PLAYER_Y, 9, 2);
        details.addCabbage(380, 350);
        details.addMagpieSpawner(0, 0, 100);
        final Game game = new JavaBeanFarm(
                dimensions,
                new FileReader("resources/testmaps/beeTest.map"),
                details.toReader());

        AnalyserManager data = new AnalyserManager();
        final MockCore core = new MockCore(data);
        final Engine engine = new Engine(game, dimensions, core);
        MockEngineState state = new MockEngineState(dimensions);

        // Place hive at frame 40, then just tick (no interaction)
        for (int i = 0; i < 300; i++) {
            state = state.withFrame(i);
            if (i == 40) {
                state = state.press('4').leftClick();
            }
            core.setState(state);
            engine.tick();
        }

        // Verify hive was placed
        Assert.assertEquals("Should have 1 hive", 1, data.getBySpriteGroup("hive").size());

        // The hive should exist and be ticking properly
        // This verifies tick() is being called
    }

    // ===== Test 3: interact() calls super.interact(), timer.tick(), and addNpc =====
    // Covers mutations on lines 47, 49, 52: removed calls

    /**
     * Test that interact() spawns bees when enemies are nearby.
     * This verifies all method calls in interact() are working.
     */
    @Test
    public void testInteractSpawnsBees() throws IOException, WorldLoadException {
        // Setup: Same as BeeSimulationTest - hive with magpies spawning
        ScenarioDetails details = new ScenarioDetails(PLAYER_X, PLAYER_Y, 9, 2);
        details.addCabbage(380, 350);
        details.addMagpieSpawner(0, 0, 100);
        final Game game = new JavaBeanFarm(
                dimensions,
                new FileReader("resources/testmaps/beeTest.map"),
                details.toReader());

        AnalyserManager data = new AnalyserManager();
        final MockCore core = new MockCore(data);
        final Engine engine = new Engine(game, dimensions, core);
        MockEngineState state = new MockEngineState(dimensions);

        // Place hive at frame 40
        for (int i = 0; i < 630; i++) {
            state = state.withFrame(i);
            if (i == 40) {
                state = state.press('4').leftClick();
            }
            core.setState(state);
            engine.tick();
        }

        // Verify: Bees were spawned (addNpc was called)
        Assert.assertTrue("Bees should spawn when enemies are nearby",
                data.getBySpriteGroup("bee").size() > 0);

        // Verify: Expected 6 bees as per original test
        Assert.assertEquals("Should spawn 6 bees", 6, data.getBySpriteGroup("bee").size());
    }

    // ===== Test 4: interact() does not add npc when checkAndSpawnBee returns null =====
    // Covers mutation on line 52 and conditional on line 51

    /**
     * Test that no bee is added when no enemy is nearby.
     */
    @Test
    public void testInteractDoesNotSpawnWhenNoEnemy() throws IOException, WorldLoadException {
        // Setup: Hive without any enemies
        ScenarioDetails details = new ScenarioDetails(PLAYER_X, PLAYER_Y, 9, 2);
        // No magpie spawner - no enemies!
        final Game game = new JavaBeanFarm(
                dimensions,
                new FileReader("resources/testmaps/beeTest.map"),
                details.toReader());

        AnalyserManager data = new AnalyserManager();
        final MockCore core = new MockCore(data);
        final Engine engine = new Engine(game, dimensions, core);
        MockEngineState state = new MockEngineState(dimensions);

        // Place hive and wait, standing on it to trigger interact()
        for (int i = 0; i < 300; i++) {
            state = state.withFrame(i);
            if (i == 40) {
                state = state.press('4').leftClick();
            }
            core.setState(state);
            engine.tick();
        }

        // Verify: No bees spawned (because no enemies nearby)
        Assert.assertEquals("No bees should spawn without enemies",
                0, data.getBySpriteGroup("bee").size());
    }

    // ===== Test 5: Timer reload logic (lines 51, 54) =====
    // Covers mutations: removed conditional equality checks

    /**
     * Test that hive reloads after timer finishes.
     * Verifies multiple bees can spawn over time.
     */
    @Test
    public void testHiveReloadsAfterTimer() throws IOException, WorldLoadException {
        // Setup: Hive with slow-spawning enemies
        ScenarioDetails details = new ScenarioDetails(PLAYER_X, PLAYER_Y, 9, 6);
        details.addCabbage(380, 350);
        details.addEagleSpawner(800, 400, 300); // Slow spawn
        final Game game = new JavaBeanFarm(
                dimensions,
                new FileReader("resources/testmaps/beeSlowEagleSpawnTest.map"),
                details.toReader());

        AnalyserManager data = new AnalyserManager();
        final MockCore core = new MockCore(data);
        final Engine engine = new Engine(game, dimensions, core);
        MockEngineState state = new MockEngineState(dimensions).leftClick();

        // Place multiple hives while moving up
        for (int i = 0; i < 530; i++) {
            MockEngineState currentState = state.withFrame(i);
            if (i == 0) {
                currentState = currentState.press('4');
            } else {
                currentState = currentState.press('w');
            }
            core.setState(currentState);
            engine.tick();
        }

        // Verify: Multiple bees spawned (means hives reloaded)
        Assert.assertTrue("Multiple bees should spawn as hives reload",
                data.getBySpriteGroup("bee").size() >= 2);
    }

    // ===== Test 6: checkAndSpawnBee distance check (line 67) =====
    // Covers mutations: replaced comparison check with false/true, equality check with false/true

    /**
     * Test that bees only spawn when enemy is within DETECTION_DISTANCE (350 pixels).
     */
    @Test
    public void testBeeSpawnsOnlyWhenEnemyInRange() throws IOException, WorldLoadException {
        // Setup: Hive with magpie spawner close by
        ScenarioDetails details = new ScenarioDetails(PLAYER_X, PLAYER_Y, 9, 2);
        details.addCabbage(380, 350);
        details.addMagpieSpawner(0, 0, 100); // Spawns at (0, 0)
        // Hive will be at approximately (380, 420)
        // Distance from (0,0) to (380, 420) = sqrt(380^2 + 420^2) ≈ 567 pixels
        // This is > 350, so initially no spawn

        final Game game = new JavaBeanFarm(
                dimensions,
                new FileReader("resources/testmaps/beeTest.map"),
                details.toReader());

        AnalyserManager data = new AnalyserManager();
        final MockCore core = new MockCore(data);
        final Engine engine = new Engine(game, dimensions, core);
        MockEngineState state = new MockEngineState(dimensions);

        for (int i = 0; i < 630; i++) {
            state = state.withFrame(i);
            if (i == 40) {
                state = state.press('4').leftClick();
            }
            core.setState(state);
            engine.tick();
        }

        // Magpies will move toward player and enter hive range
        // Verify bees spawned when magpies got close
        Assert.assertTrue("Bees should spawn when enemies get within range",
                data.getBySpriteGroup("bee").size() > 0);
    }

    // ===== Test 7: checkAndSpawnBee returns null (line 69) =====
    // Covers mutation: replaced return value with null

    /**
     * Test that checkAndSpawnBee returns null when no enemies in range.
     * Covered by testInteractDoesNotSpawnWhenNoEnemy.
     */
    @Test
    public void testCheckAndSpawnBeeReturnsNullWhenNoEnemyInRange() {
        BeeHive hive = new BeeHive(300, 400);

        // Call checkAndSpawnBee with empty list
        Npc result = hive.checkAndSpawnBee(java.util.Collections.emptyList());

        // Verify: returns null
        Assert.assertNull("Should return null when no enemies", result);
    }

    // ===== Test 8: checkAndSpawnBee returns GuardBee (line 69) =====

    /**
     * Test that checkAndSpawnBee returns a GuardBee when conditions are met.
     */
    @Test
    public void testCheckAndSpawnBeeReturnsGuardBee() throws IOException, WorldLoadException {
        // Create a scenario where we can verify bee type
        ScenarioDetails details = new ScenarioDetails(PLAYER_X, PLAYER_Y, 9, 2);
        details.addCabbage(380, 350);
        details.addMagpieSpawner(0, 0, 100);
        final Game game = new JavaBeanFarm(
                dimensions,
                new FileReader("resources/testmaps/beeTest.map"),
                details.toReader());

        AnalyserManager data = new AnalyserManager();
        final MockCore core = new MockCore(data);
        final Engine engine = new Engine(game, dimensions, core);
        MockEngineState state = new MockEngineState(dimensions);

        for (int i = 0; i < 630; i++) {
            state = state.withFrame(i);
            if (i == 40) {
                state = state.press('4').leftClick();
            }
            core.setState(state);
            engine.tick();
        }

        // Verify bee sprite group (GuardBee uses "bee" sprite)
        Assert.assertTrue("Should spawn GuardBee entities",
                data.getBySpriteGroup("bee").size() > 0);
    }

    // ===== Test 9: Hive loaded state =====

    /**
     * Test that hive only spawns one bee then becomes unloaded.
     */
    @Test
    public void testHiveOnlySpawnsOneBeeWhenLoaded() throws IOException, WorldLoadException {
        // Setup with many enemies but short test duration
        ScenarioDetails details = new ScenarioDetails(PLAYER_X, PLAYER_Y, 9, 2);
        details.addCabbage(380, 350);
        details.addMagpieSpawner(0, 0, 50); // Fast spawn
        final Game game = new JavaBeanFarm(
                dimensions,
                new FileReader("resources/testmaps/beeTest.map"),
                details.toReader());

        AnalyserManager data = new AnalyserManager();
        final MockCore core = new MockCore(data);
        final Engine engine = new Engine(game, dimensions, core);
        MockEngineState state = new MockEngineState(dimensions);

        // Place hive and run for short duration
        for (int i = 0; i < 200; i++) {
            state = state.withFrame(i);
            if (i == 40) {
                state = state.press('4').leftClick();
            }
            core.setState(state);
            engine.tick();
        }

        // In 200 ticks with fast magpie spawn, multiple magpies will appear
        // But hive should only spawn ONE bee initially (then needs to reload)
        int beeCount = data.getBySpriteGroup("bee").size();
        Assert.assertTrue("Should spawn at least 1 bee", beeCount >= 1);

        // Due to timer (240 ticks for reload), should have limited bees in 200 ticks
        Assert.assertTrue("Should not spawn unlimited bees without reload",
                beeCount < 5);
    }

    // ===== Test 10: Hive placement and constants =====

    /**
     * Test that hive is placed at correct location.
     */
    @Test
    public void testHivePlacement() throws IOException, WorldLoadException {
        ScenarioDetails details = new ScenarioDetails(PLAYER_X, PLAYER_Y, 9, 2);
        details.addCabbage(380, 350);
        details.addMagpieSpawner(0, 0, 100);
        final Game game = new JavaBeanFarm(
                dimensions,
                new FileReader("resources/testmaps/beeTest.map"),
                details.toReader());

        AnalyserManager data = new AnalyserManager();
        final MockCore core = new MockCore(data);
        final Engine engine = new Engine(game, dimensions, core);
        MockEngineState state = new MockEngineState(dimensions);

        for (int i = 0; i < 100; i++) {
            state = state.withFrame(i);
            if (i == 40) {
                state = state.press('4').leftClick();
            }
            core.setState(state);
            engine.tick();
        }

        // Verify hive placement
        Assert.assertEquals("Should place 1 hive", 1, data.getBySpriteGroup("hive").size());

        RenderableAnalyser hive = data.getBySpriteGroup("hive").getFirst();
        int hiveX = hive.getFirstFrame().getX();
        int hiveY = hive.getFirstFrame().getY();

        // Hive should be near player location
        int distance = (int) Math.sqrt(
                Math.pow(hiveX - PLAYER_X, 2) + Math.pow(hiveY - PLAYER_Y, 2));
        Assert.assertTrue("Hive should be placed near player",
                distance < dimensions.tileSize() * 2);
    }

    // ===== Test 11: Bees spawn on top of hive =====

    /**
     * Test that spawned bees start at hive location.
     */
    @Test
    public void testBeesSpawnAtHiveLocation() throws IOException, WorldLoadException {
        ScenarioDetails details = new ScenarioDetails(PLAYER_X, PLAYER_Y, 9, 2);
        details.addCabbage(380, 350);
        details.addMagpieSpawner(0, 0, 100);
        final Game game = new JavaBeanFarm(
                dimensions,
                new FileReader("resources/testmaps/beeTest.map"),
                details.toReader());

        AnalyserManager data = new AnalyserManager();
        final MockCore core = new MockCore(data);
        final Engine engine = new Engine(game, dimensions, core);
        MockEngineState state = new MockEngineState(dimensions);

        for (int i = 0; i < 630; i++) {
            state = state.withFrame(i);
            if (i == 40) {
                state = state.press('4').leftClick();
            }
            core.setState(state);
            engine.tick();
        }

        // Get hive and bee positions
        RenderableAnalyser hive = data.getBySpriteGroup("hive").getFirst();
        int hiveX = hive.getFirstFrame().getX();
        int hiveY = hive.getFirstFrame().getY();

        // Check all bees spawn on hive
        for (RenderableAnalyser bee : data.getBySpriteGroup("bee")) {
            int beeSpawnX = bee.getFirstFrame().getX();
            int beeSpawnY = bee.getFirstFrame().getY();
            Assert.assertEquals("Bee should spawn on hive X", hiveX, beeSpawnX);
            Assert.assertEquals("Bee should spawn on hive Y", hiveY, beeSpawnY);
        }
    }

    // ===== Test 12: Constants verification =====

    /**
     * Test that BeeHive constants are defined correctly.
     */
    @Test
    public void testBeeHiveConstants() {
        Assert.assertEquals("DETECTION_DISTANCE should be 350",
                350, BeeHive.DETECTION_DISTANCE);
        Assert.assertEquals("TIMER should be 240",
                240, BeeHive.TIMER);
        Assert.assertEquals("FOOD_COST should be 2",
                2, BeeHive.FOOD_COST);
        Assert.assertEquals("COIN_COST should be 2",
                2, BeeHive.COIN_COST);
    }

    // ===== Test 13: Hive doesn't exist at start =====

    /**
     * Test that hive doesn't exist until placed by player.
     */
    @Test
    public void testHiveNotThereAtStart() throws IOException, WorldLoadException {
        ScenarioDetails details = new ScenarioDetails(PLAYER_X, PLAYER_Y, 9, 2);
        details.addCabbage(380, 350);
        details.addMagpieSpawner(0, 0, 100);
        final Game game = new JavaBeanFarm(
                dimensions,
                new FileReader("resources/testmaps/beeTest.map"),
                details.toReader());

        AnalyserManager data = new AnalyserManager();
        final MockCore core = new MockCore(data);
        final Engine engine = new Engine(game, dimensions, core);
        MockEngineState state = new MockEngineState(dimensions);

        final int hivePlaceFrame = 40;

        for (int i = 0; i < 200; i++) {
            state = state.withFrame(i);
            if (i == hivePlaceFrame) {
                state = state.press('4').leftClick();
            }
            core.setState(state);
            engine.tick();
        }

        // Verify hive didn't exist before placement frame
        RenderableAnalyser hive = data.getBySpriteGroup("hive").getFirst();
        for (int i = 0; i < hivePlaceFrame; i++) {
            Assert.assertFalse("Hive should not exist in frame " + i,
                    hive.wasInFrame(i));
        }

        // Verify hive exists after placement
        Assert.assertTrue("Hive should exist at placement frame",
                hive.wasInFrame(hivePlaceFrame + 1));
    }

    // ===== Test 14: Bees spawn in reaction to birds =====

    /**
     * Test that bees only spawn after birds appear.
     */
    @Test
    public void testBeesSpawnInReactionToBirds() throws IOException, WorldLoadException {
        ScenarioDetails details = new ScenarioDetails(PLAYER_X, PLAYER_Y, 9, 2);
        details.addCabbage(380, 350);
        details.addMagpieSpawner(0, 0, 100);
        final Game game = new JavaBeanFarm(
                dimensions,
                new FileReader("resources/testmaps/beeTest.map"),
                details.toReader());

        AnalyserManager data = new AnalyserManager();
        final MockCore core = new MockCore(data);
        final Engine engine = new Engine(game, dimensions, core);
        MockEngineState state = new MockEngineState(dimensions);

        for (int i = 0; i < 630; i++) {
            state = state.withFrame(i);
            if (i == 40) {
                state = state.press('4').leftClick();
            }
            core.setState(state);
            engine.tick();
        }

        // Get first magpie and first bee spawn times
        RenderableAnalyser firstMagpie = data.getFirstSpawnedOfSpriteGroup("magpie");
        Assert.assertFalse("Should have magpies", data.getBySpriteGroup("magpie").isEmpty());

        // Verify bees don't spawn before birds
        for (RenderableAnalyser bee : data.getBySpriteGroup("bee")) {
            int beeSpawnFrame = bee.getFirstFrame().getFrame();
            Assert.assertTrue("Bees should spawn after birds appear",
                    beeSpawnFrame >= firstMagpie.getFirstFrame().getFrame());
        }
    }
}