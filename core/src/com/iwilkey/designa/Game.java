package com.iwilkey.designa;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.states.GameState;
import com.iwilkey.designa.states.State;

public class Game extends ApplicationAdapter {

	// Define dimensions
	public static int w;
	public static int h;

	// GameBuffer
	private GameBuffer gb;

	// Graphics
	SpriteBatch batch;

	// States
	private State gameState;

	public Game() {

	}
	
	@Override
	public void create () {
		// Init graphics batch
		batch = new SpriteBatch();

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

		batch.begin();

		// Set camera matrix
		/*
		Vector3 position = new Vector3(100,0,0);
		Matrix4 mat = new Matrix4();
		mat.translate(position);
		batch.setTransformMatrix(mat);
		*/

		if(State.getCurrentState() != null) {
			State.getCurrentState().render(batch);
		}

		batch.end();

		if(timer > 1000000000) {
			System.out.println("tps: " + ticks);
			ticks = 0;
			timer = 0;
		}

	}
	
	@Override
	public void dispose () {
		batch.dispose();

		if(State.getCurrentState() != null) {
			State.getCurrentState().dispose();
		}
	}
}
