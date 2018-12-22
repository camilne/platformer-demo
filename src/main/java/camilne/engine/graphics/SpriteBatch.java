package camilne.engine.graphics;

import camilne.engine.Camera;
import camilne.engine.Sprite;
import camilne.engine.graphics.font.Font;
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
    private Shader shader;
    private Camera camera;

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

    public void draw(TextureRegion region, float x, float y, float width, float height) {
        if (!isDrawing) {
            throw new RuntimeException("SpriteBatch is not drawing");
        }
        sprites.add(new SpriteEntry(region, x, y, width, height, zIndex));
        zIndex += 0.01f;
    }

    public void draw(Font font, String string, float x, float y) {
        if (!isDrawing) {
            throw new RuntimeException("SpriteBatch is not drawing");
        }
        final var startX = x;
        y += font.getSpacingY();
        for (var i = 0; i < string.length(); i++) {
            if (string.charAt(i) != '\n') {
                var glyph = font.getGlyph(string.codePointAt(i));
                var glyphX = x + glyph.getOffsetX();
                if (i >= 1 && x != startX) {
                    glyphX += font.getKerning(string.codePointAt(i - 1), string.codePointAt(i));
                }
                var glyphY = y - glyph.getRegion().getHeight() - (glyph.getOffsetY() - font.getSpacingY());
                sprites.add(new SpriteEntry(glyph.getRegion(), glyphX, glyphY,
                        glyph.getRegion().getWidth(), glyph.getRegion().getHeight(), zIndex));
                x += glyph.getAdvance();
            } else {
                y -= font.getHeight() + font.getSpacingY();
                x = startX;
            }
        }
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
        if (sprites.isEmpty()) {
            return;
        }

        sprites.sort(Comparator.comparingInt((SpriteEntry a) -> a.getRegion().getTexture().getId()));

        if (shader != null && camera != null) {
            shader.bind();
            shader.setUniform("u_mvp", camera.getCombinedMatrix());
        }

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        List<Vertex> vertices = new ArrayList<>();

        Texture lastTexture = null;
        for (var item : sprites) {
            if (lastTexture == null || lastTexture != item.getRegion().getTexture()) {
                render(vertices);
                lastTexture = item.getRegion().getTexture();
                lastTexture.bind();
            }

            var z = item.getZIndex();

            var u = item.getRegion().getU();
            var v = item.getRegion().getV();
            var u2 = item.getRegion().getU2();
            var v2 = item.getRegion().getV2();

            vertices.add(new Vertex(item.getX(), item.getY(), z, u, v2));
            vertices.add(new Vertex(item.getX() + item.getWidth(), item.getY(), z, u2, v2));
            vertices.add(new Vertex(item.getX() + item.getWidth(), item.getY() + item.getHeight(), z, u2, v));
            vertices.add(new Vertex(item.getX() + item.getWidth(), item.getY() + item.getHeight(), z, u2, v));
            vertices.add(new Vertex(item.getX(), item.getY() + item.getHeight(), z, u, v));
            vertices.add(new Vertex(item.getX(), item.getY(), z, u, v2));
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

    private final class SpriteEntry {

        private final TextureRegion region;
        private final float x;
        private final float y;
        private final float width;
        private final float height;
        private final float zIndex;

        SpriteEntry(Sprite sprite, float zIndex) {
            this.region = sprite.getAnimation().getCurrentFrame();
            this.x = sprite.getX();
            this.y = sprite.getY();
            this.width = sprite.getWidth();
            this.height = sprite.getHeight();
            this.zIndex = zIndex;
        }

        public SpriteEntry(TextureRegion region, float x, float y, float width, float height, float zIndex) {
            this.region = region;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.zIndex = zIndex;
        }

        TextureRegion getRegion() {
            return region;
        }

        float getX() {
            return x;
        }

        float getY() {
            return y;
        }

        float getWidth() {
            return width;
        }

        float getHeight() {
            return height;
        }

        float getZIndex() {
            return zIndex;
        }
    }

}
