package com.iwilkey.designa.entities.creature.violent;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.entities.creature.Creature;

import java.awt.*;

/**

 This is an abstract class that defines the common facilities of a Designa enemy.

 @author Ian Wilkey (iwilkey)
 @version VERSION
 @since 7/21/2020

 */

// Note: This object is a Creature.
public abstract class Enemy extends Creature {
    public Rectangle hitBox;
    /**
     * The Enemy constructor.
     * @param gb An enemy needs the GameBuffer.
     * @param x The x spawn location of the enemy.
     * @param y The y spawn location of the enemy.
     * @param w The width of the enemy.
     * @param h The height of the enemy.
     */
    public Enemy(GameBuffer gb, float x, float y, int w, int h) {
        // Invoke the Creature constructor.
        super(gb, x, y, w, h);
    }
}
