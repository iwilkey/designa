package com.iwilkey.designa.machines;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.inventory.InventorySlot;
import com.iwilkey.designa.inventory.crate.Crate;
import com.iwilkey.designa.items.Item;
import com.iwilkey.designa.tiles.Tile;
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
        public final int ITEM_CAP = 2; // How many items can be in a pipe at a time?
        public float CONVEY_SPEED = 0.5f; // How fast do the items move?
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

            if(currentItems.size() != 0) {
                for(int i = 0; i < percentItemTraveled.size(); i++) {
                    if(percentItemTraveled.get(i) >= 100) {
                        percentItemTraveled.set(i, 100.0f); break;
                    }
                    percentItemTraveled.set(i, percentItemTraveled.get(i) + CONVEY_SPEED);
                }
            }

            switch(direction) {
                case RIGHT: source = checkLeft(); destination = checkRight(); break;
                case LEFT: source = checkRight(); destination = checkLeft(); break;
                case UP: source = checkDown(); destination = checkUp(); break;
                case DOWN: source = checkUp(); destination = checkDown(); break;
            }

            timer++;
            if(timer > newItem) {
                // If the source is a drill...
                if (source == Tile.copperMechanicalDrillTile) {
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
                if(source == Tile.crateTile) unloadFromCrate();
                timer = 0;
            }

            if(source == Tile.stonePipeTile) offloadToPipe();
            if(destination == Tile.crateTile) offloadToCrate();
            if(destination == Tile.nodeTile) offloadToNode();

        }

        private void addItemForTransport(Item item) {
            currentItems.add(item); percentItemTraveled.add(0.0f);
        }

        private void removeItemFromTransport(int index) {
            currentItems.remove(index); percentItemTraveled.remove(index);
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
            if(World.getTile(x, y + 1) != Tile.airTile) return World.getTile(x, y + 1);
            return Tile.airTile;
        }
        private Tile checkDown() {
            if(World.getTile(x, y - 1) != Tile.airTile) return World.getTile(x, y - 1);
            return Tile.airTile;
        }
        private Tile checkRight() {
            if(World.getTile(x + 1, y) != Tile.airTile) return World.getTile(x + 1, y);
            return Tile.airTile;
        }
        private Tile checkLeft() {
            if(World.getTile(x - 1, y) != Tile.airTile) return World.getTile(x - 1, y);
            return Tile.airTile;
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
                                xx = (int)(((x * Tile.TILE_SIZE) - 8) + (Tile.TILE_SIZE * (percentDone / 100.0f)));
                                break;
                            case LEFT:
                                yy = y * Tile.TILE_SIZE + 4;
                                xx = (int) ((((x * Tile.TILE_SIZE) + Tile.TILE_SIZE)) - (Tile.TILE_SIZE * (percentDone / 100.0f)));
                                break;
                            case UP:
                                xx = x * Tile.TILE_SIZE + 4;
                                yy = (int)(((y * Tile.TILE_SIZE) - 8) + (Tile.TILE_SIZE * (percentDone / 100.0f)));
                                break;
                            case DOWN:
                                xx = x * Tile.TILE_SIZE + 4;
                                yy = (int) ((((y * Tile.TILE_SIZE) + Tile.TILE_SIZE)) - (Tile.TILE_SIZE * (percentDone / 100.0f)));
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
        public HashMap<Direction, Integer> io = new HashMap<>(); // 0 is input, 1 output, -1 neither, 2 for crate
        public ArrayList<Direction> outputs = new ArrayList<>();
        public HashMap<Item, Integer> storedItems = new HashMap<>();
        public long timer = 0, offloadMax = 20;
        public final int STORAGE_CAP = 3;

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
            if(checkUp() == Tile.stonePipeTile) {
                for(MachineType.Pipe pipe : MachineHandler.pipes) {
                    if(pipe.x == x && pipe.y == World.h - (y - 1) - 1) {
                        if(pipe.direction == Direction.UP) io.put(Direction.UP, 1);
                        else if(pipe.direction == Direction.DOWN) io.put(Direction.UP, 0);
                        else io.put(Direction.UP, -1);
                        break;
                    }
                }
            } else if (checkUp() == Tile.crateTile) {
                io.put(Direction.UP, 2);
            }

            // Down
            if(checkDown() == Tile.stonePipeTile) {
                for(MachineType.Pipe pipe : MachineHandler.pipes) {
                    if(pipe.x == x && pipe.y == World.h - (y + 1) - 1) {
                        if(pipe.direction == Direction.DOWN) io.put(Direction.DOWN, 1);
                        else if(pipe.direction == Direction.UP) io.put(Direction.DOWN, 0);
                        else io.put(Direction.DOWN, -1);
                        break;
                    }
                }
            } else if (checkDown() == Tile.crateTile) {
                io.put(Direction.DOWN, 2);
            }

            // Right
            if(checkRight() == Tile.stonePipeTile) {
                for(MachineType.Pipe pipe : MachineHandler.pipes) {
                    if(pipe.x == x + 1 && pipe.y == World.h - y - 1) {
                        if(pipe.direction == Direction.RIGHT) io.put(Direction.RIGHT, 1);
                        else if(pipe.direction == Direction.LEFT) io.put(Direction.RIGHT, 0);
                        else io.put(Direction.RIGHT, -1);
                        break;
                    }
                }
            } else if (checkRight() == Tile.crateTile) {
                io.put(Direction.RIGHT, 2);
            }


            // Left
            if(checkLeft() == Tile.stonePipeTile) {
                for(MachineType.Pipe pipe : MachineHandler.pipes) {
                    if(pipe.x == x - 1 && pipe.y == World.h - y - 1) {
                        if(pipe.direction == Direction.LEFT) io.put(Direction.LEFT, 1);
                        else if(pipe.direction == Direction.RIGHT) io.put(Direction.LEFT, 0);
                        else io.put(Direction.LEFT, -1);
                        break;
                    }
                }
            } else if (checkLeft() == Tile.crateTile) {
                io.put(Direction.LEFT, 2);
            }

            updateOutputList();
        }

        private void updateOutputList() {
            outputs.clear();
            for(Map.Entry<Direction, Integer> node : io.entrySet()) {
                if(node.getValue() == 1) {
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
                            pipe.addItemForTransport(item.getKey());
                            item.setValue(item.getValue() - 1);
                            return;
                        } else {
                            pipe.addItemForTransport(item.getKey());
                            storedItems.remove(item.getKey());
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

            Crate crate = null;
            for(Crate crt : World.getEntityHandler().getPlayer().crates)
                if(crt.x == xx && crt.y == yy) crate = crt;

            for(Map.Entry<Item, Integer> item : storedItems.entrySet()) {
                if(item.getValue() - 1 < 0) {
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
        }


        private Tile checkUp() {
            if(World.getTile(x, World.h - (y - 1) - 1) != Tile.airTile) {
                return World.getTile(x, World.h - (y - 1) - 1);
            }
            return Tile.airTile;
        }
        private Tile checkDown() {
            if(World.getTile(x, World.h - (y + 1) - 1) != Tile.airTile) {
                return World.getTile(x, World.h - (y + 1) - 1);
            }
            return Tile.airTile;
        }
        private Tile checkRight() {
            if(World.getTile(x + 1, World.h - y - 1) != Tile.airTile) {
                // System.out.println("Returning: " + Item.getItemByID(World.getTile(x + 1, World.h - y - 1).getItemID()).getName());
                return World.getTile(x + 1, World.h - y - 1);
            }
            return Tile.airTile;
        }
        private Tile checkLeft() {
            if(World.getTile(x - 1, World.h - y - 1) != Tile.airTile) {
                return World.getTile(x - 1, World.h - y - 1);
            }
            return Tile.airTile;
        }

        public void render(Batch b, int x, int y) {
            // b.draw(Assets.errorSelector, (x + 1) * Tile.TILE_SIZE, y * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
            b.draw(Assets.node[0], x * Tile.TILE_SIZE, y * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
        }
    }
}
