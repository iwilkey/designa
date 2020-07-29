package com.iwilkey.designa.entities.creature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;

public class Player extends Creature {

    public Player(GameBuffer gb, float x, float y) {
        super(gb, x, y, Creature.DEFAULT_CREATURE_WIDTH, Creature.DEFAULT_CREATURE_HEIGHT);

        // Setting the collider
        collider.width = width - 9;
        collider.height = height;
        collider.x = (width / 2) - (collider.width / 2);
        collider.y = (height / 2) - (collider.height / 2) + 1;

    }

    private void control() {
        xMove = 0;

        // Desktop
        if(Gdx.input.isKeyPressed(Input.Keys.D)) xMove += speed;
        if(Gdx.input.isKeyPressed(Input.Keys.A)) xMove -= speed;
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !isJumping && isGrounded) jump();
    }

    @Override
    public void tick() {

        // Move
        control();
        move();

        // Center camera
        gb.getCamera().centerOnEntity(this);

    }

    @Override
    public void render(Batch b) {
        b.draw(Assets.player, x, y, width, height);
    }

    @Override
    public void die() {

    }

    public GameBuffer getGameBuffer() { return gb; }

}
