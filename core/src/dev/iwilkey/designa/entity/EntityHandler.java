package dev.iwilkey.designa.entity;

import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;
import java.util.Iterator;

public class EntityHandler {

    public ArrayList<Entity> entities;

    public EntityHandler() {
        entities = new ArrayList<>();
    }

    public void addEntity(Entity e) {
        entities.add(e);
    }

    public void tick() {
        Iterator<Entity> it = entities.iterator();
        while (it.hasNext()) {
            Entity e = it.next();
            e.tick();
            if(!e.active) it.remove();
        }
    }

    public void render(Batch b) {
        for(Entity e : entities) e.render(b);
    }

}
