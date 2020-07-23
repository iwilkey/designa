package dev.iwilkey.designa.items;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

import dev.iwilkey.designa.AppBuffer;

public class ItemHandler {

	private AppBuffer ab;
	private ArrayList<Item> items;
	
	public ItemHandler(AppBuffer ab) {
		this.ab = ab;
		items = new ArrayList<Item>();
	}
	
	public void tick() {
		Iterator<Item> it = items.iterator();
		while(it.hasNext()) {
			Item i = it.next();
			i.tick();
			if(i.isPickedUp()) it.remove();
		}
	}
	
	public void render(Graphics g) {
		for(Item i : items) {
			i.render(g);
		}
	}
	
	public void addItem(Item i) {
		i.setAppBuffer(ab);
		items.add(i);
	}
	
	// Getters and setters
	public AppBuffer getAppBuffer() {
		return ab;
	}
	
	public void setAppBuffer(AppBuffer ab) {
		this.ab = ab;
	}

}
