package dev.iwilkey.designa.inventory;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import dev.iwilkey.designa.AppBuffer;
import dev.iwilkey.designa.items.Item;

public abstract class Inventory {

	protected AppBuffer ab;
	protected List<Item> items;
	protected int totalSlots;
	
	protected int slotDistBuffer = 2;
	protected int startX, startY;
	
	public Inventory(AppBuffer ab, int size, int sX, int sY) {
		this.ab = ab;
		this.totalSlots = size;
		this.startX = sX; this.startY = sY;
		items = new ArrayList<Item>();
	}
	
	public void addItem(Item i) {
		if(items.contains(i)) {
			for(Item item : items) {
				if(item == i) item.setCount(item.getCount() + 1);
			}
		} else {
			if(!(items.size() + 1 > totalSlots)) {
				i.setCount(i.getCount() + 1);
				items.add(i);
			} else {
				return;
			}
		}
	}
	
	public void removeItem(Item i) {
		if(items.contains(i)) {
			for(Item item : items) {
				if(item == i) { 
					if(!(item.getCount() - 1 <= 0)) item.setCount(item.getCount() - 1);
					else items.remove(i);
				}
			}
		} else {
			return;
		}
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);
	
}
