package com.iwilkey.designa.gfx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**

 This class allows for implementation of animation whether on a tile, entity, or item.

 @author Ian Wilkey (iwilkey)
 @version VERSION
 @since 7/21/2020

 */

public class Animation {

    /**
     * Global vars
     */

    // Final vars
    private final TextureRegion[] frames;

    // Integers
    private int speed, index;

    // Longs (timers)
    private long lt, timer;

    /**
     * Animation constructor.
     * @param speed The speed at which the animation will play (in milliseconds)
     * @param frames The frames in the animations (textures)
     */
    public Animation(int speed, TextureRegion[] frames) {
        // Assign vars
        this.speed = speed;
        this.frames = frames;
        // Reset timers
        index = 0; timer = 0; lt = System.currentTimeMillis();
    }

    /**
     * Animation tick method. Runs every frame.
     */
    public void tick() {
        // Increment the timer based on system time.
        timer += System.currentTimeMillis() - lt;
        lt = System.currentTimeMillis();

        // If the timer has surpassed the speed var, go to the next frame.
        if(timer > speed) {
            // Go to the next frame
            index++;
            // Reset the timer
            timer = 0;
            // Loop the animation if needed.
            if(index >= frames.length) index = 0;
        }
    }

    /**
     * This method will return the proper texture based on the frame it currently is on.
     * @return The proper texture based on the frame it currently is on.
     */
    public TextureRegion getCurrentFrame() {
        return frames[index];
    }

    /**
     * This method will set a new speed for the animation.
     * @param s The new speed to be set.
     */
    public void changeSpeed(int s) {
        speed = s;
    }
}
