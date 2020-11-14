package dev.iwilkey.designa.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import dev.iwilkey.designa.Game;
import dev.iwilkey.designa.clock.Clock;
import dev.iwilkey.designa.clock.Event;
import dev.iwilkey.designa.clock.Timer;
import dev.iwilkey.designa.entity.Entity;
import dev.iwilkey.designa.input.InputHandler;
import dev.iwilkey.designa.tile.Tile;
import dev.iwilkey.designa.world.World;

public class Camera {

    World world;

    public static Matrix4 mat = new Matrix4();
    public static Vector3 position,
            scale = new Vector3(1,1,1),
            offset;
    public static int GW, GH;
    private final static float CAMERA_SPEED = 4.0f,
            CAMERA_MAX_ZOOM = 7.0f,
            CAMERA_MIN_ZOOM = 1.0f; // 1 norm
    private static float targetZoom = 1;
    public static boolean isZooming = false;

    Timer t;

    public Camera(World world, int x, int y) {
        this.world = world;
        position = new Vector3(x, y,0);
        offset = new Vector3(x - (Game.WINDOW_WIDTH / 2f), y - (Game.WINDOW_HEIGHT / 2f),0);
        GW = Game.WINDOW_WIDTH; GH = Game.WINDOW_HEIGHT;

        t = Clock.addTimer(new Timer(10, new Event() {
            @Override
            public void onEvent() {
                if(!isZooming) return;
                isZooming = false;
            }
        }));

    }

    int hx, hy;
    private void translate() {
        hx = (int) ((world.WIDTH * scale.x * Tile.TILE_SIZE) -
                (GW)) - (4 * Tile.TILE_SIZE);
        hy = (int) ((world.HEIGHT * scale.y * Tile.TILE_SIZE) -
                (GH / 2f));
        checkBounds(0, 0, hx, hy);
    }

    float x, y;
    private void checkBounds(int lx, int ly, int hx, int hy) {

        if(offset.x <= lx) x = lx;
        else if(offset.x >= hx) x = hx;
        else x = offset.x;

        if(offset.y <= ly) y = ly;
        else if(offset.y >= hy) y = hy;
        else y = offset.y;

        mat.setToTranslationAndScaling(new Vector3(-x, -y, 0), scale);

    }

    public static void zoom(float amount, Entity e) {
        isZooming = true;
        if(targetZoom - amount > CAMERA_MAX_ZOOM) {
            targetZoom = CAMERA_MAX_ZOOM;
            isZooming = false;
            InputHandler.zoomRequest = 0;
            return;
        }
        else if (targetZoom - amount < CAMERA_MIN_ZOOM) {
            targetZoom = CAMERA_MIN_ZOOM;
            isZooming = false;
            InputHandler.zoomRequest = 0;
            return;
        }
        else targetZoom -= amount;
        scale.x = targetZoom; scale.y = targetZoom;
        offset.x = (int)(e.x * scale.x - (GW / 2f) + e.width / 2f);
        offset.y = (int)(e.y * scale.y - (GH / 2f) + e.height / 2f);
        InputHandler.zoomRequest = 0;
    }

    float targxOffset, targyOffset;
    public void centerOnEntity(Entity e) {
        targxOffset = e.x * scale.x - (GW / 2f) + e.width / 2f;
        targyOffset = e.y * scale.y - (GH / 2f) + e.height / 2f;
        offset.x += (((int)targxOffset - offset.x) * CAMERA_SPEED * Gdx.graphics.getDeltaTime());
        offset.y += (((int)targyOffset - offset.y) * CAMERA_SPEED * Gdx.graphics.getDeltaTime());
        if(InputHandler.zoomRequest < 0 || InputHandler.zoomRequest > 0) zoom(InputHandler.zoomRequest, e);
    }

    public void tick() {
        mat.getTranslation(position);
        translate();
        if(!t.active) t.enable();
    }
}
