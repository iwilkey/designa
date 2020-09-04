package com.iwilkey.designa.machines;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.gui.Hud;
import com.iwilkey.designa.tiles.Tile;

public class MachineType {

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

    // Offloader
    public static class Offloader {
        public enum Direction { RIGHT, DOWN, LEFT, UP }
        public Direction direction;
        public int x, y;
        public Offloader(int x, int y) {
            this.x = x; this.y = y;
            direction = Direction.RIGHT;
            switch(Hud.SELECTED_PIPE_DIRECTION) {
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

        public void tick() {}

        public static void render(Batch b, int x, int y, int bl, int id) {
            /*
            switch(direction) {
                case RIGHT:
                    b.draw(Assets.offloader[0], x, y, Tile.TILE_SIZE, Tile.TILE_SIZE);
                    break;
                case DOWN:
                    b.draw(Assets.offloader[1], x, y, Tile.TILE_SIZE, Tile.TILE_SIZE);
                    break;
                case LEFT:
                    b.draw(Assets.offloader[2], x, y, Tile.TILE_SIZE, Tile.TILE_SIZE);
                    break;
                case UP:
                    b.draw(Assets.offloader[3], x, y, Tile.TILE_SIZE, Tile.TILE_SIZE);
                    break;
            }

             */
        }
    }
}
