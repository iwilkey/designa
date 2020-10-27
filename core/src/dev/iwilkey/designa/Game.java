package dev.iwilkey.designa;

import com.badlogic.gdx.ApplicationAdapter;

import com.badlogic.gdx.Gdx;
import dev.iwilkey.designa.assets.Assets;
import dev.iwilkey.designa.gfx.Renderer;
import dev.iwilkey.designa.clock.Clock;
import dev.iwilkey.designa.input.InputHandler;
import dev.iwilkey.designa.scene.Scene;

public class Game extends ApplicationAdapter {

	public static int WINDOW_WIDTH, WINDOW_HEIGHT;
	Clock clock;
	Renderer renderer;

	@Override
	public void create () {
		clock = new Clock();
		renderer = new Renderer();
		InputHandler.init();
		Assets.init();
		Scene.init();
		Scene.setSceneTo("single-player-game-scene");
	}

	private void tick(float dt) {
		clock.masterClock(dt);
	}

	float dt = 0;
	@Override
	public void render () {
		dt = Gdx.graphics.getDeltaTime();
		tick(dt);
		renderer.masterRenderer();
	}

	@Override
	public void resize(int width, int height) {
		clock.onResize(width, height);
		renderer.onResize(width, height);
	}

	@Override
	public void dispose () {
		renderer.dispose();
		Scene.currentScene.dispose();
	}

}
