package dev.iwilkey.designa.math;

import java.awt.Point;

public class Maths {

    public static double distance(Point p1, Point p2) {
        return Math.abs(Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2)));
    }

    static byte rand = 0;
    public static boolean percentChance(int chance) {
        chance = Math.min(100, chance);
        rand = (byte)com.badlogic.gdx.math.MathUtils.random(0, 100);
        return chance > rand;
    }

}
