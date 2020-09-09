package com.iwilkey.designa.states;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.Game;

public abstract class State {

    public static State currentState = null;

    public static State getCurrentState() {
        return currentState;
    }

    public static void setState(State s) {
        currentState = s;
    }

    protected void switchState(int index){
        Game.getStates().get(index).start();
        State.setState(Game.getStates().get(index));
    }

    public abstract void start();
    public abstract void tick();
    public abstract void render(Batch b);
    public abstract void onGUI(Batch b);
    public abstract void dispose();

}
