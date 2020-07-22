package dev.iwilkey.devisea.gfx;

import java.awt.image.BufferedImage;

public class SpriteSheet {
	
	public final int SLOT_SIZE = 8;
	private BufferedImage sheet;
	
	public SpriteSheet(BufferedImage sheet) {
		this.sheet = sheet;
	}
	
	public BufferedImage crop(int row, int col, int w, int h) {
		return sheet.getSubimage(row * SLOT_SIZE, col * SLOT_SIZE, w, h);
	}
	
}
