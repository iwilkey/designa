package com.iwilkey.designa.tiles.tiletypes;

import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.tiles.Tile;

public class StoneTile extends Tile {

    private int itemID = 1;

    public StoneTile(int ID, int strength) {
        super(Assets.stone, ID, strength);
    }

    @Override
    public int getItemID() { return itemID; }

}
