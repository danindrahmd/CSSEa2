package builder.entities.npc;

import builder.GameState;
import builder.Tickable;
import builder.entities.Interactable;
import builder.ui.RenderableGroup;

import engine.EngineState;
import engine.renderer.Renderable;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all NPCs in the game.
 * Responsible for tracking, updating, and cleaning up NPCs.
 */
public class NpcManager implements Interactable, Tickable, RenderableGroup {

    // Keep public for backward compatibility with Dirt.java and Grass.java
    public final ArrayList<Npc> npcs = new ArrayList<>();

    /**
     * Creates a new NPC manager.
     */
    public NpcManager() {}

    /**
     * Removes all NPCs marked for removal from the active NPC list.
     */
    public void cleanup() {
        npcs.removeIf(Npc::isMarkedForRemoval);
    }

    /**
     * Adds an NPC to the manager.
     *
     * @param npc the NPC to add
     */
    public void addNpc(Npc npc) {
        this.npcs.add(npc);
    }

    /**
     * Gets all active NPCs.
     * Returns a defensive copy to prevent external modification.
     *
     * @return a list of all active NPCs
     */
    public List<Npc> getAllNpcs() {
        return new ArrayList<>(this.npcs);
    }

    @Override
    public void tick(EngineState state, GameState game) {
        this.cleanup();
        for (Npc npc : npcs) {
            npc.tick(state, game);
        }
    }

    @Override
    public void interact(EngineState state, GameState game) {
        for (Interactable interactable : this.getInteractables()) {
            interactable.interact(state, game);
        }
    }

    /**
     * Gets all NPCs that implement Interactable.
     *
     * @return a list of interactable NPCs
     */
    private List<Interactable> getInteractables() {
        final ArrayList<Interactable> interactables = new ArrayList<>();
        for (Npc npc : npcs) {
            if (npc instanceof Interactable) {
                interactables.add(npc);
            }
        }
        return interactables;
    }

    @Override
    public List<Renderable> render() {
        return new ArrayList<>(this.npcs);
    }
}