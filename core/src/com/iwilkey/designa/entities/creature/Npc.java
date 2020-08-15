package com.iwilkey.designa.entities.creature;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.MathUtils;
import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Text;

public class Npc extends Creature {

    TextureRegion currentTexture;
    long DECISION_TIME = 100;
    long timer = 0;
    boolean walkLeft = true;
    String name;

    public Npc(GameBuffer gb, float x, float y) {
        super(gb, x, y, Creature.DEFAULT_CREATURE_WIDTH, Creature.DEFAULT_CREATURE_HEIGHT);

        name = Assets.maleNames[MathUtils.random(0, Assets.maleNames.length - 1)];
        speed = MathUtils.random(DEFAULT_SPEED - 0.4f, DEFAULT_SPEED + 0.4f);

        facingLeft = true;
        facingRight = false;
        currentTexture = Assets.man[0];

        collider.width = width - 9;
        collider.height = height;
        collider.x = (width / 2) - (collider.width / 2);
        collider.y = (height / 2) - (collider.height / 2) + 1;

    }

    // TODO: Make an AI Behavior web that Npcs can choose actions on.
    // TODO: Then, apply perlin noise with time and see that happens.

    @Override
    public void tick() {

        checkStuck();

        timer++;
        if(timer > DECISION_TIME) {
            walkLeft = MathUtils.random(0, 1) != 1;
            facingLeft = walkLeft;
            facingRight = !facingLeft;
            if(!isJumping && isGrounded) jump();
            DECISION_TIME = MathUtils.random(1, 200);
            timer = 0;
        }

        move();
        xMove = (walkLeft) ? (speed / 2) : -(speed / 2);

    }

    @Override
    public void render(Batch b) {

        if(facingLeft) currentTexture = Assets.man[0];
        else currentTexture = Assets.man[1];

        b.draw(currentTexture, x, y, width, height);
        Text.draw(b, name, (int)(x + (width / 2)) - ((name.length() * 4) / 2), (int)y + 36, 4);

    }

    @Override
    public void die() {

    }

}
