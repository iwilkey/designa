package com.iwilkey.designa.machines;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.tiles.Tile;

import java.util.ArrayList;

public class MachineHandler {

    public static ArrayList<MachineType.MechanicalDrill> drills = new ArrayList<>();
    public static ArrayList<MachineType.Offloader> offloaders = new ArrayList<>();
    public PipeHandler pipeHandler;

    private GameBuffer gb;

    public MachineHandler(GameBuffer gb) {
        this.gb = gb;
        pipeHandler = new PipeHandler();
    }

    public static void addMechanicalDrill(int x, int y, Tile miningResource, MachineType.MechanicalDrill.ResourceType resourceType) {
        drills.add(new MachineType.MechanicalDrill(x, y, miningResource, resourceType));
    }

    public static void addOffloader(int x, int y, MachineType.Offloader.Direction direction) {
        offloaders.add(new MachineType.Offloader(x, y, direction));
    }

    public void tick() {
        pipeHandler.tick();
    }

    public void render(Batch b) {
    }

}
