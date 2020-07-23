package dev.iwilkey.designa.items;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import dev.iwilkey.designa.AppBuffer;
import dev.iwilkey.designa.assets.Assets;

public class Item {
	
	// Statics
	
	public static Item[] items = new Item[256];
	public static Item dirtItem = new Item(Assets.itemDirt, "Dirt", 0);
	
	public static final int ITEM_SIZE = 12;
	
	// Class
	
	protected AppBuffer ab;
	protected BufferedImage texture;
	protected String name;
	protected final int ID;
	
	protected Rectangle collider;
	protected int x, y, count;
	protected boolean pickedUp = false;
	
	public Item(BufferedImage tex, String name, int id) {
		this.texture = tex;
		this.name = name;
		this.ID = id;
		
		collider = new Rectangle(0,0, ITEM_SIZE, ITEM_SIZE);

		items[id] = this;
	}
	
	public Item createNew(int x, int y) {
		Item i = new Item(texture, name, ID);
		i.setPosition(x, y);
		return i;
	}
	
	public void tick() {
		if(ab.getWorld().getEntityHandler().getPlayer().getCollisionBounds(0f, 0f).intersects(collider)) {
			pickedUp = true;
			// Add to personal inventory!
		}
	}
	
	public void render(Graphics g) { // Render in game world
		if(ab == null) {
			return;
		}
		render(g, (int) (x - ab.getCamera().getxOffset()), (int) (y - ab.getCamera().getyOffset()));
	}
	
	public void render(Graphics g, int x, int y) { // Render in some inventory
		g.drawImage(texture, x, y, ITEM_SIZE, ITEM_SIZE, null);
	}
	
	public void setPosition(int x, int y) {
		this.x = x; this.y = y;
		collider.x = x; collider.y = y;
	}
	
	// Getters and setters

	public AppBuffer getAppBuffer() {
		return ab;
	}

	public void setAppBuffer(AppBuffer ab) {
		this.ab = ab;
	}

	public BufferedImage getTexture() {
		return texture;
	}

	public void setTexture(BufferedImage texture) {
		this.texture = texture;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public boolean isPickedUp() {
		return pickedUp;
	}

	public void setPickedUp(boolean pickedUp) {
		this.pickedUp = pickedUp;
	}

	public int getID() {
		return ID;
	}

}
