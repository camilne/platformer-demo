package camilne.engine.graphics.font;

import camilne.engine.graphics.TextureRegion;

public class Glyph {

    private final int codePoint;
    private final TextureRegion region;
    private final int offsetX;
    private final int offsetY;
    private final int advance;

    public Glyph(int codePoint, TextureRegion region, int offsetX, int offsetY, int advance) {
        this.codePoint = codePoint;
        this.region = region;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.advance = advance;
    }

    public int getCodePoint() {
        return codePoint;
    }

    public TextureRegion getRegion() {
        return region;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public int getAdvance() {
        return advance;
    }
}
