package com.iwilkey.designa.tiles.tiletypes;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.tiles.Tile;

public class AirTile extends Tile {

    public AirTile(int ID) {
        super(Assets.air, ID);
    }

    @Override
    public boolean isSolid() {
        return false;
    }

}
