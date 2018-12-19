package camilne.engine.physics;

import java.util.ArrayList;
import java.util.List;

public class BoundingSet {

    private List<Bounds> bounds;

    public BoundingSet() {
        bounds = new ArrayList<>();
    }

    public boolean intersects(BoundingSet other) {
        for (var bound : bounds) {
            for (var otherBound : other.bounds) {
                if (bound.intersects(otherBound)) {
                    return true;
                }
            }
        }
        return false;
    }

}
