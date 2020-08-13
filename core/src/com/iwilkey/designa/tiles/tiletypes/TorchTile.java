package com.iwilkey.designa.tiles.tiletypes;

import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Animation;
import com.iwilkey.designa.tiles.Tile;

public class TorchTile extends Tile {

    private int itemID = 20;

    private Animation animation;

    public TorchTile(int ID, int strength) {
        super(Assets.torch[1], ID, strength);
        animation = new Animation(75, Assets.torch);
    }

    @Override
    public int getItemID() { return itemID; }

    @Override
    public void tick() {
        animation.tick();
        texture = animation.getCurrentFrame();
    }

    @Override
    public boolean isSolid() {
        return false;
    }

}
