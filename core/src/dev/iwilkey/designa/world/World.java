package dev.iwilkey.designa.world;

import com.badlogic.gdx.graphics.g2d.Batch;

import dev.iwilkey.designa.entity.EntityHandler;
import dev.iwilkey.designa.entity.creature.passive.Player;
import dev.iwilkey.designa.gfx.Camera;
import dev.iwilkey.designa.gfx.LightHandler;
import dev.iwilkey.designa.gfx.Renderer;
import dev.iwilkey.designa.item.ActiveItemHandler;
import dev.iwilkey.designa.item.Item;
import dev.iwilkey.designa.tile.Tile;
import dev.iwilkey.designa.ui.UIManager;

public class World {

    public final int WIDTH, HEIGHT;

    public byte[][] FRONT_TILES,
        LIGHT_MAP;

    public UIManager uiManager;
    public AmbientCycle ambientCycle;
    public LightHandler lightHandler;
    public EntityHandler entityHandler;
    public ActiveItemHandler activeItemHandler;
    public Player player;

    public World(UIManager uiManager, int width, int height) {

        this.WIDTH = width; this.HEIGHT = height;
        FRONT_TILES = new byte[WIDTH][HEIGHT];
        LIGHT_MAP = new byte[WIDTH][HEIGHT];

        debugGenerateWorld();

        this.uiManager = uiManager;
        ambientCycle = new AmbientCycle(this);
        lightHandler = new LightHandler(this);
        lightHandler.bake();
        player = new Player(this, 100 * Tile.TILE_SIZE, 60 * Tile.TILE_SIZE);
        entityHandler = new EntityHandler();
        activeItemHandler = new ActiveItemHandler(this);
        entityHandler.addEntity(player);
        Renderer.setCamera(new Camera(this, 100 * Tile.TILE_SIZE, 60 * Tile.TILE_SIZE));

        debugInit();

    }

    private void debugInit() {
        activeItemHandler.spawn(Item.DIRT, 100 * Tile.TILE_SIZE, 60 * Tile.TILE_SIZE);
    }

    public void debugGenerateWorld() {

        // Front tiles
        for(int x = 0; x < WIDTH; x++) {
            for(int y = 0; y < HEIGHT; y++) {
                if(y > HEIGHT - 50) FRONT_TILES[x][y] = (byte)Tile.AIR.getTileID();
                else if (y == HEIGHT - 50) FRONT_TILES[x][y] = (byte)Tile.GRASS.getTileID();
                else FRONT_TILES[x][y] = (byte)Tile.DIRT.getTileID();
            }
        }

    }

    public Tile getTile(int x, int y) {
        if(y > HEIGHT - 1 || x > WIDTH - 1) return Tile.AIR;
        if(y < 0 || x < 0) return Tile.AIR;
        for (Tile t : Tile.values())
            if (t.getTileID() == FRONT_TILES[x][y]) return t;
        return null;
    }

    public void tick() {
        ambientCycle.tick();
        Renderer.getCamera().tick();
        entityHandler.tick();
        activeItemHandler.tick();
    }

    int xStart, xEnd, yStart, yEnd;
    public void render(Batch b) {

        ambientCycle.render(b);
        activeItemHandler.render(b);

        xStart = (int) Math.max(0, ((-Camera.position.x / Camera.scale.x) / Tile.TILE_SIZE) - 2);
        xEnd = (int) Math.min(WIDTH, ((((-Camera.position.x + Camera.GW) / Camera.scale.x) / Tile.TILE_SIZE)) + 2);
        yStart = (int) Math.max(0, ((-Camera.position.y / Camera.scale.y) / Tile.TILE_SIZE) - 2);
        yEnd = (int) Math.min(HEIGHT, ((((-Camera.position.y + Camera.GH) / Camera.scale.y) / Tile.TILE_SIZE)) + 2);

        for(int x = xStart; x < xEnd; x++) {
            for (int y = yStart; y < yEnd; y++) {
                for(Tile t : Tile.values()) {
                    if(FRONT_TILES[x][y] == t.getTileID())
                        t.render(b, x * Tile.TILE_SIZE, y * Tile.TILE_SIZE);
                }
            }
        }

        for(int x = xStart; x < xEnd; x++)
            for (int y = yStart; y < yEnd; y++)
                lightHandler.renderLight(b, x, y);

        entityHandler.render(b);
    }

}
