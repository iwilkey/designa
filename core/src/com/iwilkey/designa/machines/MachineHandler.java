package com.iwilkey.designa.machines;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.gui.Hud;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.world.World;

import java.util.ArrayList;

public class MachineHandler {

    public static ArrayList<MachineType.Pipe> pipes = new ArrayList<>();
    public static ArrayList<MachineType.MechanicalDrill> drills = new ArrayList<>();
    public static ArrayList<MachineType.Node> nodes = new ArrayList<>();

    private GameBuffer gb;

    public MachineHandler(GameBuffer gb) {
        this.gb = gb;
    }

    public static void addPipe(int x, int y) {
        pipes.add(new MachineType.Pipe(x, y, Hud.SELECTED_PIPE_DIRECTION));
    }

    public static void addMechanicalDrill(int x, int y, Tile miningResource, MachineType.MechanicalDrill.ResourceType resourceType) {
        drills.add(new MachineType.MechanicalDrill(x, y, miningResource, resourceType));
    }

    public static void addNode(int x, int y) {
        nodes.add(new MachineType.Node(x, y, Hud.SELECTED_PIPE_DIRECTION));
    }

    public void tick() {
        for(MachineType.Pipe pipe : pipes) pipe.tick();
    }

    public void render(Batch b) {
        for(MachineType.Node node : nodes) node.render(b, node.x, World.h - node.y - 1, node.direction);
        for(int i = 0; i < pipes.size(); i++) {
            int ii = pipes.size() - i - 1;
            pipes.get(ii).render(b, pipes.get(ii).x, pipes.get(ii).y, pipes.get(ii).direction);
        }
    }

}
