package com.iwilkey.designa.entities.statics;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.entities.Entity;

/**

 This is an abstract class that defines the common facilities of a Designa static entity (an entity that cannot move).

 @author Ian Wilkey (iwilkey)
 @version VERSION
 @since 7/21/2020

 */

// Note: this class extends the Entity class.
public abstract class StaticEntity extends Entity {
    /**
     * StaticEntity constructor.
     * @param gb A StaticEntity needs the GameBuffer.
     * @param x The x location of the StaticEntity.
     * @param y The y location of the StaticEntity.
     * @param w The width of the StaticEntity.
     * @param h The height of the StaticEntity.
     */
    public StaticEntity(GameBuffer gb, float x, float y, int w, int h) {
        // Invoke the Entity constructor.
        super(gb, x, y, w, h);
    }

}
