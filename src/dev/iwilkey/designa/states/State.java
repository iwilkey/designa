package dev.iwilkey.designa.states;

import java.awt.Graphics;

public abstract class State {
	
	// Static
	
	public static State currentState = null;
	
	public static State getState() {
		return currentState;
	}
	
	public static void setState(State s) {
		currentState = s;
	}
	
	// Class
	
	public State() {
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);
 	
}
