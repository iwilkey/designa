package dev.iwilkey.designa.tile;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.iwilkey.designa.assets.Assets;
import dev.iwilkey.designa.item.Item;
import dev.iwilkey.designa.item.ItemType;

public enum Tile {

    AIR (0, Assets.air, new TileType.Natural(), 0,false),
    DIRT (1, Assets.dirt, new TileType.Natural(), 10, true),
    GRASS (2, Assets.grass, new TileType.Natural(), 12,true),
    STONE (3, Assets.stone, new TileType.Natural(), 30, true),
    COPPER_ORE (4, Assets.copperOre, new TileType.Natural.Ore(), 32, true),
    SILVER_ORE (5, Assets.silverOre, new TileType.Natural.Ore(), 38, true),
    IRON_ORE (6, Assets.ironOre, new TileType.Natural.Ore(), 42, true);

    public static final int TILE_SIZE = 16;
    private final int tileID;
    private final int strength;
    private TileType type;
    private TextureRegion texture;
    private boolean isSolid;

    Tile(int id, TextureRegion texture, TileType type, int strength, boolean isSolid) {
        this.tileID = id;
        this.texture = texture;
        this.type = type;
        this.strength = strength;
        this.isSolid = isSolid;
    }

    public void render(Batch b, int x, int y, int bl) {
        b.draw(texture, x, y, TILE_SIZE, TILE_SIZE);
        renderBreakLevel(b, x, y, bl);
    }

    float pd;
    public void renderBreakLevel(Batch b, int x, int y, int bl) {
        if(strength > 0)
            pd = Math.abs((float)(strength - bl) / bl) * 100;
        else pd = 0;

        if(pd >= 0 && pd <= 20) b.draw(Assets.breakLevel[0], x, y, Tile.TILE_SIZE, Tile.TILE_SIZE);
        else if (pd > 20 && pd <= 40) b.draw(Assets.breakLevel[1], x, y, Tile.TILE_SIZE, Tile.TILE_SIZE);
        else if (pd > 40 && pd <= 60) b.draw(Assets.breakLevel[2], x, y, Tile.TILE_SIZE, Tile.TILE_SIZE);
        else if (pd > 60 && pd <= 80) b.draw(Assets.breakLevel[3], x, y, Tile.TILE_SIZE, Tile.TILE_SIZE);
        else b.draw(Assets.breakLevel[4], x, y, Tile.TILE_SIZE, Tile.TILE_SIZE);
    }

    public void renderBack(Batch b, int x, int y) {
        b.draw(texture, x, y, TILE_SIZE, TILE_SIZE);
        b.draw(Assets.lightColors[3], x, y, TILE_SIZE, TILE_SIZE);
    }

    public static Item getItemFromTile(Tile t) {
        for(Item i : Item.values()) {
            if(i.getType() instanceof ItemType.NonCreatableItem.PlaceableTile) {
                if(((ItemType.NonCreatableItem.PlaceableTile)i.getType()).correspondingTile == t) return i;
            }
        }
        return null;
    }

    public static Tile getTileFromID(int ID) {
        for(Tile t : Tile.values())
            if(t.getTileID() == ID) return t;
        return null;
    }

    public boolean isSolid() { return isSolid; }
    public TileType getType() { return type; }
    public int getTileID() { return tileID; }
    public int getStrength() { return strength; }

}
