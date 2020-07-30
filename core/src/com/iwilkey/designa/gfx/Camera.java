package com.iwilkey.designa.gfx;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.entities.Entity;
import com.iwilkey.designa.tiles.Tile;

public class Camera {

    private GameBuffer gb;

    private Vector3 position;
    private Vector3 offset;

    public static Matrix4 mat = new Matrix4();

    private float camSpeed = 4.0f;

    public Camera(GameBuffer gb, int x, int y) {
        this.gb = gb;
        position = new Vector3(0,0,0);
        offset = new Vector3(0,0,0);
    }

    private void translate() {
        if(offset.x < 0 && offset.y < 0) {
            mat.setTranslation(new Vector3(0, 0, 0));
        } else if (offset.x < 0) {
            mat.setTranslation(new Vector3(0, -offset.y, 0));
        } else if (offset.y < 0) {
            mat.setTranslation(new Vector3(-offset.x, 0, 0));
        } else if (offset.x > gb.getWorld().w * Tile.TILE_SIZE - gb.getGame().w &&
                offset.y > gb.getWorld().h * Tile.TILE_SIZE + gb.getGame().h) {
            mat.setTranslation(new Vector3(-(gb.getWorld().w * Tile.TILE_SIZE - gb.getGame().w),
                    -(gb.getWorld().h * Tile.TILE_SIZE - gb.getGame().h), 0));
        } else if (offset.x > gb.getWorld().w * Tile.TILE_SIZE - gb.getGame().w) {
            mat.setTranslation(new Vector3(-(gb.getWorld().w * Tile.TILE_SIZE - gb.getGame().w),
                    -offset.y, 0));
        } else if (offset.y > gb.getWorld().h * Tile.TILE_SIZE + gb.getGame().h) {
            mat.setTranslation(new Vector3(-offset.x,
                    -(gb.getWorld().h * Tile.TILE_SIZE - gb.getGame().h), 0));
        } else {
            mat.setTranslation(new Vector3(-offset.x, -offset.y, 0));
        }
    }

    public void centerOnEntity(Entity e) {
        float targxOffset = e.getX() - (gb.getGame().w / 2) + e.getWidth() / 2;
        float targyOffset = e.getY()- (gb.getGame().h / 2) + e.getHeight() / 2;

        offset.x += (((int)targxOffset - offset.x) * camSpeed * 0.01f);
        offset.y += (((int)targyOffset - offset.y) * camSpeed * 0.01f);
    }

    public void tick() {
        mat.getTranslation(position);
        translate();
    }

}
