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

    public static void addPipe(int x, int y, int dir, ArrayList<Item> items, ArrayList<Float> percentDone) {
        for(MachineType.Pipe pipe : pipes) if(pipe.x == x && pipe.y == y) return;
        pipes.add(new MachineType.Pipe(x, y, dir, items, percentDone));
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

    public static MachineType.Pipe returnPipeAt(int x, int y) {
        for(MachineType.Pipe p : pipes) {
            if(p.x == x && p.y == y) return p;
        }
        return null;
    }

    public static void removePipeAt(int x, int y) {
        pipes.removeIf(p -> p.x == x && p.y == y);
    }

    public static MachineType.MechanicalDrill returnDrillAt(int x, int y) {
        for(MachineType.MechanicalDrill d : drills) {
            if(d.x == x && d.y == y) return d;
        }
        return null;
    }

    public static void removeDrillAt(int x, int y) {
        drills.removeIf(p -> p.x == x && p.y == y);
    }

    public static MachineType.Node returnNodeAt(int x, int y) {
        for(MachineType.Node n : nodes) {
            if(n.x == x && n.y == y) return n;
        }
        return null;
    }

    public static void removeNodeAt(int x, int y) {
        nodes.removeIf(d -> d.x == x && d.y == y);
    }

    public static MachineType.Assembler returnAssemblerAt(int x, int y) {
        for(MachineType.Assembler a : assemblers) {
            if(a.x == x && a.y == y) return a;
        }
        return null;
    }

    public static void removeAssemblerAt(int x, int y) {
        assemblers.removeIf(a -> a.x == x && a.y == y);
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
