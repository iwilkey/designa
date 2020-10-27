package dev.iwilkey.designa.tile;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.iwilkey.designa.assets.Assets;

public enum Tile {

    AIR (0, Assets.air, new TileType.Natural(),0,false),
    DIRT (1, Assets.dirt, new TileType.Natural(), 4, true),
    GRASS (2, Assets.grass, new TileType.Natural(), 6,true);

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

    public void render(Batch b, int x, int y) {
        b.draw(texture, x, y, TILE_SIZE, TILE_SIZE);
    }

    public boolean isSolid() { return isSolid; }
    public TileType getType() { return type; }
    public int getTileID() { return tileID; }
    public int getStrength() { return strength; }

}
