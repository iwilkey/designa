package com.iwilkey.designa.defense;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.defense.WeaponType.SimpleBlaster;

import java.util.ArrayList;

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

    public void tick() {
        for(SimpleBlaster blaster : blasters) blaster.tick();
    }
    public void render(Batch b) {
        for(SimpleBlaster blaster : blasters) blaster.render(b);
    }

}
