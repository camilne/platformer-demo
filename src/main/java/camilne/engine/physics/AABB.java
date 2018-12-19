package camilne.engine.physics;

import org.joml.Rectanglef;

public class AABB extends Bounds {
    private Rectanglef bounds;

    public AABB(float x, float y, float width, float height) {
        bounds = new Rectanglef(x, y, x + width, y + height);
    }

    @Override
    public boolean intersects(Bounds other) {
        return other.intersects(this);
    }

    @Override
    public boolean intersects(AABB other) {
        return Intersection.intersects(this, other);
    }

    @Override
    public boolean intersects(LineSegment other) {
        return Intersection.intersects(other, this);
    }

    @Override
    public boolean intersects(Triangle other) {
        return Intersection.intersects(other, this);
    }

    @Override
    public void update(float x, float y, float width, float height) {
        setMinX(x);
        setMinY(y);
        setMaxX(x + width);
        setMaxY(y + height);
    }

    public float getMinX() {
        return bounds.minX;
    }

    public void setMinX(float minX) {
        bounds.minX = minX;
    }

    public float getMaxX() {
        return bounds.maxX;
    }

    public void setMaxX(float maxX) {
        bounds.maxX = maxX;
    }

    public float getMinY() {
        return bounds.minY;
    }

    public void setMinY(float minY) {
        bounds.minY = minY;
    }

    public float getMaxY() {
        return bounds.maxY;
    }

    public void setMaxY(float maxY) {
        bounds.maxY = maxY;
    }

    @Override
    public Bounds copy(float scale) {
        var width = getMaxX() - getMinX();
        var height = getMaxY() - getMinY();
        return new AABB(getMinX(), getMinY(), getMinX() + width * scale, getMinY() + height * scale);
    }

    @Override
    public void translate(float x, float y) {
        setMinX(getMinX() + x);
        setMinY(getMinY() + y);
        setMaxX(getMaxX() + x);
        setMaxY(getMaxX() + x);
    }
}