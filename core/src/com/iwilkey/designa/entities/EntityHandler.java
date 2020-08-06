package com.iwilkey.designa.entities;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.entities.creature.Player;

import java.util.ArrayList;
import java.util.Iterator;

public class EntityHandler {

    private final Player player;
    private final ArrayList<Entity> entities;

    // TODO: I'm thinking here will be a couple 1-D arrays that will hold x values for trees, rocks, and other static
    // TODO: that need to be placed randomly by the World Generator.
    // Ex: public static int[] trees = new int[25]; <-- Or whatever number the world gen sets.
    // This could also be done in the world gen class then just added as entities into the list when the World
    // Constructor runs.

    public EntityHandler(Player player) {
        this.player = player;
        entities = new ArrayList<Entity>();
        entities.add(player);
    }

    public void tick() {
        Iterator<Entity> it = entities.iterator();

        while(it.hasNext()) {
            Entity e = it.next();
            e.tick();
            if(!e.isActive()) it.remove();
        }
    }

    public void render(Batch b) {
        for(Entity e : entities) {
            e.render(b);
        }
    }

    public void addEntity(Entity e) {
        entities.add(e);
    }
    public Player getPlayer() { return player; }
    public ArrayList<Entity> getEntities() { return entities; }

}
