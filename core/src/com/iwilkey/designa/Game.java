package com.iwilkey.designa;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Camera;
import com.iwilkey.designa.gfx.LightManager;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.items.ItemRecipe;
import com.iwilkey.designa.states.GameState;
import com.iwilkey.designa.states.State;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.world.World;

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

	// Input
	private InputHandler input;

	@Override
	public void create () {
		// Init graphics batch
		gameBatch = new SpriteBatch();
		guiBatch = new SpriteBatch();

		// Init assets
		Assets.init();

		// Game Buffer
		gb = new GameBuffer(this);

		// Input
		input = new InputHandler();

		// Init states
		gameState = new GameState(gb);

		// Init the starting state
		State.setState(gameState);
		State.getCurrentState().start();

		// Init dimensions
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();

		// Init camera
		camera = new Camera(gb, (World.w / 2) * Tile.TILE_SIZE,
				LightManager.highestTile[World.w / 2] * Tile.TILE_SIZE);
	}

	private void tick() {
		input.tick();
		if(State.getCurrentState() != null) {
			State.getCurrentState().tick();
		}
	}

	long lt = System.nanoTime();
	long now = 0;
	long timer = 0;
	int ticks = 0;
	public static int tps;

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
		if(State.getCurrentState() != null) {
			State.getCurrentState().render(gameBatch);
		}
		gameBatch.setTransformMatrix(Camera.mat);
		gameBatch.end();

		// Render GUI
		guiBatch.begin();
		if(State.getCurrentState() != null) {
			State.getCurrentState().onGUI(guiBatch);
		}
		guiBatch.end();

		if(timer > 1000000000) {
			tps = ticks;
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
