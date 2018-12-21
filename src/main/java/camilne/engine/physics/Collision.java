package camilne.engine.physics;

import camilne.engine.GameObject;

import java.util.Objects;

class Collision {

    private final GameObject objA;
    private final GameObject objB;

    public Collision(GameObject objA, GameObject objB) {
        if (objA == null || objB == null) {
            throw new IllegalStateException("GameObject of collision cannot be null");
        }

        this.objA = objA;
        this.objB = objB;
    }

    public GameObject getObjA() {
        return objA;
    }

    public GameObject getObjB() {
        return objB;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Collision)) {
            return false;
        }

        var collision = (Collision) obj;

        return objA.equals(collision.getObjA()) && objB.equals(collision.getObjB());
    }

    @Override
    public int hashCode() {
        return Objects.hash(objA, objB);
    }
}
