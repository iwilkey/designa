package com.iwilkey.designa.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import com.iwilkey.designa.Game;
import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.entities.Entity;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.world.World;

public class Camera {

    public static Matrix4 mat = new Matrix4();

    private final GameBuffer gb;
    public static Vector3 position;
    public static Vector3 scale = new Vector3(1,1,1);
    public static Vector3 offset;

    private final static float camSpeed = 4.0f;
    private static float targetZoom = 1;

    public static boolean isZooming = false;

    private static final float CAMERA_MAX_ZOOM = 6.0f,
        CAMERA_MIN_ZOOM = 0.65f;

    public Camera(GameBuffer gb, int x, int y) {
        this.gb = gb;
        position = new Vector3(x, y,0);
        offset = new Vector3(x - (Game.w / 2f), y - (Game.h / 2f),0);
    }

    private void translate() {
        int hx = (int) ((World.w * scale.x * Tile.TILE_SIZE) - (Game.w));
        int hy = (int) (World.h * scale.y * Tile.TILE_SIZE) - (Game.h);
        checkBounds(0, 0, hx, hy);
    }

    private void checkBounds(int lx, int ly, int hx, int hy) {

        if(offset.x < lx && offset.y > hy) mat.setToTranslationAndScaling(new Vector3(lx, -hy, 0), scale);
        else if(offset.x < lx) mat.setToTranslationAndScaling(new Vector3(lx, -offset.y, 0), scale);
        else if(offset.y > hy) mat.setToTranslationAndScaling(new Vector3(-offset.x, -hy, 0), scale);
        else if(offset.x > hx && offset.y < ly) mat.setToTranslationAndScaling(new Vector3(-hx, ly, 0), scale);
        else if(offset.x > hx) mat.setToTranslationAndScaling(new Vector3(-hx, -offset.y, 0), scale);
        else if(offset.y < ly) mat.setToTranslationAndScaling(new Vector3(-offset.x, ly, 0), scale);
        else mat.setToTranslationAndScaling(new Vector3(-(offset.x), -(offset.y), 0), scale);

    }

    public static void zoom(float amount, Entity e) {
        isZooming = true;

        if(targetZoom - amount > CAMERA_MAX_ZOOM) targetZoom = CAMERA_MAX_ZOOM;
        else if (targetZoom - amount < CAMERA_MIN_ZOOM) targetZoom = CAMERA_MIN_ZOOM;
        else targetZoom -= amount;

        scale.x = targetZoom;
        scale.y = targetZoom;

        offset.x = (int)(e.getX() * scale.x - (Game.w / 2f) + e.getWidth() / 2f);
        offset.y = (int)(e.getY() * scale.y - (Game.h / 2f) + e.getHeight() / 2f);

        InputHandler.zoomRequest = 0;

        Assets.zoom[MathUtils.random(0,2)].play(0.5f);

    }

    public void centerOnEntity(Entity e) {

        float targxOffset = e.getX() * scale.x - (Game.w / 2f) + e.getWidth() / 2f;
        float targyOffset = e.getY() * scale.y - (Game.h / 2f) + e.getHeight() / 2f;

        offset.x += (((int)targxOffset - offset.x) * camSpeed * Gdx.graphics.getDeltaTime());
        offset.y += (((int)targyOffset - offset.y) * camSpeed * Gdx.graphics.getDeltaTime());

        if(InputHandler.zoomRequest < 0 || InputHandler.zoomRequest > 0) zoom(InputHandler.zoomRequest, e);
    }

    long timer = 0, zoomBlack = 5;
    public void tick() {
        mat.getTranslation(position);
        translate();

        if(isZooming) {
            timer++;
            if(timer > zoomBlack) {
                isZooming = false;
                timer = 0;
            }
        }
    }

}
