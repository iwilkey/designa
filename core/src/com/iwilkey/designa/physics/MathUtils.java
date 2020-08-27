package com.iwilkey.designa.physics;

import com.iwilkey.designa.entities.Entity;

public class MathUtils {

    public static float distance(Entity e1, Entity e2) {
        Vector2 displacement = new Vector2(e1.getX() - e2.getX(), e1.getY() - e2.getY());
        return Vector2.magnitude(displacement);
    }

}
