package com.iwilkey.designa.tiles.tiletypes;

import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.tiles.Tile;

public class OakWoodTile extends Tile {

    public int itemID = 2;

    public OakWoodTile(int ID, int strength) { super(Assets.oakWood, ID, strength); }

    @Override
    public int getItemID() { return itemID; }

}
