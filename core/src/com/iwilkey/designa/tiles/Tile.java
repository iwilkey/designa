package com.iwilkey.designa.tiles;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.iwilkey.designa.assets.Assets;
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

    public void tick() {}

    public void render(Batch b, int x, int y, int bl, int id) { // This will render a tile at the x and y of it the world has set
        b.draw(texture, x, y, TILE_SIZE, TILE_SIZE);
        renderBreakLevel(b, x, y, bl, id);
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

}