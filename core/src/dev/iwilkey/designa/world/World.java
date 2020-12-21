package dev.iwilkey.designa.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import dev.iwilkey.designa.assets.Assets;
import dev.iwilkey.designa.entity.EntityHandler;
import dev.iwilkey.designa.entity.creature.passive.Player;
import dev.iwilkey.designa.gfx.Camera;
import dev.iwilkey.designa.gfx.LightHandler;
import dev.iwilkey.designa.gfx.Renderer;
import dev.iwilkey.designa.item.ActiveItemHandler;
import dev.iwilkey.designa.item.creator.ItemCreator;
import dev.iwilkey.designa.tile.Tile;
import dev.iwilkey.designa.ui.UIManager;

import java.util.ArrayList;

public class World {

    public final int WIDTH, HEIGHT;

    public byte[][][] FRONT_TILES,
        BACK_TILES;
    public byte[][] LIGHT_MAP;

    // Ambient
    public AmbientCycle ambientCycle;
    public ArrayList<Cloud> clouds; // :)

    public UIManager uiManager;
    public LightHandler lightHandler;
    public EntityHandler entityHandler;
    public ActiveItemHandler activeItemHandler;
    public Player player;

    public World(UIManager uiManager, int width, int height) {

        this.WIDTH = width; this.HEIGHT = height;
        FRONT_TILES = new byte[WIDTH][HEIGHT][2];
        BACK_TILES = new byte[WIDTH][HEIGHT][2];
        LIGHT_MAP = new byte[WIDTH][HEIGHT];
        clouds = new ArrayList<>();
        WorldGeneration.GenerateWorld(width, height, FRONT_TILES, BACK_TILES, clouds);

        this.uiManager = uiManager;
        ambientCycle = new AmbientCycle(this);
        lightHandler = new LightHandler(this);
        lightHandler.bake();

        WorldGeneration.GenerateCaves(WIDTH, HEIGHT, FRONT_TILES, BACK_TILES, lightHandler.highestFrontTile);

        player = new Player(this, (WIDTH / 2f) * Tile.TILE_SIZE, (lightHandler.highestFrontTile[(WIDTH / 2)] * Tile.TILE_SIZE) + 4);
        entityHandler = new EntityHandler();
        activeItemHandler = new ActiveItemHandler(this);
        entityHandler.addEntity(player);
        Renderer.setCamera(new Camera(this, (int)(WIDTH / 2f) * Tile.TILE_SIZE, (HEIGHT - 40) * Tile.TILE_SIZE));

    }

    public Tile getFrontTile(int x, int y) {
        if(y > HEIGHT - 1 || x > WIDTH - 1) return Tile.AIR;
        if(y < 0 || x < 0) return Tile.AIR;
        for (Tile t : Tile.values())
            if (t.getTileID() == FRONT_TILES[x][y][0]) return t;
        return null;
    }

    public void tick() {
        ambientCycle.tick();
        Renderer.getCamera().tick();
        entityHandler.tick();
        activeItemHandler.tick();
        for(Cloud c : clouds) c.tick();
    }

    int xStart, xEnd, yStart, yEnd;
    public void render(Batch b) {

        ambientCycle.render(b);

        xStart = (int) Math.max(0, ((-Camera.position.x / Camera.scale.x) / Tile.TILE_SIZE) - 2);
        xEnd = (int) Math.min(WIDTH, ((((-Camera.position.x + Camera.GW) / Camera.scale.x) / Tile.TILE_SIZE)) + 2);
        yStart = (int) Math.max(0, ((-Camera.position.y / Camera.scale.y) / Tile.TILE_SIZE) - 2);
        yEnd = (int) Math.min(HEIGHT, ((((-Camera.position.y + Camera.GH) / Camera.scale.y) / Tile.TILE_SIZE)) + 2);

        for(Cloud c : clouds)
            if(c.position.x > (xStart * Tile.TILE_SIZE) - 120 && c.position.x < (xEnd * Tile.TILE_SIZE) + 120)
                if(c.position.y > (yStart * Tile.TILE_SIZE) - 120 && c.position.y < (yEnd * Tile.TILE_SIZE) + 120)
                    c.render(b);

        for(int x = xStart; x < xEnd; x++) {
            for (int y = yStart; y < yEnd; y++) {

                if(FRONT_TILES[x][y][0] == Tile.AIR.getTileID() || BACK_TILES[x][y][0] == Tile.AIR.getTileID())
                    if(y * Tile.TILE_SIZE < (lightHandler.originalHighestFrontTiles[x]) * Tile.TILE_SIZE) b.draw(Assets.backDirt,
                            x * Tile.TILE_SIZE, y * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);

                for(Tile t : Tile.values())
                    if (BACK_TILES[x][y][0] == t.getTileID() && t.getTileID() != Tile.AIR.getTileID())
                        t.renderBack(b, (x * Tile.TILE_SIZE), y * Tile.TILE_SIZE);

                if(LIGHT_MAP[x][y] <= 0) continue;

                for(Tile t : Tile.values())
                    if(FRONT_TILES[x][y][0] == t.getTileID())
                        t.render(b, x * Tile.TILE_SIZE, y * Tile.TILE_SIZE, FRONT_TILES[x][y][1]);
            }
        }
        
        if(!ItemCreator.isActive) entityHandler.render(b);

        for(int x = xStart; x < xEnd; x++)
            for (int y = yStart; y < yEnd; y++)
                lightHandler.renderLight(b, x, y);

        activeItemHandler.render(b);

    }

    public static class Cloud {
        public Texture texture;
        public Vector2 position;
        private final int width;
        public static final int cloudW = 100, cloudH = (int) (100 / 2.27f);
        public Cloud(Vector2 pos, int width) {
            this.position = pos;
            this.width = width;
            texture = Assets.clouds[MathUtils.random(0,3)];
        }

        public void tick() {
            position.x %= width * Tile.TILE_SIZE;
            position.x += MathUtils.random(0.04f, 0.05f);
        }

        public void render(Batch b) {
            b.draw(texture, position.x, position.y, cloudW, cloudH);
        }
    }

}
