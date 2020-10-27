package dev.iwilkey.designa.gfx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.iwilkey.designa.clock.Clock;
import dev.iwilkey.designa.clock.Event;
import dev.iwilkey.designa.clock.Timer;

public class Animation {

    private final Timer timer;
    private final TextureRegion[] frames;
    private short speed, index;

    public Animation(int speed, final TextureRegion[] frames) {
        this.speed = (short)speed;
        this.frames = frames;
        index = 0;

        timer = Clock.addTimer(new Timer(this.speed, new Event() {
            @Override
            public void onEvent() {
                index++;
                if(index >= frames.length) index = 0;
            }
        }));
    }

    public TextureRegion getCurrentFrame() { return frames[index]; }

    public void changeSpeed(int s) {
        this.speed = (short)s;
        timer.length = this.speed;
    }

}
