package com.iwilkey.designa.entities.creature.violent;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.entities.creature.Enemy;

public class GroundBot extends Enemy {

    public TextureRegion currentTexture;

    public GroundBot(GameBuffer gb, float x, float y) {
        super(gb, x, y, DEFAULT_CREATURE_WIDTH, DEFAULT_CREATURE_HEIGHT);
        currentTexture = Assets.groundBotRight[0];
        facingLeft = true;
        collider.width = width - 9;
        collider.height = height;
        collider.x = (width / 2) - (collider.width / 2);
        collider.y = (height / 2) - (collider.height / 2) + 1;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Batch b) {
        b.draw(currentTexture, x, y, width, height);
    }

    @Override
    public void die() {

    }
}
