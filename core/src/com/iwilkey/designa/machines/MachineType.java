package com.iwilkey.designa.machines;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.inventory.crate.Crate;
import com.iwilkey.designa.items.Item;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.world.World;

import java.util.ArrayList;

public class MachineType {

    public enum Direction { RIGHT, DOWN, LEFT, UP }

    // Pipe
    public static class Pipe {

        public int x, y;
        public Direction direction = null;
        public Tile source = null; // What is this pipe getting objects from?
        public Tile destination = null; // Where is this pipe going to?
        public ArrayList<Item> currentItems = new ArrayList<>(); // What items are this pipe transporting?
        public ArrayList<Float> percentItemTraveled = new ArrayList<>(); // Where is each item in the pipe?
        public final int ITEM_CAP = 4;
        public final float CONVEY_SPEED = 0.5f;

        public Pipe(int x, int y, int direction) {
            this.x = x; this.y = y;
            switch(direction) {
                case 0:
                    this.direction = Direction.RIGHT;
                    break;
                case 1:
                    this.direction = Direction.DOWN;
                    break;
                case 2:
                    this.direction = Direction.LEFT;
                    break;
                case 3:
                    this.direction = Direction.UP;
                    break;
            }
        }

        long timer = 0, newItem = 100;

        public void tick() {

            if(currentItems.size() != 0) {
                for(int i = 0; i < percentItemTraveled.size(); i++) {

                    if(percentItemTraveled.get(i) >= 100) {
                        percentItemTraveled.set(i, 100.0f);
                        break;
                    }

                    percentItemTraveled.set(i, percentItemTraveled.get(i) + CONVEY_SPEED);
                }
            }

            switch(direction) {
                case RIGHT:
                    source = checkLeft();
                    destination = checkRight();
                    break;
                case LEFT:
                    source = checkRight();
                    destination = checkLeft();
                    break;
                case UP:
                    source = checkDown();
                    destination = checkUp();
                    break;
                case DOWN:
                    source = checkUp();
                    destination = checkDown();
                    break;
            }

            timer++;
            if(timer > newItem) {
                // If the source is a drill...
                if (source == Tile.copperMechanicalDrillTile) {
                    if (currentItems.size() + 1 < ITEM_CAP) {
                        addItemForTransport(Assets.copperScrapResource);
                    }
                }
                timer = 0;
            }

            if(source == Tile.stonePipeTile) {
                switch(direction) {
                    case RIGHT:
                        if(returnCompletedItem(x - 1, y) != null) {
                            if (currentItems.size() + 1 < ITEM_CAP) {
                                for(MachineType.Pipe pipe : MachineHandler.pipes) {
                                    if (pipe.x == x - 1 && pipe.y == y)
                                        for(int i = 0; i < pipe.currentItems.size(); i++) {
                                            pipe.percentItemTraveled.remove(i);
                                            pipe.currentItems.remove(i);
                                        }
                                }
                                addItemForTransport(Assets.copperScrapResource);
                            }
                        }
                        break;
                    case LEFT:
                        if(returnCompletedItem(x + 1, y) != null) {
                            if (currentItems.size() + 1 < ITEM_CAP) {
                                for(MachineType.Pipe pipe : MachineHandler.pipes) {
                                    if (pipe.x == x + 1 && pipe.y == y)
                                        for(int i = 0; i < pipe.currentItems.size(); i++) {
                                            pipe.percentItemTraveled.remove(i);
                                            pipe.currentItems.remove(i);
                                        }
                                }
                                addItemForTransport(Assets.copperScrapResource);
                            }
                        }
                        break;
                    case UP:
                        if(returnCompletedItem(x, y - 1) != null) {
                            if (currentItems.size() + 1 < ITEM_CAP) {
                                for(MachineType.Pipe pipe : MachineHandler.pipes) {
                                    if (pipe.x == x && pipe.y == y - 1)
                                        for(int i = 0; i < pipe.currentItems.size(); i++) {
                                            pipe.percentItemTraveled.remove(i);
                                            pipe.currentItems.remove(i);
                                        }
                                }
                                addItemForTransport(Assets.copperScrapResource);
                            }
                        }
                        break;
                    case DOWN:
                        if(returnCompletedItem(x, y + 1) != null) {
                            if (currentItems.size() + 1 < ITEM_CAP) {
                                for(MachineType.Pipe pipe : MachineHandler.pipes) {
                                    if (pipe.x == x && pipe.y == y + 1)
                                        for(int i = 0; i < pipe.currentItems.size(); i++) {
                                            pipe.percentItemTraveled.remove(i);
                                            pipe.currentItems.remove(i);
                                        }
                                }
                                addItemForTransport(Assets.copperScrapResource);
                            }
                        }
                        break;
                }
            }

            if(destination == Tile.nodeTile) {
                for(int i = 0; i < percentItemTraveled.size(); i++) {
                    if(percentItemTraveled.get(i) >= 100.0f) {
                        switch(direction) {
                            case RIGHT:
                                for(Node node : MachineHandler.nodes) {
                                    if(node.x == x + 1 && node.y == y) {
                                        node.sendToDestination(currentItems.get(i));
                                        currentItems.remove(i);
                                        percentItemTraveled.remove(i);
                                    }
                                }
                                break;
                            case LEFT:
                                for(Node node : MachineHandler.nodes) {
                                    if(node.x == x - 1 && node.y == y) {
                                        node.sendToDestination(currentItems.get(i));
                                        currentItems.remove(i);
                                        percentItemTraveled.remove(i);
                                    }
                                }
                                break;
                        }
                    }
                }
            }


            if(destination == Tile.crateTile) {
                for(int i = 0; i < percentItemTraveled.size(); i++) {
                    if(percentItemTraveled.get(i) >= 100.0f) {
                        switch(direction) {
                            case RIGHT:
                                offloadToCrate(currentItems.get(i), x + 1, y);
                                percentItemTraveled.remove(i);
                                currentItems.remove(i);
                                break;
                            case LEFT:
                                offloadToCrate(currentItems.get(i), x - 1, y);
                                percentItemTraveled.remove(i);
                                currentItems.remove(i);
                                break;
                            case UP:
                                offloadToCrate(currentItems.get(i), x, y + 1);
                                percentItemTraveled.remove(i);
                                currentItems.remove(i);
                                break;
                            case DOWN:
                                offloadToCrate(currentItems.get(i), x, y - 1);
                                percentItemTraveled.remove(i);
                                currentItems.remove(i);
                                break;
                        }
                    }
                }
            }
        }

