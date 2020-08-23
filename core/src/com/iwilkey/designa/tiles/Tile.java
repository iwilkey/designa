package com.iwilkey.designa.tiles;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Animation;
import com.iwilkey.designa.world.AmbientCycle;

public class Tile {

    // Statics

    // Tile types

    // Natural Tiles
    public static class AirTile extends Tile {
        public AirTile(int ID, int strength) { super(Assets.air, ID, strength); }
        @Override
        public boolean isSolid() {
            return false;
        }
    }
    public static class DirtTile extends Tile {
        // What item ID will it create?
        private int itemID = 0;
        public DirtTile(int ID, int strength) {
            super(Assets.dirt, ID, strength);
        }
        @Override
        public int getItemID() { return itemID; }
    }
    public static class GrassTile extends Tile {
        public int itemID = 0;
        public GrassTile(int ID, int strength) {
            super(Assets.grass, ID, strength);
        }
        @Override
        public int getItemID() { return itemID; }
    }
    public static class StoneTile extends Tile {
        private int itemID = 1;
        public StoneTile(int ID, int strength) {
            super(Assets.stone, ID, strength);
        }
        @Override
        public int getItemID() { return itemID; }
    }

    // Ores
    public static class CopperOreTile extends Tile {
        private int itemID = 114;
        public CopperOreTile(int ID, int strength) {
            super(Assets.copperOre, ID, strength);
        }
        public int getItemID() { return itemID; }
    }
    public static class SilverOreTile extends Tile {
        private int itemID = 119;
        public SilverOreTile(int ID, int strength) {
            super(Assets.silverOre, ID, strength);
        }
        public int getItemID() { return itemID; }
    }
    public static class IronOreTile extends Tile {
        private int itemID = 126;

        public IronOreTile(int ID, int strength) {
            super(Assets.ironOre, ID, strength);
        }

        public int getItemID() {
            return itemID;
        }
    }

    // Animated Tiles
    public static class TorchTile extends Tile {
        private int itemID = 20;
        private final Animation animation;
        public TorchTile(int ID, int strength) {
            super(Assets.torch[1], ID, strength);
            animation = new Animation(75, Assets.torch);
        }
        @Override
        public int getItemID() {
            return itemID;
        }
        @Override
        public void tick() {
            animation.tick();
            texture = animation.getCurrentFrame();
        }
        @Override
        public boolean isSolid() {
            return false;
        }
    }

    // Construction bricks
    public static class PlywoodTile extends Tile {
        public int itemID = 2;
        public PlywoodTile(int ID, int strength) {
            super(Assets.plywood, ID, strength);
        }
        public int getItemID() { return itemID; }
    }
    public static class HardwoodTile extends Tile {
        public int itemID = 3;
        public HardwoodTile(int ID, int strength) {
            super(Assets.hardwood, ID, strength);
        }
        public int getItemID() { return itemID; }
    }
    public static class ReinforcedHardwoodTile extends Tile {
        public int itemID = 4;
        public ReinforcedHardwoodTile(int ID, int strength) {
            super(Assets.reinforcedHardwood, ID, strength);
        }
        public int getItemID() { return itemID; }
    }
    public static class StrongwoodTile extends Tile {
        public int itemID = 5;
        public StrongwoodTile(int ID, int strength) {
            super(Assets.strongwood, ID, strength);
        }
        public int getItemID() { return itemID; }
    }
    public static class ReinforcedStrongwoodTile extends Tile {
        public int itemID = 6;
        public ReinforcedStrongwoodTile(int ID, int strength) {
            super(Assets.reinforcedStrongwood, ID, strength);
        }
        public int getItemID() { return itemID; }
    }

    public static Tile[] tiles = new Tile[256];
    public static final int TILE_SIZE = 16;

    // Instances (May need to be moved to Assets)
    // Natural
    public static Tile airTile = new AirTile(0, 0);
    public static Tile grassTile = new GrassTile(1, 4);
    public static Tile dirtTile = new DirtTile(2, 3);
    public static Tile stoneTile = new StoneTile(3, 16);

    // Animated
    public static Tile torchTile = new TorchTile(4, 1);

