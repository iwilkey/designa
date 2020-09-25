package com.iwilkey.designa.machines;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.gui.Hud;
import com.iwilkey.designa.items.Item;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.world.World;

import java.util.ArrayList;

public class MachineHandler {

    public static ArrayList<MachineType.Pipe> pipes = new ArrayList<>();
    public static ArrayList<MachineType.MechanicalDrill> drills = new ArrayList<>();
    public static ArrayList<MachineType.Node> nodes = new ArrayList<>();
    public static ArrayList<MachineType.Assembler> assemblers = new ArrayList<>();

    private GameBuffer gb;

    public MachineHandler(GameBuffer gb) {
        this.gb = gb;
    }

    public static void addPipe(int x, int y) {
        for(MachineType.Pipe pipe : pipes) if(pipe.x == x && pipe.y == y) return;
        pipes.add(new MachineType.Pipe(x, y, Hud.SELECTED_PIPE_DIRECTION));
    }

    public static void addPipe(int x, int y, int dir) {
        for(MachineType.Pipe pipe : pipes) if(pipe.x == x && pipe.y == y) return;
        pipes.add(new MachineType.Pipe(x, y, dir));
    }


    public static void addMechanicalDrill(int x, int y, Tile miningResource, MachineType.MechanicalDrill.ResourceType resourceType) {
        for(MachineType.MechanicalDrill drill : drills) if(drill.x == x && drill.y == y) return;
        drills.add(new MachineType.MechanicalDrill(x, y, miningResource, resourceType));
    }

    public static void addNode(int x, int y) {
        for(MachineType.Node node : nodes) if(node.x == x && node.y == y) return;
        nodes.add(new MachineType.Node(x, y));
    }

    public static void addAssembler(int x, int y) {
        for(MachineType.Assembler assembler : assemblers) if(assembler.x == x && assembler.y == y) return;
        assemblers.add(new MachineType.Assembler(x, y));
    }

    public static void addAssembler(int x, int y, Item item) {
        for(MachineType.Assembler assembler : assemblers) if(assembler.x == x && assembler.y == y) return;
        assemblers.add(new MachineType.Assembler(x, y, item));
    }


    public void tick() {
        for(MachineType.Node node : nodes) node.tick();
        for(MachineType.Pipe pipe : pipes) pipe.tick();
        for(MachineType.Assembler assembler : assemblers) assembler.tick();
    }

    public void render(Batch b) {
        for(MachineType.Node node : nodes) node.render(b, node.x, World.h - node.y - 1);
        for(MachineType.Assembler assembler : assemblers) assembler.render(b, assembler.x, World.h - assembler.y - 1);
        for(int i = 0; i < pipes.size(); i++) {
            int ii = pipes.size() - i - 1;
            pipes.get(ii).render(b, pipes.get(ii).x, pipes.get(ii).y, pipes.get(ii).direction);
        }
    }

}
