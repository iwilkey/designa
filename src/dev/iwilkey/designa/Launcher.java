package dev.iwilkey.designa;

public class Launcher {
	
	public static final String NAME = "Designa";
	public static final int HEIGHT = 600;
	public static final int WIDTH = 600;
	
	public static void main(String[] args) {
		Game game = new Game(NAME, WIDTH, HEIGHT);
		game.start();
	}
	
	
}
