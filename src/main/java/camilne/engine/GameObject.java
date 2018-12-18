package camilne.engine;

import camilne.engine.physics.AABB;

public abstract class GameObject {

    private float x;
    private float y;
    private float width;
    private float height;
    private float dx;
    private float dy;
    private AABB bounds;
    private boolean dynamic;
    private boolean trigger;

    public GameObject(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dx = 0;
        this.dy = 0;
        this.bounds = new AABB(x, y, width, height);
        this.dynamic = false;
        this.trigger = false;
    }

    private void updateBounds() {
        bounds.setMinX(x);
        bounds.setMaxX(x + width);
        bounds.setMinY(y);
        bounds.setMaxY(y + height);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        updateBounds();
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        updateBounds();
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
        updateBounds();
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
        updateBounds();
    }

    public float getDx() {
        return dx;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    public AABB getBounds() {
        return bounds;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public boolean isTrigger() {
        return trigger;
    }

    public void setTrigger(boolean trigger) {
        this.trigger = trigger;
    }
}
