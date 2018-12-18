package camilne.engine.physics;

import camilne.engine.GameObject;

import java.util.ArrayList;
import java.util.List;

public class PhysicsWorld {

    private int steps;
    private float gravity;
    private List<GameObject> objects;

    public PhysicsWorld(float gravity) {
        steps = 10;
        this.gravity = gravity;
        this.objects = new ArrayList<>();
    }

    public void update(float delta) {
        applyGravity();

        for (var object : objects) {
            for (int i = 0; i < steps; i++) {
                var stepDx = object.getDx() * delta / steps;
                object.setX(object.getX() + stepDx);
                GameObject collider;
                if ((collider = doesCollide(object)) != null) {
                    if (!object.isTrigger() && !collider.isTrigger()) {
                        object.setX(object.getX() - stepDx);
                        object.setDx(0);
                        break;
                    }
                }

                var stepDy = object.getDy() * delta / steps;
                object.setY(object.getY() + stepDy);
                if ((collider = doesCollide(object)) != null) {
                    if (!object.isTrigger() && !collider.isTrigger()) {
                        object.setY(object.getY() - stepDy);
                        object.setDy(0);
                        break;
                    }
                }
            }
        }
    }

    private void applyGravity() {
        for (var object : objects) {
            if (object.isDynamic()) {
                object.setDy(object.getDy() + gravity);
            }
        }
    }

    private GameObject doesCollide(GameObject object) {
        for (var o : objects) {
            if (!o.equals(object) && object.getBounds().intersects(o.getBounds())) {
                return o;
            }
        }
        return null;
    }

    public void addObject(GameObject object) {
        objects.add(object);
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }
}
