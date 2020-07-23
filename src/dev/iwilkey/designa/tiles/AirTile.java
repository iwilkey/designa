package dev.iwilkey.designa.tiles;

import dev.iwilkey.designa.assets.Assets;

public class AirTile extends Tile {

	public AirTile(int id) {
		super(Assets.air, id, 1);
	}
	
	@Override
	public boolean isSolid() {
		return false;
	}

}
