package dev.iwilkey.devisea.tiles;

import dev.iwilkey.devisea.assets.Assets;

public class AirTile extends Tile {

	public AirTile(int id) {
		super(Assets.air, id);
	}
	
	@Override
	public boolean isSolid() {
		return false;
	}

}
