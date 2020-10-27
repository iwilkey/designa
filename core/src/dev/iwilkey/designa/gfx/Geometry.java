package dev.iwilkey.designa.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class Geometry {

    public enum ShapeType {
        LINE, RECTANGLE, ROUNDED_RECTANGLE, CIRCLE;
    }

    static ShapeRenderer shapeRenderer = new ShapeRenderer();
    public static void dispose() { shapeRenderer.dispose(); }
    public static ArrayList<GeometryRequest> requests = new ArrayList<>();

    public static abstract class GeometryRequest {
        protected ShapeType shapeType;
        public GeometryRequest(ShapeType shapeType) {
            this.shapeType = shapeType;
        }
        public abstract void render();
        public ShapeType getShapeType() { return shapeType; }
    }

    public static class Line extends GeometryRequest {

        int x1, x2, y1, y2;
        byte width;
        Color color;

        public Line(int x1, int y1, int x2, int y2) {
            super(ShapeType.LINE);
            this.x1 = x1; this.x2 = x2;
            this.y1 = y1; this.y2 = y2;
            width = 1;
            color = new Color(1, 1, 1, 1);
        }

        public Line(int x1, int y1, int x2, int y2, int width) {
            super(ShapeType.LINE);
            this.x1 = x1; this.x2 = x2;
            this.y1 = y1; this.y2 = y2;
            this.width = (byte)width;
            color = new Color(1, 1, 1, 1);
        }

        public Line(int x1, int y1, int x2, int y2, int width, Color color) {
            super(ShapeType.LINE);
            this.x1 = x1; this.x2 = x2;
            this.y1 = y1; this.y2 = y2;
            this.width = (byte)width;
            this.color = color;
        }

        @Override
        public void render() {
            Gdx.gl.glLineWidth(width);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setTransformMatrix(Camera.mat);
            shapeRenderer.setColor(color);
            shapeRenderer.line(x1, y1, x2, y2);
            shapeRenderer.end();
        }

    }

    public static class RectangleOutline extends GeometryRequest {

        int x, y;
        short width, height;
        byte stokeWidth;
        Color color;

        public RectangleOutline(int x, int y, int width, int height) {
            super(ShapeType.RECTANGLE);
            this.x = x; this.y = y;
            this.width = (short)width; this.height = (short)height;
            stokeWidth = 1;
            color = new Color(1,1,1,1);
        }

        public RectangleOutline(int x, int y, int width, int height, int strokeWidth) {
            super(ShapeType.RECTANGLE);
            this.x = x; this.y = y;
            this.width = (short)width; this.height = (short)height;
            this.stokeWidth = (byte)strokeWidth;
            color = new Color(1,1,1,1);
        }

        public RectangleOutline(int x, int y, int width, int height, int strokeWidth, Color color) {
            super(ShapeType.RECTANGLE);
            this.x = x; this.y = y;
            this.width = (short)width; this.height = (short)height;
            this.stokeWidth = (byte)strokeWidth;
            this.color = color;
        }

        @Override
        public void render() {
            Gdx.gl.glLineWidth(stokeWidth);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setTransformMatrix(Camera.mat);
            shapeRenderer.setColor(color);
            shapeRenderer.rect(x, y, width, height);
            shapeRenderer.end();
        }
    }

    public static class FilledRectangle extends GeometryRequest {

        int x, y;
        short width, height;
        byte stokeWidth;
        Color color;

        public FilledRectangle(int x, int y, int width, int height) {
            super(ShapeType.RECTANGLE);
            this.x = x; this.y = y;
            this.width = (short)width; this.height = (short)height;
            stokeWidth = 1;
            color = new Color(1,1,1,1);
        }

        public FilledRectangle(int x, int y, int width, int height, int strokeWidth) {
            super(ShapeType.RECTANGLE);
            this.x = x; this.y = y;
            this.width = (short)width; this.height = (short)height;
            this.stokeWidth = (byte)strokeWidth;
            color = new Color(1,1,1,1);
        }

        public FilledRectangle(int x, int y, int width, int height, int strokeWidth, Color color) {
            super(ShapeType.RECTANGLE);
            this.x = x; this.y = y;
            this.width = (short)width; this.height = (short)height;
            this.stokeWidth = (byte)strokeWidth;
            this.color = color;
        }

        @Override
        public void render() {
            Gdx.gl.glLineWidth(stokeWidth);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setTransformMatrix(Camera.mat);
            shapeRenderer.setColor(color);
            shapeRenderer.rect(x, y, width, height);
            shapeRenderer.end();
        }
    }

    public static class CircleOutline extends GeometryRequest {

        int x, y;
        short radius;
        byte width;
        Color color;

        public CircleOutline(int x, int y, int radius) {
            super(ShapeType.CIRCLE);
            this.x = x; this.y = y;
            this.radius = (short)radius;
            width = 1;
            color = new Color(1,1,1,1);
        }

        public CircleOutline(int x, int y, int radius, int width) {
            super(ShapeType.CIRCLE);
            this.x = x; this.y = y;
            this.radius = (short)radius;
            this.width = (byte)width;
            color = new Color(1,1,1,1);
        }

        public CircleOutline(int x, int y, int radius, int width, Color color) {
            super(ShapeType.CIRCLE);
            this.x = x; this.y = y;
            this.radius = (short)radius;
            this.width = (byte)width;
            this.color = color;
        }

        @Override
        public void render() {
            Gdx.gl.glLineWidth(width);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setTransformMatrix(Camera.mat);
            shapeRenderer.setColor(color);
            shapeRenderer.circle(x, y, radius);
            shapeRenderer.end();
        }
    }

    public static class FilledCircle extends GeometryRequest {

        int x, y;
        short radius;
        byte width;
        Color color;

        public FilledCircle(int x, int y, int radius) {
            super(ShapeType.CIRCLE);
            this.x = x; this.y = y;
            this.radius = (short)radius;
            width = 1;
            color = new Color(1,1,1,1);
        }

        public FilledCircle(int x, int y, int radius, int width) {
            super(ShapeType.CIRCLE);
            this.x = x; this.y = y;
            this.radius = (short)radius;
            this.width = (byte)width;
            color = new Color(1,1,1,1);
        }

        public FilledCircle(int x, int y, int radius, int width, Color color) {
            super(ShapeType.CIRCLE);
            this.x = x; this.y = y;
            this.radius = (short)radius;
            this.width = (byte)width;
            this.color = color;
        }

        @Override
        public void render() {
            Gdx.gl.glLineWidth(width);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setTransformMatrix(Camera.mat);
            shapeRenderer.setColor(color);
            shapeRenderer.circle(x, y, radius);
            shapeRenderer.end();
        }
    }

    public static class RoundedRectangleOutline extends GeometryRequest {

        int x, y;
        short width, height, radius;
        byte stokeWidth;
        Color color;

        public RoundedRectangleOutline(int x, int y, int width, int height, int radius) {
            super(ShapeType.ROUNDED_RECTANGLE);
            this.x = x; this.y = y;
            this.width = (short)width; this.height = (short)height;
            this.radius = (short)radius;
            stokeWidth = 1;
            color = new Color(1,1,1,1);
        }

        public RoundedRectangleOutline(int x, int y, int width, int height, int radius, int strokeWidth) {
            super(ShapeType.ROUNDED_RECTANGLE);
            this.x = x; this.y = y;
            this.width = (short)width; this.height = (short)height;
            this.radius = (short)radius;
            this.stokeWidth = (byte)strokeWidth;
            color = new Color(1,1,1,1);
        }

        public RoundedRectangleOutline(int x, int y, int width, int height, int radius, int strokeWidth, Color color) {
            super(ShapeType.ROUNDED_RECTANGLE);
            this.x = x; this.y = y;
            this.width = (short)width; this.height = (short)height;
            this.radius = (short)radius;
            this.stokeWidth = (byte)strokeWidth;
            this.color = color;
        }

        @Override
        public void render() {
            Gdx.gl.glLineWidth(stokeWidth);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setTransformMatrix(Camera.mat);
            shapeRenderer.setColor(color);
            shapeRenderer.rect(x + radius, y + radius, width - 2*radius, height - 2*radius);
            shapeRenderer.rect(x + radius, y, width - 2*radius, radius);
            shapeRenderer.rect(x + width - radius, y + radius, radius, height - 2*radius);
            shapeRenderer.rect(x + radius, y + height - radius, width - 2*radius, radius);
            shapeRenderer.rect(x, y + radius, radius, height - 2*radius);
            shapeRenderer.arc(x + radius, y + radius, radius, 180f, 90f);
            shapeRenderer.arc(x + width - radius, y + radius, radius, 270f, 90f);
            shapeRenderer.arc(x + width - radius, y + height - radius, radius, 0f, 90f);
            shapeRenderer.arc(x + radius, y + height - radius, radius, 90f, 90f);
            shapeRenderer.end();
        }
    }

    public static class FilledRoundedRectangle extends GeometryRequest {

        int x, y;
        short width, height, radius;
        byte stokeWidth;
        Color color;

        public FilledRoundedRectangle(int x, int y, int width, int height, int radius) {
            super(ShapeType.ROUNDED_RECTANGLE);
            this.x = x; this.y = y;
            this.width = (short)width; this.height = (short)height;
            this.radius = (short)radius;
            stokeWidth = 1;
            color = new Color(1,1,1,1);
        }

        public FilledRoundedRectangle(int x, int y, int width, int height, int radius, int strokeWidth) {
            super(ShapeType.ROUNDED_RECTANGLE);
            this.x = x; this.y = y;
            this.width = (short)width; this.height = (short)height;
            this.radius = (short)radius;
            this.stokeWidth = (byte)strokeWidth;
            color = new Color(1,1,1,1);
        }

        public FilledRoundedRectangle(int x, int y, int width, int height, int radius, int strokeWidth, Color color) {
            super(ShapeType.ROUNDED_RECTANGLE);
            this.x = x; this.y = y;
            this.width = (short)width; this.height = (short)height;
            this.radius = (short)radius;
            this.stokeWidth = (byte)strokeWidth;
            this.color = color;
        }

        @Override
        public void render() {
            Gdx.gl.glLineWidth(width);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setTransformMatrix(Camera.mat);
            shapeRenderer.setColor(color);
            shapeRenderer.rect(x + radius, y + radius, width - 2*radius, height - 2*radius);
            shapeRenderer.rect(x + radius, y, width - 2*radius, radius);
            shapeRenderer.rect(x + width - radius, y + radius, radius, height - 2*radius);
            shapeRenderer.rect(x + radius, y + height - radius, width - 2*radius, radius);
            shapeRenderer.rect(x, y + radius, radius, height - 2*radius);
            shapeRenderer.arc(x + radius, y + radius, radius, 180f, 90f);
            shapeRenderer.arc(x + width - radius, y + radius, radius, 270f, 90f);
            shapeRenderer.arc(x + width - radius, y + height - radius, radius, 0f, 90f);
            shapeRenderer.arc(x + radius, y + height - radius, radius, 90f, 90f);
            shapeRenderer.end();
        }
    }
}
