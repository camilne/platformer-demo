package camilne.engine;

import org.lwjgl.BufferUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class SpriteBatch {

    private final List<SpriteEntry> sprites;

    private boolean isDrawing;
    private int vbo;
    private float zIndex;

    public SpriteBatch() {
        sprites = new ArrayList<>();

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0, Vertex.positionElementCount, GL_FLOAT, false, Vertex.stride, Vertex.positionByteOffset);
        glVertexAttribPointer(1, Vertex.textureElementCount, GL_FLOAT, false, Vertex.stride, Vertex.textureByteOffset);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void draw(Sprite sprite) {
        if (!isDrawing) {
            throw new RuntimeException("SpriteBatch is not drawing");
        }
        sprites.add(new SpriteEntry(sprite, zIndex));
        zIndex += 0.01f;
    }

    public void begin() {
        if (isDrawing) {
            throw new RuntimeException("SpriteBatch already drawing");
        }
        isDrawing = true;
        sprites.clear();
        zIndex = 0;
    }

    public void end() {
        if (!isDrawing) {
            throw new RuntimeException("SpriteBatch is not drawing");
        }
        isDrawing = false;
        render();
    }

    private void render() {
        sprites.sort(Comparator.comparingInt((SpriteEntry a) -> a.getSprite().getTexture().getId()));

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        List<Vertex> vertices = new ArrayList<>();

        Texture lastTexture = null;
        for (var spriteEntry : sprites) {
            var sprite = spriteEntry.getSprite();
            if (lastTexture == null || lastTexture != sprite.getTexture()) {
                render(vertices);
                lastTexture = sprite.getTexture();
                lastTexture.bind();
            }

            vertices.add(new Vertex(sprite.getX() - sprite.getWidth() / 2, sprite.getY() - sprite.getHeight() / 2, spriteEntry.getZIndex(), 0.0f, 0.0f));
            vertices.add(new Vertex(sprite.getX() + sprite.getWidth() / 2, sprite.getY() - sprite.getHeight() / 2, spriteEntry.getZIndex(), 1.0f, 0.0f));
            vertices.add(new Vertex(sprite.getX() + sprite.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2, spriteEntry.getZIndex(), 1.0f, 1.0f));
            vertices.add(new Vertex(sprite.getX() + sprite.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2, spriteEntry.getZIndex(), 1.0f, 1.0f));
            vertices.add(new Vertex(sprite.getX() - sprite.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2, spriteEntry.getZIndex(), 0.0f, 1.0f));
            vertices.add(new Vertex(sprite.getX() - sprite.getWidth() / 2, sprite.getY() - sprite.getHeight() / 2, spriteEntry.getZIndex(), 0.0f, 0.0f));
        }
        render(vertices);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
    }

    private void render(List<Vertex> vertices) {
        if (!vertices.isEmpty()) {
            var vertexBuffer = BufferUtils.createFloatBuffer(vertices.size() * Vertex.elementCount);
            for (var vertex : vertices) {
                vertexBuffer.put(vertex.getElements());
            }
            vertexBuffer.flip();

            glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_DYNAMIC_DRAW);
            glDrawArrays(GL_TRIANGLES, 0, vertices.size());

            vertices.clear();
        }
    }

}
