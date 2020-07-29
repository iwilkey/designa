package com.iwilkey.designa.tiles;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.iwilkey.designa.tiles.tiletypes.AirTile;
import com.iwilkey.designa.tiles.tiletypes.DirtTile;
import com.iwilkey.designa.tiles.tiletypes.GrassTile;

public class Tile {

    // Statics
    public static Tile[] tiles = new Tile[256];
    public static final int TILE_SIZE = 16;

    public static Tile airTile = new AirTile(0);
    public static Tile grassTile = new GrassTile(1);
    public static Tile dirtTile = new DirtTile(2);

    // Class
    protected TextureRegion texture;
    protected final int ID;

    public Tile(TextureRegion tex, int ID) {
        this.texture = tex;
        this.ID = ID;

        tiles[ID] = this;
    }

    public void tick() {}

    public void render(Batch b, int x, int y) { // This will render a tile at the x and y of it the world has set
        b.draw(texture, x, y, TILE_SIZE, TILE_SIZE);
    }

    public boolean isSolid() {
        return true;
    }

    public int getID() {
        return ID;
    }

}
