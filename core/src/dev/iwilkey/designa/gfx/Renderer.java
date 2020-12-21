package dev.iwilkey.designa.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import dev.iwilkey.designa.Game;
import dev.iwilkey.designa.io.IOUtils;
import dev.iwilkey.designa.item.creator.ItemCreator;
import dev.iwilkey.designa.scene.Scene;

// Anything that has to be drawn has to go through this class.
public class Renderer {
	
    static Camera camera;
    ShaderProgram BLUR_SHADER;
    ShaderProgram DEFAULT_SHADER;
    final SpriteBatch GAME_BATCH = new SpriteBatch();
    final SpriteBatch UI_BATCH = new SpriteBatch();

    public Renderer() {	
    	initShaders();
        Game.WINDOW_WIDTH = Gdx.graphics.getWidth();
        Game.WINDOW_HEIGHT = Gdx.graphics.getHeight();
    }
    
    private void initShaders() {
    	try {
    		
    		final String VERT = IOUtils.readFile(Gdx.files.internal("shaders/standard.vert").read());
    		final String BLUR_FRAG = IOUtils.readFile(Gdx.files.internal("shaders/blur.frag").read());
    		final String DEFAULT = IOUtils.readFile(Gdx.files.internal("shaders/default.frag").read());
    		BLUR_SHADER = new ShaderProgram(VERT, BLUR_FRAG);
    		DEFAULT_SHADER = new ShaderProgram(VERT, DEFAULT);
 
    		if(BLUR_SHADER.getLog().length() != 0 || DEFAULT_SHADER.getLog().length() != 0) {
    			System.out.println(BLUR_SHADER.getLog());
    			System.out.println(DEFAULT_SHADER.getLog());
    			System.exit(-1);
    		}
    	
    	} catch (Exception e) {
    		e.printStackTrace();
    		System.exit(-1);
    	}
    	
    	ShaderProgram.pedantic = false;
    	
    }

    public void masterRenderer() {

        Game.WINDOW_WIDTH = Gdx.graphics.getWidth();
        Game.WINDOW_HEIGHT = Gdx.graphics.getHeight();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if(ItemCreator.isActive) GAME_BATCH.setShader(BLUR_SHADER);
        else GAME_BATCH.setShader(DEFAULT_SHADER);

        if(Scene.currentScene != null) {

            GAME_BATCH.begin();
            GAME_BATCH.setTransformMatrix(Camera.mat);
            if(!Camera.isZooming) Scene.currentScene.render(GAME_BATCH);
            GAME_BATCH.flush();
            GAME_BATCH.end();

            UI_BATCH.begin();
            Scene.currentScene.onGUI(UI_BATCH);
            UI_BATCH.end();

            if(Geometry.requests.size() == 0) return;
            for(Geometry.GeometryRequest gr : Geometry.requests)
                gr.render();
            Geometry.requests.clear();

        }

    }

    public void onResize(int width, int height) {
        Game.WINDOW_WIDTH = width;
        Game.WINDOW_HEIGHT = height;
    }

    public static Camera getCamera() {
        if(Scene.currentScene.name.equals("single-player-game-scene")) return Renderer.camera;
        else return null;
    }

    public static void setCamera(Camera camera) {
        if(Scene.currentScene.name.equals("single-player-game-scene")) Renderer.camera = camera;
        else Renderer.camera = null;
    }

    public void dispose() {
        Geometry.dispose();
    }

}
