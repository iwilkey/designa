package dev.iwilkey.designa.gfx;

import com.badlogic.gdx.math.Vector2;

public class Light {

    final int strength;
    public int x, y;

    public Light(int x, int y, int strength) {
        this.x = x; this.y = y;
        this.strength = strength;
    }

    public byte[][] buildLightMap(byte[][] oldLightMap, int width, int height) {

        Vector2 vector;
        float magnitude;
        byte original;

        for(int yl = y - strength; yl < y + strength + 1; yl++) {
            for(int xl = x - strength; xl < x + strength + 1; xl++) {

                try {

                    vector = new Vector2(x - xl, y - yl);
                    magnitude = (float)Math.sqrt(Math.pow(vector.x, 2) + Math.pow(vector.y, 2));
                    original = oldLightMap[xl][yl];
                    if(!(strength - magnitude < original))
                        oldLightMap[xl][yl] = (byte)(strength - Math.round(magnitude));

                    if(yl == y + strength && xl == x + strength) oldLightMap[xl][yl] = original;
                    if(yl == y - strength && xl == x + strength) oldLightMap[xl][yl] = original;
                    if(yl == y + strength && xl == x - strength) oldLightMap[xl][yl] = original;
                    if(yl == y - strength && xl == x - strength) oldLightMap[xl][yl] = original;

                } catch (ArrayIndexOutOfBoundsException ignored) {}
            }
        }

        return oldLightMap;

    }
}
