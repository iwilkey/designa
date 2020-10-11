package com.iwilkey.designa.machines;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.utils.Null;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.defense.WeaponHandler;
import com.iwilkey.designa.defense.WeaponType;
import com.iwilkey.designa.gfx.Camera;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.inventory.InventorySlot;
import com.iwilkey.designa.inventory.ToolSlot;
import com.iwilkey.designa.inventory.crate.Crate;
import com.iwilkey.designa.items.Item;
import com.iwilkey.designa.items.ItemRecipe;
import com.iwilkey.designa.items.ItemType;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.utils.Utils;
import com.iwilkey.designa.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MachineType {

    public enum Direction {
        RIGHT, DOWN, LEFT, UP
    }

    // Pipe
    public static class Pipe {

        public int x, y; // Where is this pipe located?
        public Direction direction = null; // What direction is this pipe?
        public Tile source = null; // What is this pipe getting objects from?
        public Tile destination = null; // Where is this pipe going to?
        public ArrayList<Item> currentItems = new ArrayList<>(); // What items are this pipe transporting?
        public ArrayList<Float> percentItemTraveled = new ArrayList<>(); // Where is each item in the pipe?
        public final int ITEM_CAP = 99; // How many items can be in a pipe at a time?
        public float CONVEY_SPEED = 0.78f; // How fast do the items move?
        public int TIME_MULTIPLIER;
        long timer = 0, newItem = 100; // Timers

        public Pipe(int x, int y, int direction) {
            this.x = x; this.y = y;
            switch(direction) {
                case 0: this.direction = Direction.RIGHT; break;
                case 1: this.direction = Direction.DOWN; break;
                case 2: this.direction = Direction.LEFT; break;
                case 3: this.direction = Direction.UP; break;
            }
        }

        public void tick() {

            if(InputHandler.speedUpTimeRequest) {
                TIME_MULTIPLIER = 15;
            } else TIME_MULTIPLIER = 1;

            // CONVEY_SPEED = 0.78f * TIME_MULTIPLIER;


            if(currentItems.size() != 0) {
                for(int i = 0; i < percentItemTraveled.size(); i++) {
                    if(percentItemTraveled.get(i) >= 100) {
                        percentItemTraveled.set(i, 100.0f); break;
                    }
                    percentItemTraveled.set(i, percentItemTraveled.get(i) + (CONVEY_SPEED * TIME_MULTIPLIER));
                }
            }

            switch(direction) {
                case RIGHT: source = checkLeft(); destination = checkRight(); break;
                case LEFT: source = checkRight(); destination = checkLeft(); break;
                case UP: source = checkDown(); destination = checkUp(); break;
                case DOWN: source = checkUp(); destination = checkDown(); break;
            }

            timer += 1 * TIME_MULTIPLIER;
            if(timer > newItem) {
                // If the source is a drill...
                if (source == Assets.copperMechanicalDrillTile) {
                    if (currentItems.size() + 1 < ITEM_CAP) {
                        // TODO: Make the item added the item the drill is drilling.
                        int xx = 0; int yy = 0;
                        switch(direction) {
                            case RIGHT: xx = x - 1; yy = World.h - y - 1; break;
                            case LEFT: xx = x + 1; yy = World.h - y - 1; break;
                            case UP: yy = World.h - (y - 1) - 1; xx = x; break;
                            case DOWN: yy = World.h - (y + 1) - 1; xx = x; break;
                        }

                        Item drilledItem = null;
                        for(MachineType.MechanicalDrill drill : MachineHandler.drills) {
                            if(drill.x == xx && drill.y == yy) {
                                drilledItem = Item.getItemByID(drill.miningResource.getItemID());
                                if(newItem != drill.getMiningSpeed()) newItem = drill.getMiningSpeed();
                                if(CONVEY_SPEED != drill.getConveySpeed()) CONVEY_SPEED = drill.getConveySpeed();
                            }
                        }
                        addItemForTransport(drilledItem);
                    }
                }
                if(source == Assets.crateTile) unloadFromCrate();
                timer = 0;
            }

            if(source == Assets.stonePipeTile) offloadToPipe();
            if(destination == Assets.crateTile) offloadToCrate();
            if(destination == Assets.nodeTile) offloadToNode();
            if(destination == Assets.assemblerTile) offloadToAssembler();
            if(destination == Assets.blasterBaseTile) offloadToBlaster();

        }

        private void addItemForTransport(Item item) {
            currentItems.add(item); percentItemTraveled.add(0.0f);
        }

        private void removeItemFromTransport(int index) {
            currentItems.remove(index); percentItemTraveled.remove(index);
        }

        private void offloadToBlaster() {
            for(int i = 0; i < percentItemTraveled.size(); i++) {
                if (percentItemTraveled.get(i) >= 100.0f) {
                    int xx = 0; int yy = 0;
                    switch (direction) {
                        case RIGHT: xx = x + 1; yy = y; break;
                        case LEFT: xx = x - 1; yy = y; break;
                        case UP: yy = y + 1; xx = x; break;
                        case DOWN: yy = y - 1; xx = x; break;
                    }
                    if (WeaponHandler.checkLocationForBlaster(xx, yy) != null) {
                        WeaponType.SimpleBlaster blaster = WeaponHandler.checkLocationForBlaster(xx, yy);
                        removeItemFromTransport(i);
                        // TODO: Make this specific for what was actually in the pipe.
                        blaster.ammo.add(WeaponType.AmmoType.COPPER);
                        return;
                    }
                }
            }
        }

        private void offloadToPipe() {
            int xx = 0; int yy = 0;
            switch(direction) {
                case RIGHT: xx = x - 1; yy = y; break;
                case LEFT: xx = x + 1; yy = y; break;
                case UP: xx = x; yy = y - 1; break;
                case DOWN: xx = x; yy = y + 1; break;
            }

            if(returnCompletedItem(xx, yy) != null) {
                if (currentItems.size() + 1 < ITEM_CAP) {
                    addItemForTransport(returnCompletedItem(xx, yy));
                    for(MachineType.Pipe pipe : MachineHandler.pipes) {
                        if (pipe.x == xx && pipe.y == yy)
                            for(int i = 0; i < pipe.currentItems.size(); i++)
                                pipe.removeItemFromTransport(i);
                    }
                }
            }
        }

        private void offloadToAssembler() {
            Item item; int xx = 0; int yy = 0;
            for(int i = 0; i < percentItemTraveled.size(); i++) {
                if (percentItemTraveled.get(i) >= 100.0f) {
                    item = currentItems.get(i);
                    switch (direction) {
                        case RIGHT: xx = x + 1; yy = y; break;
                        case LEFT: xx = x - 1; yy = y; break;
                        case UP: yy = y + 1; xx = x; break;
                        case DOWN: yy = y - 1; xx = x; break;
                    }

                    for(MachineType.Assembler assembler : MachineHandler.assemblers)
                        if(assembler.x == xx && assembler.y == World.h - yy - 1) {
                            assembler.addToStorage(item);
                        }
                    removeItemFromTransport(i);
                    return;
                }
            }
        }

        private void offloadToCrate() {
            Item item; int xx = 0; int yy = 0;
            for(int i = 0; i < percentItemTraveled.size(); i++) {
                if(percentItemTraveled.get(i) >= 100.0f) {
                    item = currentItems.get(i);
                    switch(direction) {
                        case RIGHT: xx = x + 1; yy = y; break;
                        case LEFT: xx = x - 1; yy = y; break;
                        case UP: yy = y + 1; xx = x; break;
                        case DOWN: yy = y - 1; xx = x; break;
                    }
                    for(Crate crate : World.getEntityHandler().getPlayer().crates)
                        if(crate.x == xx && crate.y == yy) crate.addItem(item);
                    removeItemFromTransport(i);
                    return;
                }
            }
        }

        private void unloadFromCrate() {
            Crate crate = null; int xx = 0; int yy = 0;
            switch(direction) {
                case RIGHT: xx = x - 1; yy = y; break;
                case LEFT: xx = x + 1; yy = y; break;
                case UP: xx = x; yy = y - 1; break;
                case DOWN: xx = x; yy = y + 1; break;
            }

            for(Crate crt : World.getEntityHandler().getPlayer().crates)
                if(crt.x == xx && crt.y == yy) crate = crt;

            for (int y = 0; y < 400 / InventorySlot.SLOT_HEIGHT; y++) {
                for (int x = 0; x < 400 / InventorySlot.SLOT_WIDTH; x++) {
                    assert crate != null;
                    if (crate.storage[x][y].getItem() != null) {
                        if (crate.storage[x][y].itemCount - 1 >= 1) {
                            Item item = crate.storage[x][y].getItem();
                            if (currentItems.size() + 1 <= ITEM_CAP) addItemForTransport(item);
                            else return;
                            crate.storage[x][y].itemCount--;
                        } else {
                            Item item = crate.storage[x][y].getItem();
                            if (currentItems.size() + 1 <= ITEM_CAP) addItemForTransport(item);
                            else return;
                            crate.storage[x][y].putItem(null, 0);
                        }
                    }
                }
            }
        }

        private void offloadToNode() {
            Item item; int xx = 0; int yy = 0;
            for(int i = 0; i < percentItemTraveled.size(); i++) {
                if (percentItemTraveled.get(i) >= 100.0f) {
                    item = currentItems.get(i);
                    switch (direction) {
                        case RIGHT: xx = x + 1; yy = y; break;
                        case LEFT: xx = x - 1; yy = y; break;
                        case UP: yy = y + 1; xx = x; break;
                        case DOWN: yy = y - 1; xx = x; break;
                    }

                    for(MachineType.Node node : MachineHandler.nodes)
                        if(node.x == xx && node.y == World.h - yy - 1) {
                            if(node.storedItems.size() + 1 <= node.STORAGE_CAP) node.addToStorage(item);
                            else return;
                        }
                    removeItemFromTransport(i);
                    return;
                }
            }
        }

        private Tile checkUp() {
            if(World.getTile(x, y + 1) != Assets.airTile) return World.getTile(x, y + 1);
            return Assets.airTile;
        }
        private Tile checkDown() {
            if(World.getTile(x, y - 1) != Assets.airTile) return World.getTile(x, y - 1);
            return Assets.airTile;
        }
        private Tile checkRight() {
            if(World.getTile(x + 1, y) != Assets.airTile) return World.getTile(x + 1, y);
            return Assets.airTile;
        }
        private Tile checkLeft() {
            if(World.getTile(x - 1, y) != Assets.airTile) return World.getTile(x - 1, y);
            return Assets.airTile;
        }

        private Item returnCompletedItem(int x, int y) {

            for(MachineType.Pipe pipe : MachineHandler.pipes) {
                if(pipe.x == x && pipe.y == y) {
                    for(int i = 0; i < pipe.currentItems.size(); i++) {
                        if(pipe.percentItemTraveled.get(i) >= 100.0f) {
                            return pipe.currentItems.get(i);
                        }
                    }
                }
            }
            return null; // Nothing is completed yet.
        }

        public void setDirection(Direction direction) {
            this.direction = direction;
        }

        public void render(Batch b, int x, int y, Direction direction) {
            try {
                if (currentItems.size() != 0) {
                    for (int i = 0; i < currentItems.size(); i++) {
                        Item item = currentItems.get(i);
                        float percentDone = percentItemTraveled.get(i);

                        int xx = 0;
                        int yy = 0;
                        switch(direction) {
                            case RIGHT:
                                yy = y * Tile.TILE_SIZE + 4;
                                xx = (int)(((x * Tile.TILE_SIZE) - 8) + (Tile.TILE_SIZE * (percentDone / 100.0f)) - (i + 1));
                                break;
                            case LEFT:
                                yy = y * Tile.TILE_SIZE + 4;
                                xx = (int) ((((x * Tile.TILE_SIZE) + Tile.TILE_SIZE)) - (Tile.TILE_SIZE * (percentDone / 100.0f)) + (i + 1));
                                break;
                            case UP:
                                xx = x * Tile.TILE_SIZE + 4;
                                yy = (int)(((y * Tile.TILE_SIZE) - 8) + (Tile.TILE_SIZE * (percentDone / 100.0f)) - (i + 1));
                                break;
                            case DOWN:
                                xx = x * Tile.TILE_SIZE + 4;
                                yy = (int) ((((y * Tile.TILE_SIZE) + Tile.TILE_SIZE)) - (Tile.TILE_SIZE * (percentDone / 100.0f)) + (i + 1));
                                break;
                        }
                        b.draw(item.getTexture(), xx, yy, 8, 8);
                    }
                }
            } catch (NullPointerException ignored) {}

            switch(direction) {
                case RIGHT: b.draw(Tile.Pipe.StonePipe.animations[0].getCurrentFrame(),
                            x * Tile.TILE_SIZE, y * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
                    break;
                case LEFT: b.draw(Tile.Pipe.StonePipe.animations[2].getCurrentFrame(),
                            x * Tile.TILE_SIZE, y * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
                    break;
                case DOWN: b.draw(Tile.Pipe.StonePipe.animations[1].getCurrentFrame(),
                            x * Tile.TILE_SIZE, y * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
                    break;
                case UP: b.draw(Tile.Pipe.StonePipe.animations[3].getCurrentFrame(),
                            x * Tile.TILE_SIZE, y * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
                    break;
            }
        }
    }

    // MechanicalDrill
    public static class MechanicalDrill {
        public enum ResourceType {
            COPPER, SILVER, IRON, DIAMOND
        }
        public Tile miningResource;
        public ResourceType resourceType;
        public int x, y;
        public MechanicalDrill(int x, int y, Tile miningResource, ResourceType rt) {
            this.resourceType = rt;
            this.miningResource = miningResource;
            this.x = x; this.y = y;
        }
        public void tick() {}
        public int getMiningSpeed() {
            switch(resourceType) {
                case COPPER:
                default: return 100;
                case SILVER: return 50;
                case IRON: return 25;
                case DIAMOND: return 10;
            }
        }
        public float getConveySpeed() {
            switch(resourceType) {
                case COPPER:
                default: return 0.5f;
                case SILVER: return 1.0f;
                case IRON: return 2.0f;
                case DIAMOND: return 4.0f;
            }
        }
    }

    // Node
    public static class Node {

        public int x, y;
        public HashMap<Direction, Integer> io = new HashMap<>(); // 0 is input, 1 output, -1 neither, 2 for crate, 3 is other node.
        public ArrayList<Direction> outputs = new ArrayList<>();
        public HashMap<Item, Integer> storedItems = new HashMap<>();
        public long timer = 0, offloadMax = 20;
        public final int STORAGE_CAP = 1;

        public Node(int x, int y) {
            this.x = x; this.y = y;
            io.put(Direction.RIGHT, -1);
            io.put(Direction.DOWN, -1);
            io.put(Direction.LEFT, -1);
            io.put(Direction.UP, -1);
        }

        public void tick() {
            deferIO();

            if(storedItems.size() > 0) {
                timer++;
                if (timer > offloadMax) {
                    deferOffloadDirection();
                    timer = 0;
                }
            }
        }

        private void deferIO() {
            // Up
            if(checkUp() == Assets.stonePipeTile) {
                for(MachineType.Pipe pipe : MachineHandler.pipes) {
                    if(pipe.x == x && pipe.y == World.h - (y - 1) - 1) {
                        if(pipe.direction == Direction.UP) io.put(Direction.UP, 1);
                        else if(pipe.direction == Direction.DOWN) io.put(Direction.UP, 0);
                        else io.put(Direction.UP, -1);
                        break;
                    }
                }
            } else if (checkUp() == Assets.crateTile) {
                io.put(Direction.UP, 2);
            } else if (checkUp() == Assets.nodeTile) {
                io.put(Direction.UP, 3);
            }

            // Down
            if(checkDown() == Assets.stonePipeTile) {
                for(MachineType.Pipe pipe : MachineHandler.pipes) {
                    if(pipe.x == x && pipe.y == World.h - (y + 1) - 1) {
                        if(pipe.direction == Direction.DOWN) io.put(Direction.DOWN, 1);
                        else if(pipe.direction == Direction.UP) io.put(Direction.DOWN, 0);
                        else io.put(Direction.DOWN, -1);
                        break;
                    }
                }
            } else if (checkDown() == Assets.crateTile) {
                io.put(Direction.DOWN, 2);
            } else if (checkDown() == Assets.nodeTile) {
                io.put(Direction.DOWN, 3);
            }

            // Right
            if(checkRight() == Assets.stonePipeTile) {
                for(MachineType.Pipe pipe : MachineHandler.pipes) {
                    if(pipe.x == x + 1 && pipe.y == World.h - y - 1) {
                        if(pipe.direction == Direction.RIGHT) io.put(Direction.RIGHT, 1);
                        else if(pipe.direction == Direction.LEFT) io.put(Direction.RIGHT, 0);
                        else io.put(Direction.RIGHT, -1);
                        break;
                    }
                }
            } else if (checkRight() == Assets.crateTile) {
                io.put(Direction.RIGHT, 2);
            } else if (checkRight() == Assets.nodeTile) {
                io.put(Direction.RIGHT, 3);
            }


            // Left
            if(checkLeft() == Assets.stonePipeTile) {
                for(MachineType.Pipe pipe : MachineHandler.pipes) {
                    if(pipe.x == x - 1 && pipe.y == World.h - y - 1) {
                        if(pipe.direction == Direction.LEFT) io.put(Direction.LEFT, 1);
                        else if(pipe.direction == Direction.RIGHT) io.put(Direction.LEFT, 0);
                        else io.put(Direction.LEFT, -1);
                        break;
                    }
                }
            } else if (checkLeft() == Assets.crateTile) {
                io.put(Direction.LEFT, 2);
            } else if (checkLeft() == Assets.nodeTile) {
                io.put(Direction.LEFT, 3);
            }

            updateOutputList();
        }

        private void updateOutputList() {
            outputs.clear();
            for(Map.Entry<Direction, Integer> node : io.entrySet()) {
                if(node.getValue() == 1) {
                    outputs.add(node.getKey());
                } else if (node.getValue() == 2) {
                    outputs.add(node.getKey());
                } else if (node.getValue() == 3) {
                    outputs.add(node.getKey());
                }
            }
        }


        public void addToStorage(Item item) {
            if(storedItems.containsKey(item)) storedItems.put(item, storedItems.get(item) + 1);
            else storedItems.put(item, 1);
        }

        private ArrayList<Direction> usedOutputs = new ArrayList<>();
        private void deferOffloadDirection() {
            if(usedOutputs.containsAll(outputs)) usedOutputs.clear();
            for(Map.Entry<Direction, Integer> node : io.entrySet()) {
                if(node.getValue() == 1 && !usedOutputs.contains(node.getKey())) {
                    usedOutputs.add(node.getKey());
                    offload(node.getKey());
                    break;
                } else if (node.getValue() == 2 && !usedOutputs.contains(node.getKey())) {
                    usedOutputs.add(node.getKey());
                    offloadToCrate(node.getKey());
                    break;
                } else if (node.getValue() == 3 && !usedOutputs.contains(node.getKey())) {
                    usedOutputs.add(node.getKey());
                    offloadToNode(node.getKey());
                    break;
                }
            }
        }

        private void offload(Direction dir) {
            int xx = 0; int yy = 0;
            switch(dir) {
                case RIGHT: xx = x + 1; yy = World.h - y - 1; break;
                case LEFT: xx = x - 1; yy = World.h - y - 1; break;
                case UP: yy = World.h - (y - 1) - 1; xx = x; break;
                case DOWN: yy = World.h - (y + 1) - 1; xx = x; break;
            }

            for(MachineType.Pipe pipe : MachineHandler.pipes) {
                if(pipe.x == xx && pipe.y == yy) {
                    for(Map.Entry<Item, Integer> item : storedItems.entrySet()) {
                        if(item.getValue() - 1 < 0) {
                            if (pipe.currentItems.size() + 1 < pipe.ITEM_CAP) {
                                pipe.addItemForTransport(item.getKey());
                                item.setValue(item.getValue() - 1);
                            }
                            return;
                        } else {
                            if (pipe.currentItems.size() + 1 < pipe.ITEM_CAP) {
                                pipe.addItemForTransport(item.getKey());
                                storedItems.remove(item.getKey());
                            }
                            return;
                        }
                    }
                }
            }
        }

        private void offloadToCrate(Direction dir) {
            if(storedItems.size() <= 0) return;
            int xx = 0; int yy = 0;
            switch(dir) {
                case RIGHT: xx = x + 1; yy = World.h - y - 1; break;
                case LEFT: xx = x - 1; yy = World.h - y - 1; break;
                case UP: yy = World.h - (y - 1) - 1; xx = x; break;
                case DOWN: yy = World.h - (y + 1) - 1; xx = x; break;
            }

            try {
                Crate crate = null;
                for (Crate crt : World.getEntityHandler().getPlayer().crates)
                    if (crt.x == xx && crt.y == yy) crate = crt;

                for (Map.Entry<Item, Integer> item : storedItems.entrySet()) {
                    if (item.getValue() - 1 < 0) {
                        assert crate != null;
                        crate.addItem(item.getKey());
                        item.setValue(item.getValue() - 1);
                        return;
                    } else {
                        assert crate != null;
                        crate.addItem(item.getKey());
                        storedItems.remove(item.getKey());
                        return;
                    }
                }
            } catch (NullPointerException ignored) {}
        }

        private void offloadToNode(Direction dir) {
            int xx = 0; int yy = 0;
            switch(dir) {
                case RIGHT: xx = x + 1; yy = y; break;
                case LEFT: xx = x - 1; yy = y; break;
                case UP: yy = y - 1; xx = x; break;
                case DOWN: yy = y + 1; xx = x; break;
            }

            try {
                Node nde = null;
                for (MachineType.Node node : MachineHandler.nodes)
                    if (node.x == xx && node.y == yy) nde = node;

                for (Map.Entry<Item, Integer> item : storedItems.entrySet()) {
                    if (item.getValue() - 1 < 0) {
                        if (nde.storedItems.size() + 1 <= nde.STORAGE_CAP) {
                            nde.addToStorage(item.getKey());
                            item.setValue(item.getValue() - 1);
                        }
                    } else {
                        if (nde.storedItems.size() + 1 <= nde.STORAGE_CAP) {
                            nde.addToStorage(item.getKey());
                            storedItems.remove(item.getKey());
                        }
                    }
                    return;
                }
            } catch (NullPointerException ignored) {}
        }


        private Tile checkUp() {
            if(World.getTile(x, World.h - (y - 1) - 1) != Assets.airTile) {
                return World.getTile(x, World.h - (y - 1) - 1);
            }
            return Assets.airTile;
        }
        private Tile checkDown() {
            if(World.getTile(x, World.h - (y + 1) - 1) != Assets.airTile) {
                return World.getTile(x, World.h - (y + 1) - 1);
            }
            return Assets.airTile;
        }
        private Tile checkRight() {
            if(World.getTile(x + 1, World.h - y - 1) != Assets.airTile) {
                // System.out.println("Returning: " + Item.getItemByID(World.getTile(x + 1, World.h - y - 1).getItemID()).getName());
                return World.getTile(x + 1, World.h - y - 1);
            }
            return Assets.airTile;
        }
        private Tile checkLeft() {
            if(World.getTile(x - 1, World.h - y - 1) != Assets.airTile) {
                return World.getTile(x - 1, World.h - y - 1);
            }
            return Assets.airTile;
        }

        public void render(Batch b, int x, int y) {
            // b.draw(Assets.errorSelector, (x + 1) * Tile.TILE_SIZE, y * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
            b.draw(Assets.node[0], x * Tile.TILE_SIZE, y * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
        }
    }

    // Assembler
    public static class Assembler {

        public int x, y;
        public ItemRecipe targetRecipe = null;
        public HashMap<Direction, Integer> io = new HashMap<>(); // 0 is input, 1 output, -1 neither, 2 for crate, 3 is other node.
        public ArrayList<Direction> outputs = new ArrayList<>();
        public HashMap<Item, Integer> storedItems = new HashMap<>();
        public int readyItems = 0;
        public long timer = 0, offloadMax = 20;
        public final int STORAGE_CAP = 1;

        public Assembler(int x, int y) {
            this.x = x; this.y = y;
            targetRecipe = null;
            io.put(Direction.RIGHT, -1);
            io.put(Direction.DOWN, -1);
            io.put(Direction.LEFT, -1);
            io.put(Direction.UP, -1);
        }

        public Assembler(int x, int y, Item item) {
            this.x = x; this.y = y;
            if (item.getItemType() instanceof ItemType.CreatableItem) {
                targetRecipe = ((ItemType.CreatableItem)
                        (item.getItemType())).getRecipe();
            } else if(item.getItemType() instanceof ItemType.PlaceableBlock.CreatableTile) {
                targetRecipe = ((ItemType.PlaceableBlock.CreatableTile)
                        (item.getItemType())).getRecipe();
            }
            io.put(Direction.RIGHT, -1);
            io.put(Direction.DOWN, -1);
            io.put(Direction.LEFT, -1);
            io.put(Direction.UP, -1);
        }

        short tir = 0, tickBuffer = 50;

        public void tick() {

            if(tir < tickBuffer) {
                tir++; return;
            }

            deferIO();

            if(pointerOnTileX() == x && pointerOnTileY() == World.h - y - 1) {
                if(InputHandler.rightMouseButtonDown) {
                    try {
                        if (ToolSlot.currentItem.getItem() != null) {
                            if (ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.CreatableItem) {
                                targetRecipe = ((ItemType.CreatableItem)
                                        (ToolSlot.currentItem.getItem().getItemType())).getRecipe();
                                readyItems = 0;
                            } else if(ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.PlaceableBlock.CreatableTile) {
                                targetRecipe = ((ItemType.PlaceableBlock.CreatableTile)
                                        (ToolSlot.currentItem.getItem().getItemType())).getRecipe();
                                readyItems = 0;
                            } else if(ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.Resource) {
                                targetRecipe = ((ItemType.Resource)
                                        (ToolSlot.currentItem.getItem().getItemType())).getItemRecipe();
                                readyItems = 0;
                            }
                        }
                    } catch (NullPointerException ignored) {}
                }
            }

            if(storedItems.size() > 0) {
                timer++;
                if (timer > offloadMax) {
                    assemble();
                    if(readyItems - 1 >= 0) deferOffloadDirection(targetRecipe.item);
                    timer = 0;
                }
            }
        }

        public void addToStorage(Item item) {
            if(!checkInRecipe(item)) {
                World.getItemHandler().addItem(item.createNew(x * Tile.TILE_SIZE + 4,
                        ((World.h - y - 1) * Tile.TILE_SIZE) - 2));
            } else {
                if(storedItems.containsKey(item)) storedItems.put(item, storedItems.get(item) + 1);
                else storedItems.put(item, 1);
            }
        }

        private boolean checkInRecipe(Item item) {
            try {
                for (Map.Entry<Item, String> recipe : targetRecipe.getRecipe().entrySet())
                    if (recipe.getKey() == item) return true;
            } catch (NullPointerException ignored) {}
            return false;
        }

        private void assemble() {

            int entry = 0;
            for(Map.Entry<Item, String> recipe : targetRecipe.getRecipe().entrySet()) {
                for(Map.Entry<Item, Integer> storedItems : storedItems.entrySet()) {
                    if(storedItems.getKey() == recipe.getKey()) {
                        if(storedItems.getValue() >= Utils.parseInt(recipe.getValue())) {
                            storedItems.setValue(storedItems.getValue() - Utils.parseInt(recipe.getValue()));
                            entry++;
                        }
                    }
                }
            }

            if(entry == targetRecipe.getRecipe().entrySet().size()) readyItems++;

        }

        // This is only called when an item is completed
        private ArrayList<Direction> usedOutputs = new ArrayList<>();
        private void deferOffloadDirection(Item item) {
            if(usedOutputs.containsAll(outputs)) usedOutputs.clear();
            for(Map.Entry<Direction, Integer> node : io.entrySet()) {
                // If the output is a pipe
                if(node.getValue() == 1 && !usedOutputs.contains(node.getKey())) {
                    usedOutputs.add(node.getKey());
                    offload(node.getKey(), item);
                    break;
                }
            }
        }

        // This is only called when an item is completed
        private void offload(Direction dir, Item item) {
            int xx = 0; int yy = 0;
            switch(dir) {
                case RIGHT: xx = x + 1; yy = World.h - y - 1; break;
                case LEFT: xx = x - 1; yy = World.h - y - 1; break;
                case UP: yy = World.h - (y - 1) - 1; xx = x; break;
                case DOWN: yy = World.h - (y + 1) - 1; xx = x; break;
            }

            for(MachineType.Pipe pipe : MachineHandler.pipes) {
                if(pipe.x == xx && pipe.y == yy) {
                    if (pipe.currentItems.size() + 1 < pipe.ITEM_CAP) {
                        pipe.addItemForTransport(item);
                        readyItems--;
                        return;
                    }
                }
            }
        }

        // Helper methods
        private Tile checkUp() {
            if(World.getTile(x, World.h - (y - 1) - 1) != Assets.airTile) {
                return World.getTile(x, World.h - (y - 1) - 1);
            }
            return Assets.airTile;
        }
        private Tile checkDown() {
            if(World.getTile(x, World.h - (y + 1) - 1) != Assets.airTile) {
                return World.getTile(x, World.h - (y + 1) - 1);
            }
            return Assets.airTile;
        }
        private Tile checkRight() {
            if(World.getTile(x + 1, World.h - y - 1) != Assets.airTile) {
                // System.out.println("Returning: " + Item.getItemByID(World.getTile(x + 1, World.h - y - 1).getItemID()).getName());
                return World.getTile(x + 1, World.h - y - 1);
            }
            return Assets.airTile;
        }
        private Tile checkLeft() {
            if(World.getTile(x - 1, World.h - y - 1) != Assets.airTile) {
                return World.getTile(x - 1, World.h - y - 1);
            }
            return Assets.airTile;
        }
        private int pointerOnTileX() {
            return (int) ((((InputHandler.cursorX) - Camera.position.x) /
                    Tile.TILE_SIZE) / Camera.scale.x);
        }
        private int pointerOnTileY() {
            return (int) ((((InputHandler.cursorY) - Camera.position.y) /
                    Tile.TILE_SIZE) / Camera.scale.y);
        }
        private void updateOutputList() {
            outputs.clear();
            for(Map.Entry<Direction, Integer> node : io.entrySet()) {
                if(node.getValue() == 1) {
                    outputs.add(node.getKey());
                } else if (node.getValue() == 2) {
                    outputs.add(node.getKey());
                } else if (node.getValue() == 3) {
                    outputs.add(node.getKey());
                }
            }
        }
        private void deferIO() {
            // Up
            if(checkUp() == Assets.stonePipeTile) {
                for(MachineType.Pipe pipe : MachineHandler.pipes) {
                    if(pipe.x == x && pipe.y == World.h - (y - 1) - 1) {
                        if(pipe.direction == Direction.UP) io.put(Direction.UP, 1);
                        else if(pipe.direction == Direction.DOWN) io.put(Direction.UP, 0);
                        else io.put(Direction.UP, -1);
                        break;
                    }
                }
            } else if (checkUp() == Assets.crateTile) {
                io.put(Direction.UP, 2);
            } else if (checkUp() == Assets.nodeTile) {
                io.put(Direction.UP, 3);
            }

            // Down
            if(checkDown() == Assets.stonePipeTile) {
                for(MachineType.Pipe pipe : MachineHandler.pipes) {
                    if(pipe.x == x && pipe.y == World.h - (y + 1) - 1) {
                        if(pipe.direction == Direction.DOWN) io.put(Direction.DOWN, 1);
                        else if(pipe.direction == Direction.UP) io.put(Direction.DOWN, 0);
                        else io.put(Direction.DOWN, -1);
                        break;
                    }
                }
            } else if (checkDown() == Assets.crateTile) {
                io.put(Direction.DOWN, 2);
            } else if (checkDown() == Assets.nodeTile) {
                io.put(Direction.DOWN, 3);
            }

            // Right
            if(checkRight() == Assets.stonePipeTile) {
                for(MachineType.Pipe pipe : MachineHandler.pipes) {
                    if(pipe.x == x + 1 && pipe.y == World.h - y - 1) {
                        if(pipe.direction == Direction.RIGHT) io.put(Direction.RIGHT, 1);
                        else if(pipe.direction == Direction.LEFT) io.put(Direction.RIGHT, 0);
                        else io.put(Direction.RIGHT, -1);
                        break;
                    }
                }
            } else if (checkRight() == Assets.crateTile) {
                io.put(Direction.RIGHT, 2);
            } else if (checkRight() == Assets.nodeTile) {
                io.put(Direction.RIGHT, 3);
            }


            // Left
            if(checkLeft() == Assets.stonePipeTile) {
                for(MachineType.Pipe pipe : MachineHandler.pipes) {
                    if(pipe.x == x - 1 && pipe.y == World.h - y - 1) {
                        if(pipe.direction == Direction.LEFT) io.put(Direction.LEFT, 1);
                        else if(pipe.direction == Direction.RIGHT) io.put(Direction.LEFT, 0);
                        else io.put(Direction.LEFT, -1);
                        break;
                    }
                }
            } else if (checkLeft() == Assets.crateTile) {
                io.put(Direction.LEFT, 2);
            } else if (checkLeft() == Assets.nodeTile) {
                io.put(Direction.LEFT, 3);
            }

            updateOutputList();
        }

        public void render(Batch b, int x, int y) {
            b.draw(Assets.assembler, x * Tile.TILE_SIZE, y * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
            if(targetRecipe != null) {
                b.draw(targetRecipe.item.getTexture(), x * Tile.TILE_SIZE + 4, y * Tile.TILE_SIZE + 4, 8, 8);
            }
        }
    }
}