        private void addItemForTransport(Item item) {
            currentItems.add(item);
            percentItemTraveled.add(0.0f);
        }

        private void offloadToCrate(Item item, int x, int y) {
            for(Crate crate : World.getEntityHandler().getPlayer().crates) {
                if(crate.x == x && crate.y == y) crate.addItem(item);
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
                case RIGHT:
                    // Right
                    b.draw(Tile.Pipe.StonePipe.animations[0].getCurrentFrame(), x * Tile.TILE_SIZE, y * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
                    break;
                case LEFT:
                    // Left
                    b.draw(Tile.Pipe.StonePipe.animations[2].getCurrentFrame(), x * Tile.TILE_SIZE, y * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
                    break;
                case DOWN:
                    // Down
                    b.draw(Tile.Pipe.StonePipe.animations[1].getCurrentFrame(), x * Tile.TILE_SIZE, y * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
                    break;
                case UP:
                    // Up
                    b.draw(Tile.Pipe.StonePipe.animations[3].getCurrentFrame(), x * Tile.TILE_SIZE, y * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
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
    }

    // Node
    public static class Node {
        public Direction direction;
        public int x, y;
        public Tile source = null;
        public Tile destination = null;
        public Node(int x, int y, int dir) {
            this.x = x; this.y = y;
            switch(dir) {
                case 0:
                    direction = Direction.RIGHT;
                    break;
                case 1:
                    direction = Direction.DOWN;
                    break;
                case 2:
                    direction = Direction.LEFT;
                    break;
                case 3:
                    direction = Direction.UP;
                    break;
            }
        }

        public void setDirection(Direction direction) {
            this.direction = direction;
        }

        public void tick() {

            switch(direction) {
                case RIGHT:
                    source = checkDown();
                    destination = checkRight();
                    break;
                case LEFT:
                    source = checkUp();
                    destination = checkLeft();
                    break;
                case UP:
                    source = checkRight();
                    destination = checkUp();
                    break;
                case DOWN:
                    source = checkLeft();
                    destination = checkDown();
                    break;
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

        public void sendToDestination(Item item) {
            // This only cares about pipes for now.
            switch(direction) {
                case RIGHT:
                    for(Pipe pipe : MachineHandler.pipes) {
                        if(pipe.x == x + 1 && pipe.y == y) {
                            pipe.addItemForTransport(item);
                            break;
                        }
                    }
                    break;
                case LEFT:
                    for(Pipe pipe : MachineHandler.pipes) {
                        if(pipe.x == x - 1 && pipe.y == y) {
                            pipe.addItemForTransport(item);
                            break;
                        }
                    }
                    break;
                case UP:
                    for(Pipe pipe : MachineHandler.pipes) {
                        if(pipe.x == x && pipe.y == y + 1) {
                            pipe.addItemForTransport(item);
                            break;
                        }
                    }
                    break;
                case DOWN:
                    for(Pipe pipe : MachineHandler.pipes) {
                        if(pipe.x == x && pipe.y == y - 1) {
                            pipe.addItemForTransport(item);
                            break;
                        }
                    }
                    break;
            }
        }

        public void render(Batch b, int x, int y, Direction direction) {
            switch(direction) {
                case RIGHT:
                    b.draw(Assets.node[0], x * Tile.TILE_SIZE, y * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
                    break;
                case DOWN:
                    b.draw(Assets.node[1], x * Tile.TILE_SIZE, y * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
                    break;
                case LEFT:
                    b.draw(Assets.node[2], x * Tile.TILE_SIZE, y * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
                    break;
                case UP:
                    b.draw(Assets.node[3], x * Tile.TILE_SIZE, y * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
                    break;
            }
        }
    }
}
