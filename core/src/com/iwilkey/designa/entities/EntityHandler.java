package com.iwilkey.designa.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.entities.creature.Player;

import java.util.ArrayList;
import java.util.Iterator;

public class EntityHandler {

    private GameBuffer gb;
    private Player player;
    private ArrayList<Entity> entities;

    public EntityHandler(GameBuffer gb, Player player) {
        this.gb = gb;
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
