package com.iwilkey.designa.gfx;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import com.iwilkey.designa.Game;
import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.entities.Entity;

public class Camera {

    private GameBuffer gb;

    private Vector3 position;
    private Vector2 scale;

    private Vector2 dPos;

    public static Matrix4 mat = new Matrix4();

    private float camSpeed = 4.0f;

    public Camera(GameBuffer gb, int x, int y) {
        this.gb = gb;
        position = new Vector3(x, y, 0);
        scale = new Vector2(1,1);

        dPos = new Vector2(0, 0);
    }

    private void checkWhiteSpace() {
        if(position.x < 0) {
            position.x = 0;
        }
    }

    public void centerOnEntity(Entity e) {

        float targxOffset = (Math.abs(Game.w - e.getX() - 32) - (gb.getGame().w / 2) + e.getWidth() / 2);
        float targyOffset = (Math.abs(Game.h - e.getY()) - (gb.getGame().h / 2) + e.getHeight() / 2) - 16;

        dPos.x = (((int)targxOffset - position.x) * camSpeed * 0.01f);
        dPos.y = (((int)targyOffset - position.y) * camSpeed * 0.01f);

        checkWhiteSpace();

    }

    public void tick() {
        mat.getTranslation(position);
        mat.translate(new Vector3(dPos.x, dPos.y, 0));
        mat.scale(scale.x, scale.y, 1);
    }


}
