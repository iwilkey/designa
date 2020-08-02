package com.iwilkey.designa.world;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;

public class AmbientCycle {

    private World world;
    private GameBuffer gb;

    private int time = 1, secondsPerDay = 10;
    private float maxTickTime = secondsToTicks(secondsPerDay), percentOfDay = 100;
    private boolean posTime = true;

    public AmbientCycle(World world, GameBuffer gb) {
        this.world = world;
        this.gb = gb;
    }

    public void tick() {
        if(maxTickTime != secondsToTicks(secondsPerDay)) maxTickTime = secondsToTicks(secondsPerDay);
        if(time + 1 > maxTickTime) {
            posTime = !posTime;
        } else if (time - 1 < 0) {
            posTime = !posTime;
        }

        if(posTime) time++;
        else time--;

        percentOfDay = Math.abs(((float)(secondsPerDay) -
                ticksToSeconds(time)) / (float)secondsPerDay) * 100;
    }

    private TextureRegion lastSky;

    public void render(Batch b, int xx, int yy) {

        /*
        percentOfDay key:
            100%: Noon
            50%: Evening / Morning <--- Depends on how time is moving (+ or -)
            0%: Night
         */

        TextureRegion shadeOfSky;

        if(percentOfDay <= 100 && percentOfDay >= 90) { shadeOfSky = Assets.sky_colors[0]; }
        else if(percentOfDay < 90 && percentOfDay >= 80) { shadeOfSky = Assets.sky_colors[1]; }
        else if (percentOfDay < 80 && percentOfDay >= 70) { shadeOfSky = Assets.sky_colors[2]; }
        else if (percentOfDay < 70 && percentOfDay >= 60) { shadeOfSky = Assets.sky_colors[3]; }
        else if (percentOfDay < 60 && percentOfDay >= 50) { shadeOfSky = Assets.sky_colors[4]; }
        else if (percentOfDay < 50 && percentOfDay >= 40) { shadeOfSky = Assets.sky_colors[5]; }
        else if (percentOfDay < 40 && percentOfDay >= 30) { shadeOfSky = Assets.sky_colors[6]; }
        else if (percentOfDay < 30 && percentOfDay >= 20) { shadeOfSky = Assets.sky_colors[7]; }
        else if (percentOfDay < 20 && percentOfDay >= 10) { shadeOfSky = Assets.sky_colors[8]; }
        else { shadeOfSky = Assets.sky_colors[9]; }

        b.draw(shadeOfSky, xx, yy + 100, 16, 16);
        if(shadeOfSky != lastSky) gb.getWorld().getLightManager().bakeLighting();
        lastSky = shadeOfSky;

    }

    private float ticksToSeconds(int ticks) {
        return (ticks / 60f);
    }

    private float secondsToTicks(int seconds) {
        return (seconds * 60f);
    }

    public void changeDaySpeed(int seconds) {
        this.secondsPerDay = seconds;
    }

    public void setTime(int ticks) {
        time = (ticks % (int)maxTickTime);
    }

    public float getPercentOfDay() { return percentOfDay; }

}
