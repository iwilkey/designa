package com.iwilkey.designa.gfx;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.entities.Entity;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.tiles.Tile;

public class Camera {

    public static Matrix4 mat = new Matrix4();

    private GameBuffer gb;
    public static Vector3 position;
    public static Vector3 scale = new Vector3(1,1,1);
    public static Vector3 offset;

    private static float camSpeed = 4.0f;
    private static float targetZoom = 1;

    public Camera(GameBuffer gb, int x, int y) {
        this.gb = gb;
        position = new Vector3(0,0,0);
        offset = new Vector3(0,0,0);
    }

    private void translate() {
        int hx = (int) (gb.getWorld().w * scale.x * Tile.TILE_SIZE - gb.getGame().w);
        int hy = (int) (gb.getWorld().h * scale.y * Tile.TILE_SIZE + 100);
        checkBounds(0, 0, hx, hy);
    }

    private void checkBounds(int lx, int ly, int hx, int hy) {
        if(offset.x < lx && offset.y < ly) {
            mat.setToTranslationAndScaling(new Vector3(lx, ly, 0), scale);
        } else if (offset.x < lx) {
            mat.setToTranslationAndScaling(new Vector3(lx, -offset.y, 0), scale);
        } else if (offset.y < ly) {
            mat.setToTranslationAndScaling(new Vector3(-offset.x, ly, 0), scale);
        } else if (offset.x > hx && offset.y > hy) {
            mat.setToTranslationAndScaling(new Vector3(-hx, -hy, 0), scale);
        } else if (offset.x > hx) {
            mat.setToTranslationAndScaling(new Vector3(-hx, -offset.y, 0), scale);
        } else if (offset.y > hy) {
            mat.setToTranslationAndScaling(new Vector3(-offset.x, -hy, 0), scale);
        } else {
            mat.setToTranslationAndScaling(new Vector3(-(offset.x), -(offset.y), 0), scale);
        }
    }

    public static void zoom(float amount) {

        if(targetZoom - amount > 3) {
            targetZoom = 3;
        } else if (targetZoom - amount < 1) {
            targetZoom = 1;
        } else {
            targetZoom -= amount;
        }

        scale.x += (((targetZoom - scale.x) * camSpeed * 4 * 0.01f));
        scale.y += (((targetZoom - scale.y) * camSpeed * 4 * 0.01f));

        InputHandler.zoomRequest = 0;

    }

    public void centerOnEntity(Entity e) {

        float targxOffset = e.getX() * scale.x - (gb.getGame().w / 2) + e.getWidth() / 2;
        float targyOffset = e.getY() * scale.y - (gb.getGame().h / 2) + e.getHeight() / 2;

        offset.x += (((int)targxOffset - offset.x) * camSpeed * 0.01f);
        offset.y += (((int)targyOffset - offset.y) * camSpeed * 0.01f);

        zoom(InputHandler.zoomRequest);
    }

    public void tick() {
        mat.getTranslation(position);
        translate();
    }

}
