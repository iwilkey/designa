package dev.iwilkey.devisea.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Input implements KeyListener, MouseListener, MouseMotionListener {

	private boolean[] keys, justPressed, cantPress;
	private boolean leftMouseButtonDown, rightMouseButtonDown;
	private int mouseX, mouseY;
	
	// CONTROLS
	// TODO: Move this into a control class to be customized by player
	public boolean left, right, space;
	
	public Input() {
		keys = new boolean[256];
		justPressed = new boolean[256];
		cantPress = new boolean[256];
	}
	
	// Mouse
	
	public boolean isLeftMouseButtonDown() {
		return leftMouseButtonDown;
	}
	
	public boolean isRightMouseButtonDown() {
		return rightMouseButtonDown;
	}
	
	public int getMouseX() {
		return mouseX;
	}
	
	public int getMouseY() {
		return mouseY;
	}

	@Override
	public void mouseMoved(MouseEvent m) {
		mouseX = m.getX();
		mouseY = m.getY();
	}
	
	@Override
	public void mousePressed(MouseEvent m) {
		if(m.getButton() == MouseEvent.BUTTON1) {
			leftMouseButtonDown = true;
		} else if (m.getButton() == MouseEvent.BUTTON3) {
			rightMouseButtonDown = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent m) {
		if(m.getButton() == MouseEvent.BUTTON1) {
			leftMouseButtonDown = false;
		} else if (m.getButton() == MouseEvent.BUTTON3) {
			rightMouseButtonDown = false;
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent m) {	
	}

	@Override
	public void mouseClicked(MouseEvent m) {
	}

	@Override
	public void mouseEntered(MouseEvent m) {
	}

	@Override
	public void mouseExited(MouseEvent m) {
	}
	
	// Keyboard
	
	@Override
	public void keyPressed(KeyEvent k) {
		if(k.getKeyCode() < 0 || k.getKeyCode() >= keys.length) return;
		keys[k.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent k) {
		if(k.getKeyCode() < 0 || k.getKeyCode() >= keys.length) return;
		keys[k.getKeyCode()] = false;
	}
	
	public boolean keyJustPressed(int KeyCode) {
		if(KeyCode < 0 || KeyCode >= keys.length) return false;
		return justPressed[KeyCode];
	}

	@Override
	public void keyTyped(KeyEvent k) {
	}
	
	// Tick
	
	public void tick() {
		for(int i = 0; i < keys.length; i++) {
			if(cantPress[i] && !keys[i]) {
				cantPress[i] = false;
			} else if (justPressed[i]) {
				cantPress[i] = true;
				justPressed[i] = false;
			}
			
			if(!cantPress[i] && keys[i]) {
				justPressed[i] = true;
			}
		}
		
		left = keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_D];
		space = keys[KeyEvent.VK_SPACE];
	}
	
}
