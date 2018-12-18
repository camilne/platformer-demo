package camilne.engine;

public class TextureRegion {

    private Texture texture;
    private float u;
    private float v;
    private float u2;
    private float v2;
    private int x;
    private int y;
    private int width;
    private int height;

    public TextureRegion(Texture texture) {
        this(texture, 0, 0, texture.getWidth(), texture.getHeight());
    }

    public TextureRegion(Texture texture, int x, int y, int width, int height) {
        this.texture = texture;
        setRegion(x, y, width, height);
    }

    public TextureRegion(TextureRegion region) {
        this.texture = region.getTexture();
        this.u = region.u;
        this.v = region.v;
        this.u2 = region.u2;
        this.v2 = region.v2;
        this.x = region.x;
        this.y = region.y;
        this.width = region.width;
        this.height = region.height;
    }

    private void setRegion(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        var invWidth = 1f / texture.getWidth();
        var invHeight = 1f / texture.getHeight();

        this.u = x * invWidth;
        this.v = y * invHeight;
        this.u2 = (x + width) * invWidth;
        this.v2 = (y + height) * invHeight;
    }

    public TextureRegion offset(int x, int y) {
        setRegion(getX() + x, getY() + y, width, height);
        return this;
    }

    public Texture getTexture() {
        return texture;
    }

    public float getU() {
        return u;
    }

    public float getV() {
        return v;
    }

    public float getU2() {
        return u2;
    }

    public float getV2() {
        return v2;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
