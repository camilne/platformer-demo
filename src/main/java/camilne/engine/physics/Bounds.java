package camilne.engine.physics;

public abstract class Bounds {
    public abstract boolean intersects(Bounds other);
    public abstract boolean intersects(AABB other);
    public abstract boolean intersects(LineSegment other);
    public abstract boolean intersects(Triangle other);
    public abstract void update(float x, float y, float width, float height);
    public abstract Bounds copy();
    public abstract Bounds scale(float amount);
    public abstract Bounds translate(float x, float y);
}
