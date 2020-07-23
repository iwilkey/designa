package dev.iwilkey.designa;

import dev.iwilkey.designa.gfx.Camera;
import dev.iwilkey.designa.input.Input;
import dev.iwilkey.designa.world.World;

public class AppBuffer {

	private Game game;
	private World world;
	private Input input;
	
	public AppBuffer(Game game, Input input) {
		this.game = game;
		this.input = input;
		this.world = null;
	}
	
	public Game getGame() {		
		return game;
	}
	
	public World getWorld() {
		return world;
	}
	
	public Input getInput() {
		return input;
	}
	
	public Camera getCamera() {
		return game.getCamera();
	}
	
	// For ease of access
	public double dt() {
		return game.getDt();
	}
	
	public void setWorld(World w) {
		this.world = w;
	}
}
