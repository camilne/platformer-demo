package camilne.engine.physics;

import org.joml.Vector2f;

public class Triangle extends Bounds {

    private Vector2f p1;
    private Vector2f p2;
    private Vector2f p3;

    public Triangle(Vector2f p1, Vector2f p2, Vector2f p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
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
        return false;
//        throw new UnsupportedOperationException();
    }

    @Override
    public void update(float x, float y, float width, float height) {
//        p1.add(x, y);
//        p2.add(x, y);
//        p3.add(x, y);
//        throw new UnsupportedOperationException();
    }

    public AABB getBoundingBox() {
        var minX = Math.min(Math.min(p1.x, p2.x), p3.x);
        var minY = Math.min(Math.min(p1.y, p2.y), p3.y);
        var maxX = Math.max(Math.max(p1.x, p2.x), p3.x);
        var maxY = Math.max(Math.max(p1.y, p2.y), p3.y);
        return new AABB(minX, minY, maxX - minX, maxY - minY);
    }

    public double getArea() {
        return 0.5f * (-p2.y * p3.x + p1.y * (-p2.x + p3.x) + p1.x * (p2.y - p3.y) + p2.x * p3.x);
    }

    public Vector2f getP1() {
        return p1;
    }

    public Vector2f getP2() {
        return p2;
    }

    public Vector2f getP3() {
        return p3;
    }

    @Override
    public Bounds copy(float scale) {
        var newP2 = new Vector2f(p2).sub(p1).mul(scale);
        var newP3 = new Vector2f(p3).sub(p1).mul(scale);
        return new Triangle(new Vector2f(p1), newP2, newP3);
    }

    @Override
    public void translate(float x, float y) {
        p1.add(x, y);
        p2.add(x, y);
        p3.add(x, y);
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "p1=" + p1 +
                ", p2=" + p2 +
                ", p3=" + p3 +
                '}';
    }
}
