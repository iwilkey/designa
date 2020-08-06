package com.iwilkey.designa.entities.statics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.entities.Entity;

public abstract class StaticEntity extends Entity {

    // 2-D model array that will be filled with tile codes.
    protected int[][] model;

    public StaticEntity(GameBuffer gb, float x, float y, int w, int h) {
        super(gb, x, y, w, h);
    }

}
