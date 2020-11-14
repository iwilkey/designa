package dev.iwilkey.designa.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import dev.iwilkey.designa.Game;
import dev.iwilkey.designa.tile.Tile;

public class AmbientCycle {

    World world;

    public final int SECONDS_PER_DAY = 60;
    public float time;
    int subSection = 0, lastSubSection = 0;
    public boolean posTime = true;

    final Texture sky;
    Color skyTint;
    float maxTickTime = secondsToTicks(SECONDS_PER_DAY),
        percentOfDay = 100.0f,
        maxR = 115f/255f,
        maxG = 232f/255f,
        maxB = 255f/255f;

    public AmbientCycle(World world) {
        this.world = world;
        sky = new Texture("textures/ambient/sky.jpg");
        skyTint = new Color(maxR, maxG, maxB,1);
    }

    public void tick() {
        if(maxTickTime != secondsToTicks(SECONDS_PER_DAY)) {
            maxTickTime = secondsToTicks(SECONDS_PER_DAY);
        }

        if(time + 1 > maxTickTime) posTime = !posTime;
        else if (time - 1 < 0) posTime = !posTime;

        if(posTime) time += (1 * (60.0 / (1 / Gdx.graphics.getDeltaTime())));
        else time -= (1 * (60.0 / (1 / Gdx.graphics.getDeltaTime())));

        subSection = (Math.round((ticksToSeconds((int)time) / SECONDS_PER_DAY) * 10));
        if(subSection != lastSubSection) {
            world.lightHandler.bake();
            lastSubSection = subSection;
        }

        percentOfDay = Math.abs(((float)(SECONDS_PER_DAY) -
                ticksToSeconds((int)time)) / (float)SECONDS_PER_DAY);

        skyTint = new Color(maxR * percentOfDay, maxG * percentOfDay, maxB * percentOfDay, 1);
    }

    public void render(Batch b) {
        b.setColor(skyTint);
        b.draw(sky, 0,0, 5000 * Tile.TILE_SIZE, 1000 * Tile.TILE_SIZE);
        b.setColor(Color.WHITE);
    }

    private float ticksToSeconds(int ticks) { return (ticks / 60f); }
    private float secondsToTicks(int seconds) { return (seconds * 60f); }
    public float getPercentOfDay() { return percentOfDay; }

}
