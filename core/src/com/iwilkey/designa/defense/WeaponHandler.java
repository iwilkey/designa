package com.iwilkey.designa.defense;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.defense.WeaponType.SimpleBlaster;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class WeaponHandler {

    public static ArrayList<SimpleBlaster> blasters;

    public WeaponHandler() {
        blasters = new ArrayList<>();
    }

    public static void addBlaster(SimpleBlaster blaster) {
        blasters.add(blaster);
    }

    public static SimpleBlaster checkLocationForBlaster(int x, int y) {
        for(SimpleBlaster b : blasters) {
            if((int)b.x == x && (int)b.y == y) return b;
        }
        return null;
    }

    public static void removeBlasterAt(int x, int y) {
        blasters.removeIf(b -> (int) b.x == x && (int) b.y == y);
    }

    public void tick() {
        try {
            for (SimpleBlaster blaster : blasters) blaster.tick();
        } catch (ConcurrentModificationException ignored) {}
    }
    public void render(Batch b) {
        for(SimpleBlaster blaster : blasters) blaster.render(b);
    }

}
