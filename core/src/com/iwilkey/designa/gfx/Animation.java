package com.iwilkey.designa.gfx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {

    private int speed, index;
    private long lt, timer;
    private final TextureRegion[] frames;

    public Animation(int speed, TextureRegion[] frames) {
        this.speed = speed;
        this.frames = frames;
        index = 0;
        timer = 0;
        lt = System.currentTimeMillis();
    }

    public void tick() {
        timer += System.currentTimeMillis() - lt;
        lt = System.currentTimeMillis();

        if(timer > speed) {
            index++;
            timer = 0;

            if(index >= frames.length) {
                index = 0;
            }
        }
    }

    public TextureRegion getCurrentFrame() {
        return frames[index];
    }

    public void changeSpeed(int s) {
        speed = s;
    }

}
