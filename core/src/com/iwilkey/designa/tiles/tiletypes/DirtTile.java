package com.iwilkey.designa.tiles.tiletypes;

import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.tiles.Tile;

public class DirtTile extends Tile {

    // What item will it create?
    private int itemID = 0;

    public DirtTile(int ID, int strength) {
        super(Assets.dirt, ID, strength);
    }

    @Override
    public int getItemID() { return itemID; }

}
