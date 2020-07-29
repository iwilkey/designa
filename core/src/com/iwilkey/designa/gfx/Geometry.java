package com.iwilkey.designa.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Geometry {

    public enum ShapeType {
        FILLED, LINE
    }

    public static ShapeRenderer sr = new ShapeRenderer();

    public static void square(float x, float y, float size, Color color) {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(color);
        sr.rect(x, y, size, size);
        sr.end();
    }

    public static void square(float x, float y, float size, Color color, ShapeType st) {
        deferShapeType(st);
        sr.setColor(color);
        sr.rect(x, y, size, size);
        sr.end();
    }

    public static void rect(float x, float y, float width, float height, Color color) {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(color);
        sr.rect(x, y, width, height);
        sr.end();
    }

    public static void rect(float x, float y, float width, float height, Color color, ShapeType st) {
        deferShapeType(st);
        sr.setColor(color);
        sr.rect(x, y, width, height);
        sr.end();
    }

    public static void circle(float x, float y, float radius, Color color) {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(color);
        sr.circle(x, y, radius);
        sr.end();
    }

    public static void circle(float x, float y, float radius, Color color, ShapeType st) {
        deferShapeType(st);
        sr.setColor(color);
        sr.circle(x, y, radius);
        sr.end();
    }

    private static void deferShapeType(ShapeType st) {
        switch(st) {
            case FILLED:
                sr.begin(ShapeRenderer.ShapeType.Filled);
                break;
            case LINE:
                sr.begin(ShapeRenderer.ShapeType.Line);
                break;
        }
    }
}
