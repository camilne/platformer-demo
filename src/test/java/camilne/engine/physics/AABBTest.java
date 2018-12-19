package camilne.engine.physics;

import org.joml.Vector2f;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AABBTest {

    @Test
    void testConstructor() {
        var aabb = new AABB(1, 2, 3, 4);

        assertEquals(1, aabb.getMinX());
        assertEquals(2, aabb.getMinY());
        assertEquals(4, aabb.getMaxX());
        assertEquals(6, aabb.getMaxY());
    }

    @Test
    void doesIntersectionWithAABB() {
        var a = new AABB(0, 0, 5, 3);
        var b = new AABB(2, 2, 4, 5);

        assertTrue(a.intersects(b));
        assertTrue(b.intersects(a));

        a = new AABB(0, 0, 1, 1);
        b = new AABB(1, 0, 1, 1);

        assertTrue(a.intersects(b));
        assertTrue(b.intersects(a));
    }

    @Test
    void doesIntersectionWithLineSegment() {
        var aabb = new AABB(1, 1, 2, 3);
        var line = new LineSegment(0, 2, 2, 0);

        assertTrue(aabb.intersects(line));

        line = new LineSegment(0, 1, 2, 0);

        assertFalse(aabb.intersects(line));
    }

    @Test
    void doesIntersectionWithTriangle() {
        var a = new AABB(1, 1, 2, 3);
        var b = new Triangle(new Vector2f(0,  0), new Vector2f(2, 0), new Vector2f(0, 3));

        assertTrue(a.intersects(b));

        b = new Triangle(new Vector2f(0,  0), new Vector2f(1.5f, 0), new Vector2f(0, 1.5f));

        assertFalse(a.intersects(b));

        b = new Triangle(new Vector2f(0,  0), new Vector2f(0.5f, 0), new Vector2f(0, 0.5f));

        assertFalse(a.intersects(b));
    }

}
