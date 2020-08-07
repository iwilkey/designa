package com.iwilkey.designa.items;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.math.MathUtils;
import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.tiles.Tile;

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
            moveX(i);
            moveY(i);
            if(i.isPickedUp()) it.remove();
        }
    }

    private void moveX(Item i) {
        if(!i.isGrounded) {
            float v = MathUtils.random(1.70f, 2.30f);
            if (gb.getWorld().getEntityHandler().getPlayer().facingLeft())
                i.setPosition((int) (i.x + v), i.y);
            else i.setPosition((int) (i.x - v), i.y);
        }
    }

    private void moveY(Item i) {
        int ty = (int) (i.y - i.gravity - i.bounds.height) / Tile.TILE_SIZE;

        if(!collisionWithTile((i.x) / Tile.TILE_SIZE, ty) &&
                !collisionWithTile((i.x + i.bounds.width) / Tile.TILE_SIZE, ty)){
            i.isGrounded = false;
            i.timeInAir += 0.09f;
            i.setPosition(i.x, (int) (i.y + i.gravity * i.timeInAir));
        } else {
            i.isGrounded = true;
            i.timeInAir = 0;
            i.setPosition(i.x, ty * Tile.TILE_SIZE + Tile.TILE_SIZE);
        }
    }

    private boolean collisionWithTile(int x, int y) { return gb.getWorld().getTile(x, y).isSolid(); }

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
