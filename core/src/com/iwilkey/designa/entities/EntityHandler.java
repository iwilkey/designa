package com.iwilkey.designa.entities;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.entities.creature.Player;
import com.iwilkey.designa.entities.statics.StaticEntity;
import com.iwilkey.designa.entities.statics.Tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class EntityHandler {

    private final Player player;
    private final ArrayList<Entity> entities;

    /*
    private Comparator<Entity> renderSorter = new Comparator<Entity>() {
        @Override
        public int compare(Entity o1, Entity o2) {
            if(o1 instanceof StaticEntity && !(o2 instanceof StaticEntity)) return -1;
            else return 1;
        }
    };

     */

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
            if(!(e instanceof Tree)) if(!e.isActive()) it.remove();
            else if (e.y < 0) it.remove();
        }
    }

    public void creatureRender(Batch b) {
        for(Entity e : entities) {
            if(!(e instanceof StaticEntity)) e.render(b);
        }
    }

    public void staticRender(Batch b) {
        for(Entity e : entities) {
            if((e instanceof StaticEntity)) e.render(b);
        }
    }

    public void addEntity(Entity e) {
        entities.add(e);
    }
    public Player getPlayer() { return player; }
    public ArrayList<Entity> getEntities() { return entities; }

}
