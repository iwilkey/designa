package dev.iwilkey.designa.assets;

import java.awt.image.BufferedImage;

import dev.iwilkey.designa.gfx.Image;
import dev.iwilkey.designa.gfx.SpriteSheet;

public class Assets {
	
	// Cursor
	public static BufferedImage cursor;
	
	// Letters
	public static BufferedImage[] letters;
	
	// Tiles
	public static BufferedImage air, grass;
	public static BufferedImage[] dirt;
	
	// Entities
	public static BufferedImage[] player, player_jump;
	
	// Items
	public static BufferedImage itemDirt;
	
	// GUI
	public static BufferedImage[] heart, breakLevel;
	public static BufferedImage selector, errorSelector, transSelector, HUDBar;
	
	// Animations
	public static BufferedImage[] walk_right, walk_left;
	
	
	public static void init() {
		// Init spritesheet
		SpriteSheet ss = new SpriteSheet(Image.loadImage("/textures/spritesheet.png"));
		
		// Init cursor
		cursor = ss.crop(0, 5, ss.SLOT_SIZE * 4, ss.SLOT_SIZE * 4);
		
		// Init letters
		letters = new BufferedImage[27];
		for (int i = 0; i < 27; i++) {
			letters[i] = ss.crop(i, 0, ss.SLOT_SIZE, ss.SLOT_SIZE);
		}
		
		// Init tiles
		air = ss.crop(2, 2, ss.SLOT_SIZE, ss.SLOT_SIZE);
		
		grass = ss.crop(0, 2, ss.SLOT_SIZE, ss.SLOT_SIZE);
		
		dirt = new BufferedImage[3];
		dirt[0] = ss.crop(1, 2, ss.SLOT_SIZE, ss.SLOT_SIZE);
		dirt[1] = ss.crop(1, 3, ss.SLOT_SIZE, ss.SLOT_SIZE);
		dirt[2] = ss.crop(2, 3, ss.SLOT_SIZE, ss.SLOT_SIZE);
		
		// Init player
		player = new BufferedImage[2];
		player[0] = ss.crop(3, 1, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2); // Idle left
		player[1] = ss.crop(5, 1, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2); // Idle right
		
		player_jump = new BufferedImage[2];
		player_jump[0] = ss.crop(7, 5, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2); // Jump left
		player_jump[1] = ss.crop(9, 5, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2); // Jump right
		
		// Init Animations
		walk_right = new BufferedImage[2];
		walk_right[0] = ss.crop(7, 1, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
		walk_right[1] = ss.crop(9, 1, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
		
		walk_left = new BufferedImage[2];
		walk_left[0] = ss.crop(7, 3, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
		walk_left[1] = ss.crop(9, 3, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
		
		// Init GUI
		heart = new BufferedImage[2];
		heart[0] = ss.crop(0, 3, ss.SLOT_SIZE, ss.SLOT_SIZE); // Full heart
		heart[1] = ss.crop(0, 4, ss.SLOT_SIZE, ss.SLOT_SIZE); // Empty heart
		HUDBar = ss.crop(0, 10, ss.SLOT_SIZE * 4, ss.SLOT_SIZE);
		selector = ss.crop(0, 1, ss.SLOT_SIZE, ss.SLOT_SIZE);
		errorSelector = ss.crop(1, 1, ss.SLOT_SIZE, ss.SLOT_SIZE);
		transSelector = ss.crop(2, 1, ss.SLOT_SIZE, ss.SLOT_SIZE);
		breakLevel = new BufferedImage[5];
		breakLevel[0] = ss.crop(5, 4, ss.SLOT_SIZE, ss.SLOT_SIZE);
		breakLevel[1] = ss.crop(1, 4, ss.SLOT_SIZE, ss.SLOT_SIZE);
		breakLevel[2] = ss.crop(2, 4, ss.SLOT_SIZE, ss.SLOT_SIZE);
		breakLevel[3] = ss.crop(3, 4, ss.SLOT_SIZE, ss.SLOT_SIZE);
		breakLevel[4] = ss.crop(4, 4, ss.SLOT_SIZE, ss.SLOT_SIZE);
		
		// Init items
		itemDirt = ss.crop(0, 9, ss.SLOT_SIZE, ss.SLOT_SIZE);
	
	}
	
}
