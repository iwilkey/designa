package com.iwilkey.designa.machines;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.assets.Assets;
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
        public Offloader(int x, int y, Direction direction) {
            this.x = x; this.y = y;
            this.direction = direction;
        }
        public void setDirection(Direction direction) {
            this.direction = direction;
        }
        public void tick() {

        }
        public static void render(Batch b, int x, int y, int bl, int id) {
            b.draw(Assets.offloader[0], x, y, Tile.TILE_SIZE, Tile.TILE_SIZE);
        }
    }
}
