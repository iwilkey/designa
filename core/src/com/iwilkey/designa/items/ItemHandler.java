package com.iwilkey.designa.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.GameBuffer;

import java.util.ArrayList;
import java.util.Iterator;

public class ItemHandler {

    private GameBuffer gb;
    private ArrayList<Item> items;

    public ItemHandler(GameBuffer gb) {
        this.gb = gb;
        items = new ArrayList<Item>();
    }

    public void tick() {
        Iterator<Item> it = items.iterator();

        while(it.hasNext()) {
            Item i = it.next();
            i.tick();
            if(i.isPickedUp()) it.remove();
        }
    }

    public void render(Batch b) {
        for (Item i : items) {
            i.render(b);
        }
    }

    public void addItem(Item i) {
        i.setGameBuffer(gb);
        items.add(i);
    }

    // Getters and setters

    public void setGameBuffer(GameBuffer gb) { this.gb = gb; }

}
