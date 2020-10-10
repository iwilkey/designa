package com.iwilkey.designa.entities;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.entities.creature.violent.Enemy;
import com.iwilkey.designa.entities.creature.passive.Npc;
import com.iwilkey.designa.entities.creature.passive.Player;
import com.iwilkey.designa.entities.statics.StaticEntity;
import com.iwilkey.designa.entities.statics.Tree;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**

 This class defines the EntityHandler API, which is given to every World instance to make it easy to handle
 many entities at once.

 @author Ian Wilkey (iwilkey)
 @version VERSION
 @since 7/21/2020

 */

public class EntityHandler {

    /**
     * Global vars
     */

    // Final vars
    private final Player player;
    private final ArrayList<Entity> entities;

    /*

        * The idea here was to sort through the listen efficiently for render order, but I ended up splitting rendering
        * up into sub functions handled in this class. I may use this later, though. It's more efficient.

    private Comparator<Entity> renderSorter = new Comparator<Entity>() {
        @Override
        public int compare(Entity o1, Entity o2) {
            if(o1 instanceof StaticEntity && !(o2 instanceof StaticEntity)) return -1;
            else return 1;
        }
    };

     */

    /**
     * EntityHandler constructor.
     * @param player The EntityHandler needs the player instance.
     */
    public EntityHandler(Player player) {
        // Assign player instance.
        this.player = player;

        // Initialize the entity array.
        entities = new ArrayList<Entity>();

        // Add the player.
        entities.add(player);
    }

    /**
     * The entity tick method. Called every frame.
     */
    public void tick() {
        // Note: Here I use an Iterator to avoid it skipping over entities when some are removed after death.

        // Init the iterator and assign it to the now iterative entity array.
        Iterator<Entity> it = entities.iterator();
        // Loop through it...
        try {
            while (it.hasNext()) {
                Entity e = it.next(); // Capture an Entity object
                e.tick(); // Tick it...
                if (!(e instanceof Tree)) if (!e.isActive()) it.remove(); // Unless it's a tree, remove it.
                else if (e.y < 0) it.remove(); // But, once the tree is under y = 0, remove it too.
            }
        } catch (ConcurrentModificationException ignored) {}
    }

    /**
     * This method is a sub-render method invoked in the world render method. It renders the player entity.
     * @param b Every render, or sub-render method needs the graphics batch.
     */
    public void playerRender(Batch b) {
        for(Entity e : entities) if((e instanceof Player)) e.render(b);
    }

    /**
     * This method is a sub-render method invoked in the world render method. It renders all npc entities.
     * @param b Every render, or sub-render method needs the graphics batch.
     */
    public void npcRender(Batch b) {
        for(Entity e : entities) if((e instanceof Npc) || (e instanceof Enemy)) e.render(b);
    }

    /**
     * This method is a sub-render method invoked in the world render method. It renders all static entities.
     * @param b Every render, or sub-render method needs the graphics batch.
     */
    public void staticRender(Batch b) {
        for(Entity e : entities) {
            if((e instanceof StaticEntity)) e.render(b);
        }
    }

    /**
     * This method will add a new entity into the list when invoked.
     * @param e The new entity to add.
     */
    public void addEntity(Entity e) {
        entities.add(e);
    }

    /*
     * GETTERS AND SETTERS
     */

    /**
     * The player getter.
     * @return The player instance.
     */
    public Player getPlayer() { return player; }

    /**
     * The entity list getter.
     * @return The list of entities.
     */
    public ArrayList<Entity> getEntities() { return entities; }

}
