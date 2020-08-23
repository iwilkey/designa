package com.iwilkey.designa.entities.creature;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.MathUtils;
import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Animation;
import com.iwilkey.designa.gfx.Text;

public class Npc extends Creature {

    // Animations
    private Animation[] animations;

    TextureRegion currentTexture;
    long DECISION_TIME = 100;
    long timer = 0;
    boolean walkLeft = true;
    String name;
    int heartSpacing = 4;

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

        // Init Animations
        animations = new Animation[2];
        animations[0] = new Animation(MathUtils.random(80, 150), Assets.manWalkRight);
        animations[1] = new Animation(MathUtils.random(80, 150), Assets.manWalkLeft);

    }

    // TODO: Make an AI Behavior web that Npcs can choose actions on.
    // TODO: Then, apply perlin noise with time and see that happens.

    // The brain
    @Override
    public void tick() {

        checkStuck();

        for(Animation anim : animations) anim.tick();

        timer++;
        if(timer > DECISION_TIME) {
            walkLeft = MathUtils.random(0, 1) != 1;
            facingLeft = walkLeft; facingRight = !facingLeft;
            if(!isJumping && isGrounded) jump();
            DECISION_TIME = MathUtils.random(1, 200);
            timer = 0;
        }

        move();
        xMove = (walkLeft) ? (speed / 2) : -(speed / 2);

    }

    @Override
    public void render(Batch b) {

        b.draw(currentSprite(), x, y, width, height);

        for (int i = 0; i < 10; i++) {
            if (getHealth() >= i + 1) {
                // Down, to the left
                b.draw(Assets.heart[0], (x + (i * heartSpacing)) - 10,
                        y + 33, 4, 4);
            } else {
                b.draw(Assets.heart[1], (x + (i * heartSpacing)) - 10,
                        y + 33, 4, 4);
            }
        }

        Text.draw(b, name, (int)(x + (width / 2)) - ((name.length() * 5) / 2), (int)y + 40, 4);

    }

    private TextureRegion currentSprite() {
        if(isMoving && isGrounded) {
            if(facingLeft) {
                return animations[1].getCurrentFrame();
            } else if (facingRight){
                return animations[0].getCurrentFrame();
            }
        } else if (isMoving) {
            if(facingLeft) {
                return Assets.manJump[0];
            } else {
                return Assets.manJump[1];
            }
        } else {
            if(facingLeft) {
                return Assets.man[0];
            } else {
                return Assets.man[1];
            }
        }

        return null;
    }

    @Override
    public void die() {

    }

}
