package dev.iwilkey.designa.states;

import java.awt.Graphics;

import dev.iwilkey.designa.AppBuffer;
import dev.iwilkey.designa.gfx.Text;
import dev.iwilkey.designa.world.World;

public class GameState extends State {
	
	private AppBuffer ab;
	private World world;
	
	public GameState(AppBuffer ab) {
		this.ab = ab;
		
		world = new World(ab, "res/worlds/testworld.txt");
		ab.setWorld(world);
	}
	
	@Override
	public void tick() {
		world.tick();
	}

	@Override
	public void render(Graphics g) {
		world.render(g);
		Text.draw(g, "Designa demo", 20, 14);
	}
	
	public AppBuffer getAppBuffer() {
		return ab;
	}

}
