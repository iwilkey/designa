package com.iwilkey.designa.entities.creature;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.GameBuffer;

public abstract class Enemy extends Creature {

    public Enemy(GameBuffer gb, float x, float y, int w, int h) {
        super(gb, x, y, w, h);
    }

}
