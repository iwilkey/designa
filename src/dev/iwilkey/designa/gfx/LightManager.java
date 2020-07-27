package dev.iwilkey.designa.gfx;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import dev.iwilkey.designa.AppBuffer;
import dev.iwilkey.designa.tiles.Tile;
import dev.iwilkey.designa.world.World;

public class LightManager {
	
	private AppBuffer ab;
	private World world;
	private ArrayList<Light> lights = new ArrayList<Light>();
	
	public LightManager(AppBuffer ab, World world) {
		this.ab = ab;
		this.world = world;
	}
	
	public void renderLight(Graphics g, int x, int y) {
		int xx = (int) (x * Tile.TILE_SIZE - ab.getCamera().getxOffset());
		int yy = (int) (y * Tile.TILE_SIZE - ab.getCamera().getyOffset());
		Color dark = new Color(0,0,0,0);
		switch(world.lightMap[x][y]) {
			case 6:
				dark = new Color(0,0,0, 0);
				break;
			case 5:
				dark = new Color(0,0,0, Math.round(255 / 4));
				break;
			case 4:
				dark = new Color(0,0,0, Math.round(255 / 2.5f));
				break;
			case 3:
				dark = new Color(0,0,0, Math.round(255 / 1.75f));
				break;
			case 2:
				dark = new Color(0,0,0, Math.round(255 / 1.40f));
				break;
			case 1:
				dark = new Color(0,0,0, Math.round(255 / 1.10f));
				break;
			
			default:
				dark = new Color(0,0,0, 255);
		}
		
		g.setColor(dark);
		g.fillRect(xx, yy, Tile.TILE_SIZE, Tile.TILE_SIZE);
	}
	
	public void addLight(int x, int y, int strength) {
		for(int i = 0; i < lights.size(); i++) {
			if(lights.get(i).x == x && lights.get(i).y == y) return;
		}
		
		lights.add(new Light(x, y, strength));
		
		bakeLighting();
	}
	
	public void removeLight(int x, int y) {
		boolean found = false;
		for(int i = 0; i < lights.size(); i++) {
			if(lights.get(i).x == x && lights.get(i).y == y) {
				lights.remove(i);
				found = true;
			}
		}
		
		if(found) {
			if(lights.size() >= 1) bakeLighting();
			else world.setLightMap(world.getLightMap());
		}
	}
	
	public void bakeLighting() {
		int ww = world.getWorldWidth();
		int hh = world.getWorldHeight();
		
		int oldLm[][] = new int[ww][hh];
		for(int y = 0; y < hh; y++) {
			for(int x = 0; x < ww; x++) {
				oldLm[x][y] = world.getLightMap()[x][y];
			}
		}
		
		int[][] newLm = new int[ww][hh];
		for (int i = 0; i < lights.size(); i++) {
			if(i > 0) newLm = lights.get(i).buildLightMap(newLm, ww, hh);
			else newLm = lights.get(i).buildLightMap(oldLm, ww, hh);
			
		}
		world.setLightMap(newLm);
	}
}
