package com.iwilkey.designa;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Camera;
import com.iwilkey.designa.states.GameState;
import com.iwilkey.designa.states.State;

public class Game extends ApplicationAdapter {

	// Define dimensions
	public static int w;
	public static int h;

	// GameBuffer
	private GameBuffer gb;

	// Graphics
	SpriteBatch gameBatch;
	SpriteBatch guiBatch;

	// States
	private State gameState;

	// Camera
	private Camera camera;

	@Override
	public void create () {
		// Init graphics batch
		gameBatch = new SpriteBatch();
		guiBatch = new SpriteBatch();

		// Init assets
		Assets.init();

		// Game Buffer
		gb = new GameBuffer(this);

		// Init states
		gameState = new GameState(gb);

		// Init the starting state
		State.setState(gameState);
		State.getCurrentState().start();

		// Init dimensions
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();

		// Init camera
		camera = new Camera(gb, 0, 0);
	}

	private void tick() {
		if(State.getCurrentState() != null) {
			State.getCurrentState().tick();
		}
	}

	long lt = System.nanoTime();
	long now = 0;
	long timer = 0;
	int ticks = 0;

	@Override
	public void render () {

		now = System.nanoTime();
		timer += (now - lt);
		lt = now;
		ticks++;

		tick();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Render game
		gameBatch.begin();
		gameBatch.setTransformMatrix(Camera.mat);
		if(State.getCurrentState() != null) {
			State.getCurrentState().render(gameBatch);
		}
		gameBatch.end();

		// Render GUI
		guiBatch.begin();
		if(State.getCurrentState() != null) {
			State.getCurrentState().onGUI(guiBatch);
		}
		guiBatch.end();

		if(timer > 1000000000) {
			System.out.println("tps: " + ticks);
			ticks = 0;
			timer = 0;
		}

	}
	
	@Override
	public void dispose () {
		gameBatch.dispose();
		guiBatch.dispose();

		if(State.getCurrentState() != null) {
			State.getCurrentState().dispose();
		}
	}

	public Camera getCamera() { return camera; }
}
