package camilne.engine.graphics;

import camilne.engine.Camera;
import camilne.engine.GLUtil;
import camilne.engine.Sprite;
import camilne.engine.graphics.font.Font;
import org.lwjgl.BufferUtils;

import java.util.List;

import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public abstract class Batch {

    private int vao;
    private int vbo;
    private boolean drawing;
    private Shader shader;
    private Camera camera;

    public Batch() {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        GLUtil.checkError();

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0, Vertex.positionElementCount, GL_FLOAT, false, Vertex.stride, Vertex.positionByteOffset);
        glVertexAttribPointer(1, Vertex.textureElementCount, GL_FLOAT, false, Vertex.stride, Vertex.textureByteOffset);
        GLUtil.checkError();

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void begin() {
        if (drawing) {
            throw new RuntimeException("Batch already drawing");
        }
        drawing = true;
        onBegin();
    }

    public void end() {
        if (!drawing) {
            throw new RuntimeException("Batch is not drawing");
        }
        drawing = false;
        render();
    }

    public abstract void draw(Sprite sprite);

    public abstract void draw(TextureRegion region, float x, float y, float width, float height);

    /**
     * Draw a string with a certain font.
     *
     * @param font   The font to draw with.
     * @param string The string to draw. May contain newline characters.
     * @param x      The left of the string.
     * @param y      The top of the string.
     */
    public abstract void draw(Font font, String string, float x, float y);

    private void render() {
        if (!shouldRender() || shader == null || camera == null) {
            return;
        }

        shader.bind();
        shader.setUniform("u_mvp", camera.getCombinedMatrix());

        glBindVertexArray(vao);
        GLUtil.checkError();

        var vertices = createVertices();
        render(vertices);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    protected void render(List<Vertex> vertices) {
        if (vertices.isEmpty()) {
            return;
        }

        var vertexBuffer = BufferUtils.createFloatBuffer(vertices.size() * Vertex.elementCount);
        for (var vertex : vertices) {
            vertexBuffer.put(vertex.getElements());
        }
        vertexBuffer.flip();

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_DYNAMIC_DRAW);
        GLUtil.checkError();
        glDrawArrays(getRenderingMode(), 0, vertices.size());
        GLUtil.checkError();

        vertices.clear();
    }

    protected void onBegin() {
        // Empty
    }

    protected abstract boolean shouldRender();

    protected abstract List<Vertex> createVertices();

    protected abstract int getRenderingMode();

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader shader) {
        render();
        this.shader = shader;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        render();
        this.camera = camera;
    }

    protected boolean isDrawing() {
        return drawing;
    }

    public void destroy() {
        glDeleteBuffers(vbo);
        glDeleteVertexArrays(vao);
    }

}
