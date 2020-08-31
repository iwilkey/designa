package com.iwilkey.designa.machines;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.GameBuffer;

import java.util.ArrayList;

public class MachineHandler {

    public static ArrayList<MechanicalDrill> drills = new ArrayList<>();
    public PipeHandler pipeHandler;

    private GameBuffer gb;

    public MachineHandler(GameBuffer gb) {
        this.gb = gb;
        pipeHandler = new PipeHandler();
    }

    public void tick() {
        pipeHandler.tick();
    }

    public void render(Batch b) {
    }

}
