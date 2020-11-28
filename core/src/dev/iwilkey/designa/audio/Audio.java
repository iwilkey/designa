package dev.iwilkey.designa.audio;

import com.badlogic.gdx.audio.Sound;

import dev.iwilkey.designa.entity.creature.passive.Player;
import dev.iwilkey.designa.math.Maths;
import dev.iwilkey.designa.tile.Tile;

import java.awt.Point;

public class Audio {

    private static final float VOLUME_DROPOFF = 0.75f;

    public static float MASTER_VOLUME = 1.0f;
    public static float SFX_VOLUME = 1.0f;
    public static float MUSIC_VOLUME = 1.0f;

    // Plays a SFX with respect volume to the player. (Drop off over space). As loud as SFX_VOLUME.
    static float distance, relVolume = 0.0f;
    public static void playSpacialSFX(Sound sound, Point point, Player player) {
        distance = (float)(Maths.distance(point, new Point((int)player.x, (int)player.y)));
        relVolume = ((distance / Tile.TILE_SIZE) > 1) ?
                (2.0f * (VOLUME_DROPOFF / ((distance / Tile.TILE_SIZE) + 1))) : 1.0f;
        if(point.x > player.x) sound.play(Math.max((MASTER_VOLUME * SFX_VOLUME) - (1.0f - relVolume), 0), 1, 0.35f);
        else if (point.x == player.x) sound.play(Math.max((MASTER_VOLUME * SFX_VOLUME) - (1.0f - relVolume), 0), 1, 0);
        else sound.play(Math.max((MASTER_VOLUME * SFX_VOLUME) - (1.0f - relVolume), 0), 1, -0.35f);
    }

    // Plays a SFX with respect to nothing. As loud as SFX_VOLUME.
    public static void playSFX(Sound sound) {
        sound.play(Math.max(MASTER_VOLUME * SFX_VOLUME, 0));
    }
    public static void playSFX(Sound sound, float relVol) {
        sound.play(Math.max((MASTER_VOLUME * SFX_VOLUME) - (1 - relVol), 0));
    }

    // Plays music with respect to nothing. As loud as MUSIC_VOLUME.
    public static void playMusic(Sound sound) {
        sound.play(Math.max(MASTER_VOLUME * MUSIC_VOLUME, 0));
    }

}
