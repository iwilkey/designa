package dev.iwilkey.devisea;

import dev.iwilkey.devisea.input.Input;
import dev.iwilkey.devisea.world.World;

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
	
	// For ease of access
	public double dt() {
		return game.getDt();
	}
	
	public void setWorld(World w) {
		this.world = w;
	}
}
