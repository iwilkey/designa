package dev.iwilkey.designa.layer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import dev.iwilkey.designa.AppBuffer;

public abstract class Layer {
	
	public static int ID = 0;

	protected int id;
	protected BufferedImage image;
	protected AppBuffer ab;
	
	public Layer(AppBuffer ab, BufferedImage im) {
		this.image = im;
		this.ab = ab;
		Layer.ID++;
		this.id = Layer.ID;
	}
	
	public void changeImage(BufferedImage im) {
		image = im;
	}
	
	public int getID() {
		return this.id;
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);
}
