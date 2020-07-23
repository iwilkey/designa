package dev.iwilkey.designa.gfx;

import java.awt.image.BufferedImage;

public class Animation {
	
	private int speed, index;
	private long lt, timer;
	private BufferedImage[] frames;
	
	public Animation(int speed, BufferedImage[] frames) {
		this.frames = frames;
		this.speed = speed;
		index = 0;
		timer = 0;
		lt = System.currentTimeMillis();
	}
	
	public void tick() {
		timer += System.currentTimeMillis() - lt;
		lt = System.currentTimeMillis();
		
		if(timer > speed) {
			index++;
			timer = 0;
			
			if(index >= frames.length) {
				index = 0;
			}
		}
	}
	
	public BufferedImage getCurrentFrame() {
		return frames[index];
	}
	
	public void changeSpeed(int s) {
		speed = s;
	}
	
}
