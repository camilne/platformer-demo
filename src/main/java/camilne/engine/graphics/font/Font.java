package camilne.engine.graphics.font;

import java.util.HashMap;
import java.util.Map;

public abstract class Font {

    private final int height;
    private final int baseline;
    private final int spacingX;
    private final int spacingY;
    private Map<Integer, Glyph> glyphs;

    Font(int height, int baseline, int spacingX, int spacingY) {
        this.height = height;
        this.baseline = baseline;
        this.spacingX = spacingX;
        this.spacingY = spacingY;
        glyphs = new HashMap<>();
    }

    void addGlyph(Glyph glyph) {
        glyphs.put(glyph.getCodePoint(), glyph);
    }

    protected Map<Integer, Glyph> getGlyphs() {
        return glyphs;
    }

    public Glyph getGlyph(int codePoint) {
        return glyphs.getOrDefault(codePoint, glyphs.get(0));
    }

    public int getHeight() {
        return height;
    }

    public int getBaseline() {
        return baseline;
    }

    public int getSpacingX() {
        return spacingX;
    }

    public int getSpacingY() {
        return spacingY;
    }
}
