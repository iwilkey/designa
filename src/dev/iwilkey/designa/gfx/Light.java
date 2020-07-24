package dev.iwilkey.designa.gfx;

import dev.iwilkey.designa.physics.Vector2;
import dev.iwilkey.designa.world.World;

public class Light {
	
	private World world;
	private int strength;
	private int x, y;
	
	public Light(World world, int x, int y, int strength) {
		this.world = world;
		this.strength = strength;
		this.x = x; this.y = y;
		
		updateLightMap();
	}
	
	private void updateLightMap() {
		for(int yl = y - strength; yl < y + strength + 1; yl++) {
			for(int xl = x - strength; xl < x + strength + 1; xl++) {

				try {
					
					Vector2 vec = new Vector2((x - xl), (y - yl));
					float mag = Vector2.magnitude(vec);
					int orig = world.lightMap[xl][yl];
					if(!(6 - (int)Math.round(mag) < orig))
						world.lightMap[xl][yl] = 6 - (int)Math.round(mag);
					
					if(yl == y + strength && xl == x + strength)
						world.lightMap[xl][yl] = orig;
					if(yl == y - strength && xl == x + strength)
						world.lightMap[xl][yl] = orig;
					if(yl == y + strength && xl == x - strength)
						world.lightMap[xl][yl] = orig;
					if(yl == y - strength && xl == x - strength)
						world.lightMap[xl][yl] = orig;
				
				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}				
			}
		}
	}
}
