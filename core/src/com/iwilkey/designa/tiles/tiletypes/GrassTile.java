package com.iwilkey.designa.tiles.tiletypes;

import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.tiles.Tile;

public class GrassTile extends Tile {

    public int itemID = 0;

    public GrassTile(int ID, int strength) {
        super(Assets.grass, ID, strength);
    }

    @Override
    public int getItemID() { return itemID; }

}