    // Ores
    public static Tile copperOreTile = new CopperOreTile(5, 20);
    public static Tile silverOreTile = new SilverOreTile(6, 20);
    public static Tile ironOreTile = new IronOreTile(7, 20);

    // Construction
    public static Tile plywoodTile = new PlywoodTile(8, 5);
    public static Tile hardwoodTile = new HardwoodTile(9, 10);
    public static Tile reinforcedHardwoodTile = new ReinforcedHardwoodTile(10, 15);
    public static Tile strongwoodTile = new StrongwoodTile(11, 20);
    public static Tile reinforcedStrongwoodTile = new ReinforcedStrongwoodTile(12, 25);

    public static int getStrength(int id) {
        return tiles[id].getStrength();
    }

    // Class
    protected TextureRegion texture;
    protected final int ID;
    protected int strength;

    public Tile(TextureRegion tex, int ID, int strength) {
        this.texture = tex;
        this.ID = ID;
        this.strength = strength;

        tiles[ID] = this;
    }

    public void tick() {
        torchTile.tick();
    }

    public void render(Batch b, int x, int y, int bl, int id) { // This will render a tile at the x and y of it the world has set
        b.draw(texture, x, y, TILE_SIZE, TILE_SIZE);
        renderBreakLevel(b, x, y, bl, id);
    }

    public void renderBackTile(Batch b, int x, int y, int bl, int id) {
        if(!(this instanceof AirTile)) {
            b.draw(texture, x, y, Tile.TILE_SIZE, Tile.TILE_SIZE);
            renderBreakLevel(b, x, y, bl, id);
            b.draw(Assets.light_colors[3], x, y, Tile.TILE_SIZE, Tile.TILE_SIZE);
        }
    }

    public void renderAmbientLight(Batch b, int x, int y) {
        if(this instanceof AirTile) {
            if(AmbientCycle.percentOfDay <= 100 && AmbientCycle.percentOfDay > 80) b.draw(Assets.light_colors[6], x, y, Tile.TILE_SIZE, Tile.TILE_SIZE);
            if(AmbientCycle.percentOfDay <= 80 && AmbientCycle.percentOfDay > 60) b.draw(Assets.light_colors[6], x, y, Tile.TILE_SIZE, Tile.TILE_SIZE);
            if(AmbientCycle.percentOfDay <= 60 && AmbientCycle.percentOfDay > 40) b.draw(Assets.light_colors[5], x, y, Tile.TILE_SIZE, Tile.TILE_SIZE);
            if(AmbientCycle.percentOfDay <= 40 && AmbientCycle.percentOfDay > 20) b.draw(Assets.light_colors[4], x, y, Tile.TILE_SIZE, Tile.TILE_SIZE);
            if(AmbientCycle.percentOfDay <= 20 && AmbientCycle.percentOfDay >= 0) b.draw(Assets.light_colors[3], x, y, Tile.TILE_SIZE, Tile.TILE_SIZE);
        }
    }

    private void renderBreakLevel(Batch b, int x, int y, int bl, int id) {
        float pd;
        if(getStrength(id) > 0)
            pd = Math.abs((float)(getStrength(id) - bl) / bl) * 100;
        else pd = 0;

        if(pd >= 0 && pd <= 20) {
            b.draw(Assets.breakLevel[0], x, y, Tile.TILE_SIZE, Tile.TILE_SIZE);
        } else if (pd > 20 && pd <= 40) {
            b.draw(Assets.breakLevel[1], x, y, Tile.TILE_SIZE, Tile.TILE_SIZE);
        } else if (pd > 40 && pd <= 60) {
            b.draw(Assets.breakLevel[2], x, y, Tile.TILE_SIZE, Tile.TILE_SIZE);
        } else if (pd > 60 && pd <= 80) {
            b.draw(Assets.breakLevel[3], x, y, Tile.TILE_SIZE, Tile.TILE_SIZE);
        } else {
            b.draw(Assets.breakLevel[4], x, y, Tile.TILE_SIZE, Tile.TILE_SIZE);
        }
    }

    public boolean isSolid() {
        return true;
    }
    public int getID() {
        return ID;
    }
    public int getStrength() { return strength; }
    public int getItemID() { return 0; }

}
