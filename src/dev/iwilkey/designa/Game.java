package dev.iwilkey.designa;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import dev.iwilkey.designa.assets.Assets;
import dev.iwilkey.designa.display.Display;
import dev.iwilkey.designa.gfx.Camera;
import dev.iwilkey.designa.input.Input;
import dev.iwilkey.designa.states.GameState;
import dev.iwilkey.designa.states.State;

public class Game implements Runnable {
	
	// Application Metadata
	private Display display;
	private String name;
	private int width, height;
	private boolean running = false;
	private Thread thread;
	
	// Rendering
	private BufferStrategy bs;
	private Graphics g;
	
	// Input
	private Input input;
	
	// App Buffer
	private AppBuffer ab;
	
	// States
	private State gameState;
	
	// Camera
	private Camera camera;
	
	// Physics
	private double dt;
	
	public Game(String name, int w, int h) {
		this.name = name;
		this.width = w;
		this.height = h;
		
		input = new Input();
	}
	
	private void init() {
		// Init assets
		Assets.init();
				
		// Init display
		display = new Display(name, width, height);
		
		// Init input
		display.getFrame().addKeyListener(input);
		display.getFrame().addMouseListener(input);
		display.getFrame().addMouseMotionListener(input);
		display.getCanvas().addMouseListener(input);
		display.getCanvas().addMouseMotionListener(input);
		
		// Init app buffer
		ab = new AppBuffer(this, input);
		
		// Init camera
		camera = new Camera(ab, 0, 0);
		
		// Init states
		gameState = new GameState(ab);
		State.setState(gameState);
	}
	
	public void start() {
		if(running) return;
		
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public void stop() {
		if(!running) return;
		
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		
		init();
		
		int tps = 60;
		double nspt = 1000000000.0 / tps;
		double delta = 0;
		long lt = System.nanoTime();
		long now = 0;
		dt = 0;
		
		long timer = 0;
		int ticks = 0;
		
		while(running) {
			now = System.nanoTime();
			delta += (now - lt) / nspt;
			dt = (now - lt) / 1000000000.0;
			timer += (now - lt);
			lt = now;
			
			if(delta >= 1) {
				tick();
				render();

				ticks++;
				delta--;
			}
			
			if(timer > 1000000000) {
				System.out.println("fps: " + ticks);
				ticks = 0;
				timer = 0;
			}
		
		}
		
		stop();
		
	}
	
	private void tick() {
		
		input.tick(); // Tick input
		
		if(State.getState() != null) { // Tick the state
			State.getState().tick();
		}
		
	}
	
	private void render() {
		bs = display.getCanvas().getBufferStrategy();
		if(bs == null) {
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		
		g = bs.getDrawGraphics();
		g.clearRect(0, 0, width, height);
		
		//Graphics2D g2d = (Graphics2D) g;
		//g2d.scale(1.5f, 1.5f);
		
		if(State.getState() != null) {
			State.getState().render(g);
		}
		
		bs.show();
		g.dispose();
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public double getDt() {
		return dt;
	}
	
	public Camera getCamera() {
		return camera;
	}

}
