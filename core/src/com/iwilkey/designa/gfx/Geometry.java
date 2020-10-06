package com.iwilkey.designa.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Geometry {

    public enum ShapeType {
        LINE, RECTANGLE, CIRCLE;
    }

    static ShapeRenderer shapeRenderer = new ShapeRenderer();

    public static abstract class GeometryRequest {
        protected ShapeType shapeType;
        public GeometryRequest(ShapeType shapeType) {
            this.shapeType = shapeType;
        }
        public abstract void render();
        public ShapeType getShapeType() { return shapeType; }
    }


    public static class LineRequest extends GeometryRequest {

        short x1, x2, y1, y2;

        public LineRequest(int x1, int y1, int x2, int y2) {
            super(ShapeType.LINE);
            this.x1 = (short)x1; this.x2 = (short)x2;
            this.y1 = (short)y1; this.y2 = (short)y2;
        }

        @Override
        public void render() {
            Gdx.gl.glLineWidth(1);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setTransformMatrix(Camera.mat);
            shapeRenderer.setColor(1, 0, 0, 1);
            shapeRenderer.line(x1, y1, x2, y2);
            shapeRenderer.end();
        }
    }

}
