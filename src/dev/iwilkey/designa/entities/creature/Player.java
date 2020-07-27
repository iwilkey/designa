package dev.iwilkey.designa.entities.creature;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import dev.iwilkey.designa.AppBuffer;
import dev.iwilkey.designa.assets.Assets;
import dev.iwilkey.designa.building.BuildingHandler;
import dev.iwilkey.designa.gfx.Animation;
import dev.iwilkey.designa.inventory.PlayerInventory;
import dev.iwilkey.designa.ui.HUD;

public class Player extends Creature {
	
	// Animations
	private Animation[] animations;
	
	// HUD
	private HUD hud;
	
	// Building manager
	private BuildingHandler buildingHandler;
	
	// Inventory
	private PlayerInventory inventory;
	
	public Player(AppBuffer ap, float x, float y) {
		super(ap, x, y, Creature.DEFAULT_CREATURE_WIDTH, Creature.DEFAULT_CREATURE_HEIGHT);
		
		// Setting the collider
		collider.width = width - 14;
		collider.height = height;
		collider.x = (width / 2) - (collider.width / 2);
		collider.y = (height / 2) - (collider.height / 2) - 1;
		
		// Animations
		animations = new Animation[2];
		animations[0] = new Animation(100, Assets.walk_right);
		animations[1] = new Animation(100, Assets.walk_left);
		
		// HUD
		hud = new HUD(this);
		
		// Building Handler
		buildingHandler = new BuildingHandler(this, ab);
		
		// Init inventory
		inventory = new PlayerInventory(ab, (ab.getGame().getWidth() / 2) - 100, ab.getGame().getHeight() - 65);
	}
	
	private void getInput()
	{
		xMove = 0;
		if(ab.getInput().left) xMove -= speed;
		if(ab.getInput().right) xMove += speed;
		if(ab.getInput().keyJustPressed(KeyEvent.VK_SPACE) && !isJumping && isGrounded) jump();
	}
	@Override
	public void tick() {
	
		// Animations here
		for (Animation anim : animations) {
			anim.tick();
		}
		
		// Movement
		getInput();
		move();
		
		// Camera follow
		ab.getGame().getCamera().centerOnEntity(this);
		
		// HUD
		hud.tick();
		
		// Building handler
		buildingHandler.tick();
		
		// Inventory
		inventory.tick();
	}

	@Override
	public void render(Graphics g) {
		int xx = (int) (x - ab.getCamera().getxOffset());
		int yy = (int) (y - ab.getCamera().getyOffset());
		if(isFlashing && flashInterval >= flashIntervalTime) {
			g.drawImage(currentSprite(), xx, yy, width, height, null);
		} else if (!isFlashing) {
			g.drawImage(currentSprite(), xx, yy, width, height, null);
		}
		//drawCollider(g);
		hud.render(g);
		buildingHandler.render(g);
		inventory.render(g);
	}

	@Override
	public void die() {
		System.out.println("The player has died!");
		System.exit(1);
	}
	
	private BufferedImage currentSprite() {
		if(isMoving && isGrounded) {
			if(facingLeft) {
				return animations[1].getCurrentFrame();
			} else if (facingRight){
				return animations[0].getCurrentFrame();
			}
		} else if (isMoving && !isGrounded) {
			if(facingLeft) {
				return Assets.player_jump[0];
			} else {
				return Assets.player_jump[1];
			}
		} else {
			if(facingLeft) {
				return Assets.player[0];
			} else {
				return Assets.player[1];
			}
		}
		
		return null;
	}
	
	public void changeFacingDirection(int num) {
		switch(num) {
			case 0:
				facingRight = true;
				facingLeft = false;
				break;
			default:
				facingRight = false;
				facingLeft = true;
		}
	}
	
	public AppBuffer getAppBuffer() {
		return ab;
	}
	
}
