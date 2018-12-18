package camilne.engine.physics;

import camilne.engine.GameObject;

import java.util.*;

public class PhysicsWorld {

    private static PhysicsWorld instance;

    private int steps;
    private float gravity;
    private List<GameObject> objects;
    private Map<GameObject, Set<String>> collisionGroups;

    private PhysicsWorld() {
        steps = 10;
        this.objects = new ArrayList<>();
        this.collisionGroups = new HashMap<>();
    }

    public static synchronized PhysicsWorld getInstance() {
        if (instance == null) {
            instance = new PhysicsWorld();
        }
        return instance;
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
            }
            for (int i = 0; i < steps; i++) {
                var stepDy = object.getDy() * delta / steps;
                object.setY(object.getY() + stepDy);
                GameObject collider;
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
            if (!o.equals(object)
                    && o.getCollisionGroup() != null
                    && collisionGroups.containsKey(object)
                    && (collisionGroups.get(object).isEmpty()
                    || collisionGroups.get(object).contains(o.getCollisionGroup()))
                    && object.getBounds().intersects(o.getBounds())) {
                return o;
            }
        }
        return null;
    }

    public void addObject(GameObject object, Set<String> collisionGroups) {
        objects.add(object);
        this.collisionGroups.put(object, collisionGroups);
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }
}
