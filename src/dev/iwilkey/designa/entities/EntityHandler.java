package dev.iwilkey.designa.entities;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

import dev.iwilkey.designa.AppBuffer;
import dev.iwilkey.designa.entities.creature.Player;

public class EntityHandler {
	
	private AppBuffer ab;
	private Player player;
	private ArrayList<Entity> entities;
	
	public EntityHandler(AppBuffer ab, Player player) {
		this.ab = ab;
		this.player = player;
		
		entities = new ArrayList<Entity>();
		entities.add(player);
	}
	
	public void tick() {
		Iterator<Entity> it = entities.iterator();
		
		while(it.hasNext()) {
			Entity e = it.next();
			e.tick();
			if(!e.isActive()) it.remove();
		}
	}
	
	public void render(Graphics g) {
		for(Entity e : entities) {
			e.render(g);
		}
	}
	
	public void addEntity(Entity e) {
		entities.add(e);
	}
	
	// Getters and setters

	public AppBuffer getAb() {
		return ab;
	}

	public void setAb(AppBuffer ab) {
		this.ab = ab;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public void setEntities(ArrayList<Entity> entities) {
		this.entities = entities;
	}

}
