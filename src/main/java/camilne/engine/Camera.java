package camilne.engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {

    private Matrix4f projection;
    private Matrix4f view;
    private int width;
    private int height;

    public Camera(int width, int height) {
        this.width = width;
        this.height = height;

        projection = new Matrix4f().ortho2D(0, width, 0, height);
        view = new Matrix4f().identity();
    }

    public void translate(Vector2f amount) {
        view.translate(new Vector3f(-amount.x, -amount.y, 0));
    }

    public void centerOn(Vector2f point) {
        var offset = new Vector3f(width / 2f, height / 2f, 0f);
        var eye = new Vector3f(point, 1.0f).sub(offset);
        var center = new Vector3f(point, 0f).sub(offset);
        var up = new Vector3f(0, 1, 0);
        view.setLookAt(eye, center, up);
    }

    public Matrix4f getCombinedMatrix() {
        return projection.mul(view, new Matrix4f());
    }

}
