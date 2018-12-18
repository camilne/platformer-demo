package camilne.engine.physics;

import org.joml.Rectanglef;

public class AABB {
    private Rectanglef bounds;

    public AABB(float x, float y, float width, float height) {
        this(new Rectanglef(x, y, width, height));
    }

    public AABB(Rectanglef bounds) {
        this.bounds = bounds;
    }

    public boolean intersects(AABB other) {
        return !(other.bounds.maxX < bounds.minX)
                && !(other.bounds.minX > bounds.maxX)
                && !(other.bounds.maxY < bounds.minY)
                && !(other.bounds.minY > bounds.maxY);
    }

    public void setMinX(float minX) {
        bounds.minX = minX;
    }

    public void setMaxX(float maxX) {
        bounds.maxX = maxX;
    }

    public void setMinY(float minY) {
        bounds.minY = minY;
    }

    public void setMaxY(float maxY) {
        bounds.maxY = maxY;
    }
}
