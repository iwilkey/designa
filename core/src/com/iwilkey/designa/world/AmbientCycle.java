package com.iwilkey.designa.world;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.iwilkey.designa.Game;
import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.utils.Utils;
import com.iwilkey.designa.wave.Wave;

import java.util.Arrays;

public class AmbientCycle {

    private final World world;
    private final GameBuffer gb;

    public int time = 1, secondsPerDay = 1200;
    private float maxTickTime = secondsToTicks(secondsPerDay);
    public static float percentOfDay = 100;
    public boolean posTime = true;
    public int daysSurvived;

    public int speedUpTimeFactor = 1;

    public Wave wave;

    public static boolean timeSpeed = false;

    public AmbientCycle(World world, GameBuffer gb) {
        this.world = world;
        this.gb = gb;
        wave = new Wave(gb);
    }

    public void tick() {

        if(InputHandler.speedUpTimeRequest && ticksToSeconds(time) > 20) {
            speedUpTimeFactor = 50;
            timeSpeed = true;
        }
        else {
            speedUpTimeFactor = 1;
            timeSpeed = false;
        }

        if(maxTickTime != secondsToTicks(secondsPerDay)) maxTickTime = secondsToTicks(secondsPerDay);
        if(time + 1 > maxTickTime) {
            posTime = !posTime;
            wave.startWave();
        }
        else if (time - 1 < 0) {
            posTime = !posTime;
            daysSurvived++;
        }
        if(posTime) time += 1 * speedUpTimeFactor;
        else time += -1 * speedUpTimeFactor;

        if(time % 60 == 0) Assets.invClick.play(0.10f);

        percentOfDay = Math.abs(((float)(secondsPerDay) -
                ticksToSeconds(time)) / (float)secondsPerDay) * 100;

        wave.tick();
    }

    private TextureRegion lastSky;
    public static String timerDisplay = "";
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

        b.draw(shadeOfSky, xx, yy, 16, 16);
        if(shadeOfSky != lastSky) gb.getWorld().getLightManager().bakeLighting();
        lastSky = shadeOfSky;

        if(posTime) timerDisplay = "Midnight in -" + secondsToStandardTimerTime((secondsPerDay) - (int)ticksToSeconds(time))[0] + ":" +
                secondsToStandardTimerTime((secondsPerDay) - (int)ticksToSeconds(time))[1];
        else timerDisplay = "High noon in +" + secondsToStandardTimerTime((int)ticksToSeconds(time))[0] + ":" +
            secondsToStandardTimerTime((int)ticksToSeconds(time))[1];

    }

    private String[] secondsToStandardTimerTime(int seconds) {
        String[] timer = new String[2];

        int min = (int)Math.floor((double)seconds / 60);
        if(min < 10) timer[0] = "0" + min;
        else timer[0] = Utils.toString(min);
        int sec = seconds % 60;
        if(sec < 10) timer[1] = "0" + sec;
        else timer[1] = Utils.toString(sec);

        return timer;
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
