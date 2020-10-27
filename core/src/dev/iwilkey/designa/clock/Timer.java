package dev.iwilkey.designa.clock;

public class Timer {

    public int length;
    public boolean active;

    double timer;
    Event event;

    public Timer(int length, Event event) {
        this.event = event;
        this.length = length;
        active = true;
    }

    public void tick(float dt) {
        if(!active) return;
        timer += 1 * (60.0 / (1 / dt));
        if(timer >= length) {
            event();
            timer = 0;
        }
    }

    void event() { event.onEvent(); }

}
