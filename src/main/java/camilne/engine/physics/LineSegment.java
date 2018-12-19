package camilne.engine.physics;

import org.joml.Vector2f;

public class LineSegment extends Bounds {

    private Vector2f p1;
    private Vector2f p2;

    public LineSegment(Vector2f p1, Vector2f p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public LineSegment(float x1, float y1, float x2, float y2) {
        this.p1 = new Vector2f(x1, y1);
        this.p2 = new Vector2f(x2, y2);
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
        return Intersection.intersects(this, other);
    }

    @Override
    public boolean intersects(Triangle other) {
        return Intersection.intersects(other, this);
    }

    @Override
    public void update(float x, float y, float width, float height) {
        throw new UnsupportedOperationException();
    }

    public Vector2f getP1() {
        return p1;
    }

    public Vector2f getP2() {
        return p2;
    }

    @Override
    public Bounds copy() {
        return new LineSegment(new Vector2f(p1), new Vector2f(p2).sub(p1));
    }

    @Override
    public void scale(float amount) {
        p2.sub(p1).mul(amount).add(p1);
    }

    @Override
    public void translate(float x, float y) {
        p1.add(x, y);
        p2.add(x, y);
    }
}
