package com.iwilkey.designa.entities.statics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.entities.Entity;
import com.iwilkey.designa.tiles.Tile;

public abstract class StaticEntity extends Entity {

    protected int[][] model;
    public int width, height;

    public StaticEntity(GameBuffer gb, float x, float y, int w, int h) {
        super(gb, x, y, w, h);
    }

}
