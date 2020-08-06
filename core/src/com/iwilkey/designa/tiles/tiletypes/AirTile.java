package com.iwilkey.designa.tiles.tiletypes;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.tiles.Tile;

public class AirTile extends Tile {

    public AirTile(int ID, int strength) {
        super(Assets.air, ID, strength);
    }

    @Override
    public boolean isSolid() {
        return false;
    }

}
