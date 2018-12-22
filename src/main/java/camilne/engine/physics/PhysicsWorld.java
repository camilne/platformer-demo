package camilne.engine.physics;

import camilne.engine.GameObject;

import java.util.*;

public class PhysicsWorld {

    private static PhysicsWorld instance;

    private int steps;
    private float gravity;
    private List<GameObject> objects;
    private Map<GameObject, Set<String>> collisionGroups;
    private Set<Collision> lastFrameCollisions;

    private PhysicsWorld() {
        steps = 10;
        this.objects = new ArrayList<>();
        this.collisionGroups = new HashMap<>();
        this.lastFrameCollisions = new HashSet<>();
    }

    public static synchronized PhysicsWorld getInstance() {
        if (instance == null) {
            instance = new PhysicsWorld();
        }
        return instance;
    }

    public void update(float delta) {
        applyGravity();

        var thisFrameCollisions = new HashSet<Collision>();
        for (var object : objects) {
            updateX(delta, object);
            updateY(delta, object);
            for (var collider : getColliders(object)) {
                var collision = new Collision(object, collider);
                thisFrameCollisions.add(collision);
                if (!lastFrameCollisions.contains(collision)) {
                    object.onEnter(collider);
                }
            }
        }

        lastFrameCollisions.removeAll(thisFrameCollisions);
        for (var collision : lastFrameCollisions) {
            collision.getObjA().onExit(collision.getObjB());
        }

        lastFrameCollisions = thisFrameCollisions;
    }

    private void applyGravity() {
        for (var object : objects) {
            if (object.isDynamic()) {
                object.setDy(object.getDy() + gravity);
            }
        }
    }

    private List<GameObject> getColliders(GameObject object) {
        List<GameObject> objects = new ArrayList<>();
        for (var o : this.objects) {
            if (doesCollide(object, o)) {
                objects.add(o);
            }
        }
        return objects;
    }

    private boolean doesCollide(GameObject a, GameObject b) {
        return shouldCollide(a, b) && a.getBounds().intersects(b.getBounds());
    }

    private boolean shouldCollide(GameObject a, GameObject b) {
        if (a == b || b.getCollisionGroup() == null || !collisionGroups.containsKey(a)) {
            return false;
        }

        return  collisionGroups.get(a).isEmpty() || collisionGroups.get(a).contains(b.getCollisionGroup());
    }

    private void updateX(float delta, GameObject object) {
        for (int i = 0; i < steps; i++) {
            var stepDx = object.getDx() * delta / steps;
            object.setX(object.getX() + stepDx);

            if (slopeBelow(object)) {
                object.setY(object.getY() - Math.abs(stepDx));
                if (!getColliders(object).isEmpty()) {
                    object.setY(object.getY() + Math.abs(stepDx));
                }
            }

            for (var collider : getColliders(object)) {
                if (!object.isTrigger() && !collider.isTrigger()) {
                    if (collider.getBounds() instanceof SlopeLeft || collider.getBounds() instanceof SlopeRight) {
                        object.setY(object.getY() + Math.abs(stepDx * 2));
                        if (!getColliders(object).isEmpty()) {
                            object.setY(object.getY() - Math.abs(stepDx * 2));
                            object.setX(object.getX() - stepDx);
                            object.setDx(0);
                            return;
                        }
                    } else {
                        object.setX(object.getX() - stepDx);
                        object.setDx(0);
                        return;
                    }
                }
            }
        }
    }

    private boolean slopeBelow(GameObject object) {
        final var testAmount = 20;
        var test = object.getBounds().copy().translate(0, -testAmount);
        for (var o : objects) {
            if (object != o && test.intersects(o.getBounds()) && (o.getBounds() instanceof SlopeRight || o.getBounds() instanceof SlopeLeft)) {
                return true;
            }
        }
        return false;
    }

    private void updateY(float delta, GameObject object) {
        for (int i = 0; i < steps; i++) {
            var stepDy = object.getDy() * delta / steps;
            object.setY(object.getY() + stepDy);
            for (var collider : getColliders(object)) {
                if (!object.isTrigger() && !collider.isTrigger()) {
                    object.setGrounded(true);
                    object.setY(object.getY() - stepDy);
                    object.setDy(0);
                    return;
                }
            }
        }
        object.setGrounded(false);
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
