package camilne.engine.graphics.font;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class Font {

    private final int height;
    private final int baseline;
    private final int spacingX;
    private final int spacingY;
    private Map<Integer, Glyph> glyphs;
    private Map<KerningEntry, Integer> kernings;

    Font(int height, int baseline, int spacingX, int spacingY) {
        this.height = height;
        this.baseline = baseline;
        this.spacingX = spacingX;
        this.spacingY = spacingY;
        glyphs = new HashMap<>();
        kernings = new HashMap<>();
    }

    void addGlyph(Glyph glyph) {
        glyphs.put(glyph.getCodePoint(), glyph);
    }

    void addKerning(Kerning kerning) {
        kernings.put(new KerningEntry(kerning.getFirstCodePoint(), kerning.getSecondCodePoint()), kerning.getAmount());
    }

    protected Map<Integer, Glyph> getGlyphs() {
        return glyphs;
    }

    public Glyph getGlyph(int codePoint) {
        return glyphs.getOrDefault(codePoint, glyphs.get(0));
    }

    public int getKerning(int firstCodePoint, int secondCodePoint) {
        return kernings.getOrDefault(new KerningEntry(firstCodePoint, secondCodePoint), 0);
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

    private static final class KerningEntry {
        private final int first;
        private final int second;

        public KerningEntry(int first, int second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            KerningEntry that = (KerningEntry) o;
            return first == that.first &&
                    second == that.second;
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }
    }
}
