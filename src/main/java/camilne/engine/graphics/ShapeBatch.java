package camilne.engine.graphics;

import camilne.engine.Sprite;
import camilne.engine.graphics.font.Font;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.GL_LINES;

public class ShapeBatch extends Batch {

    private final List<ShapeEntry> shapes;

    public ShapeBatch() {
        shapes = new ArrayList<>();
    }

    @Override
    public void draw(Sprite sprite) {
        if (!isDrawing()) {
            throw new RuntimeException("ShapeBatch is not drawing");
        }
        shapes.add(new ShapeEntry(sprite));
    }

    @Override
    public void draw(TextureRegion region, float x, float y, float width, float height) {
        if (!isDrawing()) {
            throw new RuntimeException("ShapeBatch is not drawing");
        }
        shapes.add(new ShapeEntry(x, y, width, height));
    }

    @Override
    public void draw(Font font, String string, float x, float y) {
        if (!isDrawing()) {
            throw new RuntimeException("ShapeBatch is not drawing");
        }
        var width = font.getStringWidth(string);
        var height = font.getStringHeight(string);
        shapes.add(new ShapeEntry(x, y - height, width, height));
    }

    @Override
    protected boolean shouldRender() {
        return !shapes.isEmpty();
    }

    @Override
    protected List<Vertex> createVertices() {
        List<Vertex> vertices = new ArrayList<>();
        for (var item : shapes) {
            final var z = 1f;
            vertices.add(new Vertex(item.getX(), item.getY(), z, 0, 0));
            vertices.add(new Vertex(item.getX() + item.getWidth(), item.getY(), z, 0, 0));
            vertices.add(new Vertex(item.getX() + item.getWidth(), item.getY(), z, 0, 0));
            vertices.add(new Vertex(item.getX() + item.getWidth(), item.getY() + item.getHeight(), z, 0, 0));
            vertices.add(new Vertex(item.getX() + item.getWidth(), item.getY() + item.getHeight(), z, 0, 0));
            vertices.add(new Vertex(item.getX(), item.getY() + item.getHeight(), z, 0, 0));
            vertices.add(new Vertex(item.getX(), item.getY() + item.getHeight(), z, 0, 0));
            vertices.add(new Vertex(item.getX(), item.getY(), z, 0, 0));
        }
        return vertices;
    }

    @Override
    protected int getRenderingMode() {
        return GL_LINES;
    }

    private static final class ShapeEntry {

        private final float x;
        private final float y;
        private final float width;
        private final float height;

        ShapeEntry(Sprite sprite) {
            this.x = sprite.getX();
            this.y = sprite.getY();
            this.width = sprite.getWidth();
            this.height = sprite.getHeight();
        }

        public ShapeEntry(float x, float y, float width, float height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
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
    }
}
