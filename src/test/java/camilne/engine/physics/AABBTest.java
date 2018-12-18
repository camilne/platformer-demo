package camilne.engine.physics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AABBTest {

    @Test
    void doesIntersection() {
        var a = new AABB(0, 0, 5, 3);
        var b = new AABB(2, 2, 4, 5);

        assertTrue(a.intersects(b));
        assertTrue(b.intersects(a));

        a = new AABB(0, 0, 1, 1);
        b = new AABB(1, 0, 1, 1);

        assertTrue(a.intersects(b));
        assertTrue(b.intersects(a));
    }

}
