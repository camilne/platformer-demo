package camilne.engine.graphics;

import camilne.engine.Sprite;
import camilne.engine.graphics.font.Font;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

public class SpriteBatch extends Batch {

    private final List<SpriteEntry> sprites;

    private float zIndex;

    public SpriteBatch() {
        sprites = new ArrayList<>();
    }

    public void draw(Sprite sprite) {
        if (!isDrawing()) {
            throw new RuntimeException("SpriteBatch is not drawing");
        }
        sprites.add(new SpriteEntry(sprite, zIndex));
        zIndex += 0.01f;
    }

    public void draw(TextureRegion region, float x, float y, float width, float height) {
        if (!isDrawing()) {
            throw new RuntimeException("SpriteBatch is not drawing");
        }
        sprites.add(new SpriteEntry(region, x, y, width, height, zIndex));
        zIndex += 0.01f;
    }

    /**
     * Draw a string with a certain font.
     * @param font The font to draw with.
     * @param string The string to draw. May contain newline characters.
     * @param x The left of the string.
     * @param y The top of the string.
     */
    public void draw(Font font, String string, float x, float y) {
        if (!isDrawing()) {
            throw new RuntimeException("SpriteBatch is not drawing");
        }
        final var startX = x;
        for (var i = 0; i < string.length(); i++) {
            if (string.charAt(i) != '\n') {
                final var glyph = font.getGlyph(string.codePointAt(i));
                var glyphX = x;
                if (i >= 1 && x != startX) {
                    glyphX += font.getKerning(string.codePointAt(i - 1), string.codePointAt(i));
                }
                var glyphY = y - glyph.getOffsetY() - glyph.getRegion().getHeight();
                sprites.add(new SpriteEntry(glyph.getRegion(), glyphX, glyphY,
                        glyph.getRegion().getWidth(), glyph.getRegion().getHeight(), zIndex));
                x += glyph.getAdvance();
            } else {
                y -= font.getHeight();
                x = startX;
            }
        }
        zIndex += 0.01f;
    }

    @Override
    protected void onBegin() {
        sprites.clear();
        zIndex = 0f;
    }

    @Override
    protected List<Vertex> createVertices() {
        sprites.sort(Comparator.comparingInt((SpriteEntry a) -> a.getRegion().getTexture().getId()));

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
        return vertices;
    }

    @Override
    protected boolean shouldRender() {
        return !sprites.isEmpty();
    }

    @Override
    protected int getRenderingMode() {
        return GL_TRIANGLES;
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
