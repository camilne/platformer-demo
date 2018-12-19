package camilne.engine.physics;

import org.joml.Vector2f;

public class SlopeLeft extends Triangle {

    public SlopeLeft(float x, float y, float size) {
        super(new Vector2f(x, y), new Vector2f(x + size, y), new Vector2f(x + size, y + size));
    }

    @Override
    public Bounds copy() {
        return new SlopeLeft(getP1().x, getP1().y, getP2().x - getP1().x);
    }
}
