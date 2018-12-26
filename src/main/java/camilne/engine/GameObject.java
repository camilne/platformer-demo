package camilne.engine;

import camilne.engine.audio.AudioPool;
import camilne.engine.audio.Source;
import camilne.engine.general.Property;
import camilne.engine.physics.AABB;
import camilne.engine.physics.Bounds;
import org.joml.Vector2f;

public class GameObject {

    private float x;
    private float y;
    private float width;
    private float height;
    private float dx;
    private float dy;
    private Bounds bounds;
    private boolean dynamic;
    private boolean trigger;
    private String collisionGroup;
    private Property<Boolean> grounded;
    private Source source;

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
        this.collisionGroup = null;
        this.grounded = new Property<>(false);
    }

    private void updateBounds() {
        bounds.update(x, y, width, height);
        source.setPosition(new Vector2f(x + width / 2f, y + height / 2f));
    }

    private void updateVelocity() {
        source.setVelocity(new Vector2f(dx, dy));
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
        updateVelocity();
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy) {
        this.dy = dy;
        updateVelocity();
    }

    public void onEnter(GameObject other) {
        // Empty
    }

    public void onExit(GameObject other) {
        // Empty
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
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

    public String getCollisionGroup() {
        return collisionGroup;
    }

    public void setCollisionGroup(String collisionGroup) {
        this.collisionGroup = collisionGroup;
    }

    public Property<Boolean> groundedProperty() {
        return grounded;
    }

    public boolean isGrounded () {
        return grounded.get();
    }

    public void setGrounded(boolean grounded) {
        this.grounded.set(grounded);
    }

    public Source getSource() {
        if (source == null) {
            source = AudioPool.getInstance().createSource();
            source.setPitch(1f);
            source.setVolume(1f);
        }
        return source;
    }
}
