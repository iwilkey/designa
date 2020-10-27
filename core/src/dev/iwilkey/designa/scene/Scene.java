package dev.iwilkey.designa.scene;

import com.badlogic.gdx.graphics.g2d.Batch;
import dev.iwilkey.designa.Game;

import dev.iwilkey.designa.input.InputHandler;
import dev.iwilkey.designa.clock.Clock;
import dev.iwilkey.designa.ui.UIText;
import dev.iwilkey.designa.ui.UIManager;
import dev.iwilkey.designa.world.World;

import java.util.ArrayList;

// This defines what a Scene is. A object that encapsulates certain behaviors so that the ticker and renderer than
// tick and render the right things.
public abstract class Scene {

    public static ArrayList<Scene> sceneList;
    public static Scene currentScene;
    public static void setSceneTo(String name) {
        for(Scene s : sceneList) if(s.name.equals(name)) {
            currentScene = s;
            currentScene.start();
        }
    }
    public static void init() {
        sceneList = new ArrayList<>();
        sceneList.add(new SinglePlayerGameScene());
    }

    public String name;
    public Scene(String name) { this.name = name; }

    public abstract void start();
    public abstract void tick();
    public abstract void render(Batch b);
    public abstract void onGUI(Batch b);
    public abstract void onResize(int width, int height);
    public abstract void dispose();

    public static class SinglePlayerGameScene extends Scene {

        UIManager GUI;
        World world;

        public SinglePlayerGameScene() {
            super("single-player-game-scene");
        }

        @Override
        public void start() {
            InputHandler.initSinglePlayerGameSceneInput();

            world = new World(200, 100);

            GUI = new UIManager("gui");
            GUI.addText(new UIText("FPS: " + Clock.FPS + " " + Game.WINDOW_WIDTH + "x" + Game.WINDOW_HEIGHT,
                    22, 10, Game.WINDOW_HEIGHT - 10));
        }

        @Override
        public void tick() {
            world.tick();
            GUI.tick();
            GUI.texts.get(0).message = "FPS: " + Clock.FPS
                    + " " + Game.WINDOW_WIDTH + "x" + Game.WINDOW_HEIGHT;
        }

        @Override
        public void render(Batch b) {
            world.render(b);
        }

        @Override
        public void onGUI(Batch b) {
            GUI.render(b);
        }

        @Override
        public void onResize(int width, int height) {
            GUI.onResize(width, height);
        }

        @Override
        public void dispose() {}

    }

}
