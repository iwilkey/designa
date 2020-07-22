package dev.iwilkey.devisea.states;

import java.awt.Graphics;

import dev.iwilkey.devisea.AppBuffer;
import dev.iwilkey.devisea.gfx.Text;
import dev.iwilkey.devisea.world.World;

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
		Text.draw(g, "Creationary demo", 20, 20);
	}
	
	public AppBuffer getAppBuffer() {
		return ab;
	}

}
