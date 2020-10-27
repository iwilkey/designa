package dev.iwilkey.designa.clock;

import dev.iwilkey.designa.input.InputHandler;
import dev.iwilkey.designa.scene.Scene;

import java.util.ArrayList;

// This class ticks everything and facilitates timers.
public class Clock {

    public static int FPS;
    public static ArrayList<Timer> timers = new ArrayList<>();

    long lt = System.nanoTime(); long now = 0;
    long timer = 0; byte ticks = 0;

    public void masterClock(float dt) {

        now = System.nanoTime();
        timer += (now - lt);
        lt = now;
        ticks++;
        if(timer > 1000000000) {
            FPS = ticks;
            ticks = 0;
            timer = 0;
        }

        if(Scene.currentScene != null) {
            InputHandler.tick();
            Scene.currentScene.tick();
            for(Timer t : timers) t.tick(dt);
        }
    }

    public static Timer addTimer(Timer timer) {
        timers.add(timer);
        return timer;
    }

    public void onResize(int width, int height) {
        if(Scene.currentScene != null) {
            Scene.currentScene.onResize(width, height);
        }
    }

}
