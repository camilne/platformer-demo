package camilne.engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {

    private Matrix4f projection;
    private Matrix4f view;

    public Camera(int width, int height) {
        projection = new Matrix4f().ortho2D(0, width, 0, height);
        view = new Matrix4f().identity();
    }

    public void translate(Vector2f amount) {
        view.translate(new Vector3f(-amount.x, -amount.y, 0));
    }

    public Matrix4f getCombinedMatrix() {
        return projection.mul(view, new Matrix4f());
    }

}
