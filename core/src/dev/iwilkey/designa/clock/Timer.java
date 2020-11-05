package dev.iwilkey.designa.clock;

public class Timer {

    public int length;
    public boolean active;

    public double timer;
    Event event;

    public Timer(int length, Event event) {
        this.event = event;
        this.length = length;
        active = true;
    }

    public void tick(float dt) {
        if(!active) return;
        timer += 1 * (60.0 / (1 / dt));
        if(timer >= length) event();
    }

    void event() {
        event.onEvent();
        timer = 0;
    }

    public void disable() {
        timer = 0;
        active = false;
    }

    public void enable() {
        timer = 0;
        active = true;
    }

}
