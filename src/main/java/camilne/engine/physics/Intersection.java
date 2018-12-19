package camilne.engine.physics;

import org.joml.Vector2f;

public class Intersection {
    
    public static boolean intersects(AABB a, AABB b) {
        return b.getMaxX() > a.getMinX() && b.getMinX() < a.getMaxX()
                && b.getMaxY() > a.getMinY() && b.getMinY() < a.getMaxY();
    }
    
    public static boolean intersects(AABB aabb, Vector2f p) {
        return p.x > aabb.getMinX() && p.x < aabb.getMaxX()
                && p.y > aabb.getMinY() && p.y < aabb.getMaxY();
    }

    public static boolean intersects(LineSegment a, LineSegment b) {
        var s1X = a.getP2().x - a.getP1().x;
        var s1Y = a.getP2().y - a.getP1().y;
        var s2X = b.getP2().x - b.getP1().x;
        var s2Y = b.getP2().y - b.getP1().y;

        var denom = 1f / (-s2X * s1Y + s1X * s2Y);
        float s = (-s1Y * (a.getP1().x - b.getP1().x) + s1X * (a.getP1().y - b.getP1().y)) * denom;
        float t = (s2X * (a.getP1().y - b.getP1().y) - s2Y * (a.getP1().x - b.getP1().x)) * denom;

        return s > 0 && s < 1 && t > 0 && t < 1;
    }

    public static boolean intersects(LineSegment line, AABB aabb) {
        var left = new LineSegment(aabb.getMinX(), aabb.getMinY(), aabb.getMinX(), aabb.getMaxY());
        var right = new LineSegment(aabb.getMaxX(), aabb.getMinY(), aabb.getMaxX(), aabb.getMaxY());
        var top = new LineSegment(aabb.getMinX(), aabb.getMaxY(), aabb.getMaxX(), aabb.getMaxY());
        var bottom = new LineSegment(aabb.getMinX(), aabb.getMinY(), aabb.getMaxX(), aabb.getMinY());

        return intersects(aabb, line.getP1()) || intersects(aabb, line.getP2())
                || intersects(line, left) || intersects(line, right)
                || intersects(line, top) || intersects(line, bottom);
    }

    public static boolean intersects(Triangle t, AABB aabb) {
        if (!t.getBoundingBox().intersects(aabb)) {
            return false;
        }

        if (new LineSegment(t.getP1(), t.getP2()).intersects(aabb)) {
            return true;
        }
        if (new LineSegment(t.getP2(), t.getP3()).intersects(aabb)) {
            return true;
        }
        if (new LineSegment(t.getP1(), t.getP3()).intersects(aabb)) {
            return true;
        }
        return false;
    }

    public static boolean intersects(Triangle triangle, LineSegment line) {
        var l1 = new LineSegment(triangle.getP1(), triangle.getP2());
        var l2 = new LineSegment(triangle.getP2(), triangle.getP3());
        var l3 = new LineSegment(triangle.getP1(), triangle.getP3());

        return intersects(triangle, line.getP1()) || intersects(triangle, line.getP2())
                || intersects(line, l1) || intersects(line, l2) || intersects(line, l3);
    }

    public static boolean intersects(Triangle triangle, Vector2f p) {
        var area = triangle.getArea();
        var sign = area < 0 ? -1 : 1;
        var p0 = triangle.getP1();
        var p1 = triangle.getP2();
        var p2 = triangle.getP3();
        var s = (p0.y * p2.x - p0.x * p2.y + (p2.y - p0.y) * p.x + (p0.x - p2.x) * p.y) * sign;
        var t = (p0.x * p1.y - p0.y * p1.x + (p0.y - p1.y) * p.x + (p1.x - p0.x) * p.y) * sign;

        return s > 0 && t > 0 && (s + t) < 2 * area * sign;
    }
    
}
