package dev.iwilkey.designa.gfx;

import dev.iwilkey.designa.AppBuffer;
import dev.iwilkey.designa.entities.Entity;
import dev.iwilkey.designa.tiles.Tile;

public class Camera {
	
	private AppBuffer ab;
	private float xOffset, yOffset;
	private float camSpeed = 4.0f;
	
	public Camera(AppBuffer ab, float xOffset, float yOffset) {
		this.ab = ab;
		this.xOffset = xOffset; this.yOffset = yOffset;
	}
	
	public void checkWhiteSpace() {
		if(xOffset < 0) {
			xOffset = 0;
		} else if (xOffset >= ab.getWorld().getWorldWidth() * Tile.TILE_SIZE - ab.getGame().getWidth()) {
			xOffset = ab.getWorld().getWorldWidth() * Tile.TILE_SIZE - ab.getGame().getWidth();
		}
		
		if(yOffset < 0) {
			yOffset = 0;
		} else if (yOffset >= (ab.getWorld().getWorldHeight() * Tile.TILE_SIZE) - ab.getGame().getHeight()) {
			yOffset = (ab.getWorld().getWorldHeight() * Tile.TILE_SIZE) - ab.getGame().getHeight();
		}
	}

	public void centerOnEntity(Entity e) {
		float targxOffset = e.getX() - (ab.getGame().getWidth() / 2) + e.getWidth() / 2;
		float targyOffset = e.getY()- (ab.getGame().getHeight() / 2) + e.getHeight() / 2;
		
		xOffset += (((int)targxOffset - xOffset) * camSpeed * 0.01f);
		yOffset += (((int)targyOffset - yOffset) * camSpeed * 0.01f);

		checkWhiteSpace();
	}
	
	public float getxOffset() {
		return xOffset;
	}

	public void setxOffset(float xOffset) {
		this.xOffset = xOffset;
	}

	public float getyOffset() {
		return yOffset;
	}

	public void setyOffset(float yOffset) {
		this.yOffset = yOffset;
	}
	
}
