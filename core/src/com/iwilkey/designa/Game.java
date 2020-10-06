package com.iwilkey.designa;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Null;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Camera;
import com.iwilkey.designa.gfx.Geometry;
import com.iwilkey.designa.gui.Hud;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.inventory.Inventory;
import com.iwilkey.designa.states.*;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.world.World;

import java.util.ArrayList;

public class Game extends ApplicationAdapter {

	public static int w;
	public static int h;

	public static GameBuffer gb;

	SpriteBatch gameBatch;
	SpriteBatch guiBatch;

	private State mainMenuState, worldSelectorState,
			worldCreatorState, worldLoaderState, gameState;
	private static ArrayList<State> states;

	private Camera camera;

	private InputHandler input;

	public static ArrayList<Geometry.GeometryRequest> geometryRequests;

	@Override
	public void create () {
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();

		Assets.init();

		gameBatch = new SpriteBatch();
		guiBatch = new SpriteBatch();

		gb = new GameBuffer(this);

		input = new InputHandler();

		geometryRequests = new ArrayList<>();

		mainMenuState = new MainMenuState();
		worldSelectorState = new WorldSelectorState();
		worldCreatorState = new WorldCreatorState();
		worldLoaderState = new WorldLoaderState();
		gameState = new GameState();

		states = new ArrayList<>();
		states.add(mainMenuState);
		states.add(worldSelectorState);
		states.add(worldCreatorState);
		states.add(worldLoaderState);
		states.add(gameState);

		State.setState(worldLoaderState);
		State.getCurrentState().start();
	}

	private void tick() {
		input.tick();
		if(!Hud.gameMenu) if(State.getCurrentState() != null) State.getCurrentState().tick();
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

		gameBatch.begin();
		gameBatch.setTransformMatrix(Camera.mat);
		if(!Camera.isZooming) State.getCurrentState().render(gameBatch);
		gameBatch.flush();
		gameBatch.end();

		guiBatch.begin();
		if(State.getCurrentState() != null) State.getCurrentState().onGUI(guiBatch);
		guiBatch.end();

		if(!Hud.gameMenu && !Inventory.active) drawGeometry();

		if(timer > 1000000000) {
			tps = ticks;
			ticks = 0;
			timer = 0;
		}
	}

	public void drawGeometry() {
		if(geometryRequests.size() == 0) return;
		for(Geometry.GeometryRequest gr : geometryRequests)
			gr.render();
		geometryRequests.clear();
	}
	
	@Override
	public void dispose () {
		gameBatch.dispose();
		guiBatch.dispose();
		if(State.getCurrentState() != null) State.getCurrentState().dispose();
		if(State.getCurrentState() == gameState) gb.getWorld().saveWorld();
	}

	public static ArrayList<State> getStates() {
		return states;
	}

	public Camera getCamera() {
		if(State.getCurrentState() == gameState) return camera;
		else return null;
	}

	public void setCamera(Camera camera) {
		if(State.getCurrentState() == gameState) this.camera = camera;
		else this.camera = null;
	}

	public SpriteBatch getGameBatch() { return gameBatch; }
}
